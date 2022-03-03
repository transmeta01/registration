package com.registration.register.repository;

import com.registration.register.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query(value = "SELECT * FROM STUDENT s JOIN REGISTRATION r ON s.id = r.student_id  WHERE r.course_id = :id)",
            nativeQuery = true)
    List<Student> filterStudentsByCourse(@Param("id") Long id);

    @Query(value = "SELECT s FROM Student s WHERE s.count = 0")
    List<Student> filterStudentWithoutCourse();
}
