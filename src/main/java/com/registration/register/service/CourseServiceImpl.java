package com.registration.register.service;

import com.registration.register.entity.Course;
import com.registration.register.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    private CourseRepository repository;

    @Autowired
    public CourseServiceImpl(CourseRepository repository) {
        this.repository = repository;
    }

    @Override
    public Course addCourse(Course course) {
        log.info("saving ", course);
        return repository.save(course);
    }

    @Override
    public void removeCourse(Long id) {
        log.info("deleting course id: ", id);
        Optional<Course> course = repository.findById(id);
        if(course.isPresent()) repository.delete(course.get());

        // TODO should log event
    }

    @Override
    public Optional<Course> updateCourse(Course course) throws Exception {
        Optional<Course> found = repository.findById(course.getId());
        if(found.isPresent()) {
            repository.delete(found.get());
            course.setLastModified(new Date());
            repository.save(course);
        }

        return Optional.empty();
    }

    @Override
    public List<Course> getAllCourses() {
        return repository.findAll();
    }

    @Override
    public Optional<Course> getCourse(Long id) {
        return repository.findById(id);
    }
}
