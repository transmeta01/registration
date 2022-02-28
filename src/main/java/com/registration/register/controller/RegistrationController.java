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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/v1/registration")
public class RegistrationController {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RegistrationRepository registrationRepository;

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

        return ResponseEntity
                .created(URI.create(String.format("/student/%s", addedStudent.getId())))
                .body(responseMessage);
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

        if (studentRepository.findById(studentId).isPresent() &&
                courseRepository.findById(courseId).isPresent()) {

            Student student = studentRepository.findById(studentId).get();
            Course course = courseRepository.findById(courseId).get();

            try {
                // has the student already registered?
                if(student.getCourse().contains(course)) {

                    metadata.put("", HttpStatus.NOT_ACCEPTABLE.toString());

                    ResponseMessage response = ResponseMessage
                            .builder()
                            .metadata(metadata)
                            .build();

                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(response);
                }

                // student can have 50 course and course can have 50 students at most
                if(student.getCourse().size() < 50 && course.getCount() < 50) {
                    int count = course.getCount() + 1;
                    course.setCount(count);

                    Registration registration = Registration
                            .builder()
                            .course(course)
                            .student(student)
                            .lastModified(new Date())
                            .build();

                    log.info("Saving registration", registration.toString());
                    registrationRepository.save(registration);

                    student.getCourse().add(course);
                    int courseCount = student.getCount() + 1;
                    student.setCount(courseCount);
                    student = studentRepository.saveAndFlush(student);

                    ResponseMessage responseMessage = ResponseMessage
                            .builder()
                            .payload(student)
                            .message(String.format("course added to student : %s", student.toString()))
                            .build();

                    return ResponseEntity
                            .accepted()
                            .lastModified(System.currentTimeMillis())
                            .body(responseMessage);
                } else {
                    throw new Exception("quota exceed");
                }
            } catch (Exception e) {
                metadata.put("Error", e.getMessage());
                metadata.put("Status", HttpStatus.BAD_REQUEST.toString());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseMessage
                                .builder()
                                .metadata(metadata)
                                .build()
                );
            }
        }

        // TODO include more specific message...I18n ?
        metadata.put("status", HttpStatus.NOT_FOUND.toString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseMessage
                        .builder()
                        .metadata(metadata)
                        .build()
                );

    }

    @GetMapping(path = "/student/filter/{courseId}")
    public ResponseEntity<ResponseMessage<List<Student>>> filterStudentsPerCourse(@PathVariable Long courseId) {
        List<Student> studentList = studentRepository.filterStudentsByCourse(courseId);

        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .payload(studentList)
                .build();

        return ResponseEntity.ok(responseMessage);
    }

    enum FilterMode {
        WITHOUT_COURSE,
        WITHOUT_STUDENT
    }

    @GetMapping(path = "/course/filter/")
    public ResponseEntity<List<Course>> filterCoursesWithoutStudent(@RequestParam(name = "mode", required = false) String mode) {
//        FilterMode filterMode = FilterMode.WITHOUT_STUDENT;
//        if(mode.equalsIgnoreCase(filterMode.name())) {
            return ResponseEntity.ok(courseRepository.filterCourseWithoutStudent());
//        }
    }

    @GetMapping(path = "/course/filter/{studentId}")
    public ResponseEntity<List<Course>> filterCourseByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(courseRepository.filterCoursesByStudent(studentId));
    }



    @GetMapping(
            path = "/student/filter?mode={filter_value}/"
    )
    public ResponseEntity<List<Student>> filterStudentWithoutCourse(@RequestParam(name = "mode", required = false) String filter) {
        return ResponseEntity.ok(studentRepository.filterStudentWithoutCourse());
    }

    @PostMapping(
            path = "/course/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course added = courseRepository.save(course);

        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .payload(added)
                .build();

        return ResponseEntity
                .created(URI.create(String.format("/course/%d", added.getId())))
                .lastModified(System.currentTimeMillis())
                .body(added);
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
