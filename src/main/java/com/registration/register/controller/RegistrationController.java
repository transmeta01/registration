package com.registration.register.controller;

import com.registration.register.entity.Course;
import com.registration.register.entity.Registration;
import com.registration.register.entity.Student;
import com.registration.register.messages.ResponseMessage;
import com.registration.register.repository.CourseRepository;
import com.registration.register.repository.RegistrationRepository;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/v1/registration")
public class RegistrationController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    enum FilterMode {
        STUDENT_WITHOUT_COURSE("without_course"),
        STUDENT_PER_COURSE("by_course"),
        COURSE_BY_STUDENT_ID("by_student"),
        COURSE_WITHOUT_STUDENT("without_student");

        String mode;

        FilterMode(String mode) {
            this.mode = mode;
        }
    }

    @PostMapping(
            path = "/student/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseMessage> addStudent(@RequestBody Student student) {
        Student addedStudent  = studentRepository.saveAndFlush(student);

        Map<String, String> metadata = new HashMap<>();
        URI uri = URI.create(String.format("/student/%s", addedStudent.getId()));
        metadata.put("uri",  uri.getPath());

        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .message("Student added to registrar")
                .payload(addedStudent)
                .metadata(metadata)
                .build();

        return ResponseEntity.created(uri).body(responseMessage);
    }

    @PutMapping(path = "/student/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    @PostMapping(
            path = "/student/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        try {
            studentRepository.save(student);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(student);
        }

        return ResponseEntity.accepted().body(student);
    }

    @PutMapping(
            path = "/student/add/{studentId}/{courseId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseMessage> addCourseToStudent(@PathVariable Long studentId, @PathVariable Long courseId) {
        Map<String, String> metadata = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        String message = "";

        ResponseMessage responseMessage = ResponseMessage
                                                .builder()
                                                .metadata(metadata)
                                                .message(message)
                                                .build();

        if (studentRepository.findById(studentId).isPresent() &&
                courseRepository.findById(courseId).isPresent()) {

            Student student = studentRepository.findById(studentId).get();
            Course course = courseRepository.findById(courseId).get();

            try {
                // is the student already registered to the course?
                if(student.getCourse().contains(course)) {
                    metadata.put("Status", HttpStatus.NOT_ACCEPTABLE.toString());
                    message = String.format("Student with id: %id, is already registered to course %courseId",
                                                    student.getId(), courseId);

                    status = HttpStatus.NOT_ACCEPTABLE;

                    // TODO augment with other metadata/event for analytics
                    log.info(message);
                }

                // a student can register to 5 courses and
                // a course can have 50 registered students at most
                if(student.getCourse().size() < 5 && course.getCount() < 50) {
                    int count = course.getCount() + 1;
                    course.setCount(count);

                    Registration registration = Registration
                                                    .builder()
                                                    .course(course)
                                                    .student(student)
                                                    .build();

                    log.info("Saving registration", registration.toString());
                    registrationRepository.save(registration);

                    student.getCourse().add(course);
                    int courseCount = student.getCount() + 1;
                    student.setCount(courseCount);
                    student = studentRepository.saveAndFlush(student);

                    message = String.format("course added to student : %s", student.toString());
                    log.info(message, student);

                    status = HttpStatus.ACCEPTED;

                } else {
                    message = String.format("Quota exceed: Student %d, can have 50 courses at once at most. " +
                                "Course %d can have 50 students at once at most", student.getId(), courseId);

                    status = HttpStatus.NOT_ACCEPTABLE;
                    metadata.put("Status", HttpStatus.NOT_ACCEPTABLE.toString());

                    log.info(message);
                }
            } catch (Exception e) {
                status = HttpStatus.BAD_REQUEST;
                log.error("ERROR", e.getStackTrace());
                metadata.put("Status", HttpStatus.BAD_REQUEST.toString());
                metadata.put("Error", "An error has occurred");
            }
        }

        return ResponseEntity.status(status).body(responseMessage);
    }

    /**
     *
     * path example:
     *              /course/filter?mode=by_student&student_id=5
     *              /course/filter/ and /course/filter?mode=without_student
     *
     * acceptable values :
     *
     * COURSE_BY_STUDENT_ID("by_student"),
     * COURSE_WITHOUT_STUDENT("without_student");
     *
     * @param mode filter mode
     * @param id student id
     * @return ResponseEntity, augmented with metadata
     */
    @GetMapping(
            path = "/course/filter",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseMessage> filterCourse(@RequestParam(defaultValue = "without_student") String mode,
                                                                       @RequestParam(required = false) Long id) {
        FilterMode filterMode = FilterMode.valueOf(mode);

        Map<String, String> meta = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        String message = "";

        ResponseMessage responseMessage = ResponseMessage
                                            .builder()
                                            .metadata(meta)
                                            .message(message)
                                            .build();

        List<Course> courses = Collections.emptyList();

        switch (filterMode) {
            case COURSE_WITHOUT_STUDENT -> {
                courses = courseRepository.filterCourseWithoutStudent();

                if(courses.isEmpty()) break;

                message = String.format("Courses without students: %d", courses.size());
            }

            case COURSE_BY_STUDENT_ID -> {
                courses = courseRepository.filterCoursesByStudent(id);

                if(courses.isEmpty()) break;

                message = String.format("course count %d for student with id : %d", courses.size(), id);
                break;
            }

            default -> {
                status = HttpStatus.NOT_FOUND;
                message = "Not found";
            }
        };

        responseMessage.setPayload(courses);
        responseMessage.setMetadata(meta);
        responseMessage.setMessage(message);

        return ResponseEntity
                .status(status)
                .lastModified(System.currentTimeMillis())
                .body(responseMessage);
    }

    /**
     *
     * path example:
     *      /student/filter?mode=by_course&course_id=5
     *      /student/filter/ and /student/filter?mode=without_course (there are equivalent)
     *
     * acceptable query parameter values:
     *
     * STUDENT_WITHOUT_COURSE("without_course"),
     * STUDENT_PER_COURSE("by_course"),
     *
     * @param mode
     * @param course_id
     * @return
     */

    @GetMapping(
            path = "/student/filter/",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseMessage> filterStudent(@RequestParam(defaultValue = "without_course") String mode,
                                                            @RequestParam(required = false) Long course_id) {
        FilterMode filterMode = FilterMode.valueOf(mode);

        List<Student> payload = new ArrayList<>();
        Map<String, String> meta = new HashMap<>();
        HttpStatus status = HttpStatus.OK;

        ResponseMessage responseMessage = ResponseMessage
                                                    .builder()
                                                    .metadata(meta)
                                                    .build();

        String message = "";

        switch (filterMode) {
            case STUDENT_PER_COURSE -> {
                payload = studentRepository.filterStudentsByCourse(course_id);
                message = String.format("student %id, has %count courses", course_id, payload.size());
            }

            case STUDENT_WITHOUT_COURSE -> {
                payload = studentRepository.filterStudentWithoutCourse();
                message = String.format("there are %d student without courses", payload.size());
            }

            default -> {
                message = "No Students were found";
                status = HttpStatus.NOT_FOUND;
            }
        }

        responseMessage.setMessage(message);
        responseMessage.setPayload(payload);
        meta.put("Status", status.toString());

        return ResponseEntity
                .status(status)
                .lastModified(System.currentTimeMillis())
                .body(responseMessage);
    }

    @PostMapping(
            path = "/course/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseMessage> addCourse(@RequestBody Course course) {
        Course added = courseRepository.saveAndFlush(course);

        Map<String, String> meta = new HashMap<>();

        URI uri = URI.create(String.format("/course/%d", added.getId()));
        meta.put("URI", uri.toString());
        meta.put("Status", HttpStatus.CREATED.toString());

        return ResponseEntity
                .created(uri)
                .lastModified(System.currentTimeMillis())
                .body(
                        ResponseMessage
                            .builder()
                            .payload(added)
                            .metadata(meta)
                            .payload(added)
                            .build()

                );
    }


    @GetMapping(
            path = "/student/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {
        Optional<Student> found = studentRepository.findById(id);
        if(found.isPresent()) return ResponseEntity.ok(found.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(
            path = "/course/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {

        Optional<Course> found = courseRepository.findById(id);
        if (found.isPresent()) return ResponseEntity.ok(found.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping(
            path = "/student",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Student>> getAllStudent() {
        return  ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping(
            path = "/course",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Course>> getAllCourse() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

}
