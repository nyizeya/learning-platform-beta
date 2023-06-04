package com.jdc.admin.model.service;

import com.jdc.admin.model.dto.InstructorDTO;
import com.jdc.admin.model.entity.Instructor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InstructorService {

    Instructor loadInstructorById(Long instructorId);

    Page<InstructorDTO> findInstructorsByName(String name, int page, int size);

    InstructorDTO loadInstructorByEmail(String email);

    InstructorDTO createInstructor(InstructorDTO instructorDTO);

    InstructorDTO updateInstructor(InstructorDTO instructorDTO);

    List<InstructorDTO> fetchInstructors();

    void removeInstructor(Long instructorId);

}
