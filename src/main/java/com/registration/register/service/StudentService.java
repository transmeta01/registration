package com.registration.register.service;

import com.registration.register.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student addStudent(Student student);

    void updateStudent(Student student) throws Exception;

    void removeStudent(Long id);

    List<Student> getAllStudent();

    List<Student> filterStudentWithoutCourse();

    Optional<Student> getStudent(Long studentId);
}
