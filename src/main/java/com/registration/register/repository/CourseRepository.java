package com.registration.register.repository;

import com.registration.register.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT * FROM COURSE WHERE id IN (SELECT course_id FROM REGISTRATION WHERE student_id = :id)",
            nativeQuery = true)
    List<Course> filterCoursesByStudent(@Param("id") Long id);

    @Query(value = "SELECT c FROM Course c WHERE c.count = 0")
    List<Course> filterCourseWithoutStudent();
}
