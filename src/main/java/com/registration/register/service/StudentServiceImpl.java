package com.registration.register.service;

import com.registration.register.entity.Student;
import com.registration.register.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository repository;

    @Override
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    @Override
    public void updateStudent(Student student) throws Exception {
        Optional<Student> found = repository.findById(student.id);
        if(found.isPresent()) {
            repository.delete(found.get());
            repository.save(student);
        }
    }

    @Override
    public void removeStudent(Long id) {
        Optional<Student> found = repository.findById(id);
        if(found.isPresent()) repository.delete(found.get());
    }

    @Override
    public List<Student> getAllStudent() {
        return repository.findAll();
    }

    @Override
    public List<Student> filterStudentWithoutCourse() {
        return repository.filterStudentWithoutCourse();
    }

    @Override
    public Optional<Student> getStudent(Long id) {
        return repository.findById(id);
    }
}
