package com.jdc.admin.model.mapper;

import com.jdc.admin.model.dto.StudentDTO;
import com.jdc.admin.model.entity.Student;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class StudentMapper {

    public StudentDTO fromStudent(Student student) {
        StudentDTO dto = new StudentDTO();
        BeanUtils.copyProperties(student, dto);

        return dto;
    }

    public Student fromStudentDTO(StudentDTO dto) {
        Student student = new Student();
        BeanUtils.copyProperties(dto, student);

        return student;
    }

}
