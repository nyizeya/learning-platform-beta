package com.jdc.admin.model.repo;

import com.jdc.admin.model.entity.Course;
import com.jdc.admin.model.entity.Instructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InstructorRepo extends JpaRepository<Instructor, Long> {

    @Query("SELECT i FROM Instructor AS i WHERE i.firstName LIKE %:name% OR i.lastName LIKE %:name% ")
    Page<Instructor> findInstructorsByName(@Param("name") String name, PageRequest pageRequest);

    @Query("SELECT i FROM Instructor i WHERE i.user.email = :email")
    Instructor findInstructorByEmail(@Param("email") String email);

    @Query(
            nativeQuery = true,
            value = "select * from courses c left join instructors i on c.instructor_id = i.instructor_id where i.instructor_id = :instructorId"
    )
    List<Course> getCoursesByInstructorId(@Param("instructorId") Long id);

}
