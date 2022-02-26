package com.registration.register.service;

import com.registration.register.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    Course addCourse(Course course);
    void removeCourse(Long id);
    Optional<Course> updateCourse(Course service) throws Exception;

    List<Course> getAllCourses();

    Optional<Course> getCourse(Long id);
}
