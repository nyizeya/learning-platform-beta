package com.jdc.admin.model.mapper;

import com.jdc.admin.model.dto.InstructorDTO;
import com.jdc.admin.model.entity.Instructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class InstructorMapper {

    public InstructorDTO fromInstructor(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        BeanUtils.copyProperties(instructor, dto);

        return dto;
    }

    public Instructor fromInstructorDTO(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        BeanUtils.copyProperties(dto, instructor);

        return instructor;
    }

}
