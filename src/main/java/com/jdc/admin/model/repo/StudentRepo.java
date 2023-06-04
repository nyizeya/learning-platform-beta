package com.jdc.admin.model.repo;

import com.jdc.admin.model.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.firstName LIKE %:name% OR s.lastName LIKE %:name%")
    Page<Student> findStudentsByName(@Param("name") String name, PageRequest pageRequest);

    @Query("SELECT s FROM Student s WHERE s.user.email = :email")
    Student findStudentByEmail(@Param("email") String email);
}
