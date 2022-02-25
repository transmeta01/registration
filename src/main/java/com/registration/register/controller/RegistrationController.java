package com.registration.register.controller;

import com.registration.register.entity.Course;
import com.registration.register.entity.Student;
import com.registration.register.service.CourseService;
import com.registration.register.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/registration")
public class RegistrationController {

    @Autowired
    CourseService courseService;

    @Autowired
    StudentService studentService;

    @PostMapping(
            path = "/student/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        Student addedStudent  = studentService.addStudent(student);

        return ResponseEntity
                .created(URI.create(String.format("/student/%s", addedStudent.getId())))
                .body(addedStudent);
    }

    @PostMapping(
            path = "/student/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> updateStudent(@RequestBody Student student) {
        try {
            studentService.updateStudent(student);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(student);
        }

        return ResponseEntity.accepted().body(student);
    }

    @PutMapping(
            path = "/student/add/{studentId}/{courseId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> addCourseToStudent(@PathVariable Long studentId, Long courseId) {
        if (courseService.getCourse(courseId).isPresent() &&
                studentService.getStudent(studentId).isPresent()) {

            Student student = studentService.getStudent(studentId).get();
            Course course = courseService.getCourse(courseId).get();

            try {
                courseService.updateCourse(course);
                studentService.updateStudent(student);

                return ResponseEntity
                        .accepted()
                        .lastModified(System.currentTimeMillis())
                        .body(null);

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(student);
            }
        } else {
            // TODO include more specific message...I18n ?
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping(
            path = "/student/remove/{studentId}/{courseId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> removeCourseFromStudent(@PathVariable Long studentId, Long courseId) {

        if (courseService.getCourse(courseId).isPresent() &&
                studentService.getStudent(studentId).isPresent()) {

            Student student = studentService.getStudent(studentId).get();
            Course course = courseService.getCourse(courseId).get();

            try {
                studentService.updateStudent(student);
                courseService.updateCourse(course);

                return ResponseEntity
                        .accepted()
                        .lastModified(System.currentTimeMillis())
                        .body(student);

            } catch (Exception e) {
                return ResponseEntity.badRequest().body(student);
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(
            path = "/course/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
        Course added = courseService.addCourse(course);

        return ResponseEntity
                .created(URI.create(String.format("/course/%d", added.getId())))
                .lastModified(System.currentTimeMillis())
                .body(added);
    }

    @PostMapping(
            path = "/course/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Course> updateCourse(@RequestBody Course course) {
        try {
            courseService.updateCourse(course);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(course);
        }

        return ResponseEntity.ok(course);
    }

    @GetMapping(
            path = "/student/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Student> getStudent(@PathVariable Long id) {

        if (studentService.getStudent(id).isPresent())
            return ResponseEntity.ok(studentService.getStudent(id).get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(
            path = "/course/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Course> getCourse(@PathVariable Long id) {

        if (courseService.getCourse(id).isPresent())
            return ResponseEntity.ok(courseService.getCourse(id).get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping(
            path = "/student",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Student>> getAllStudent() {
        return  ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping(
            path = "/course",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Course>> getAllCourse() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

}
