package com.jdc.admin.model.service.impl;

import com.jdc.admin.model.dto.InstructorDTO;
import com.jdc.admin.model.entity.Course;
import com.jdc.admin.model.entity.Instructor;
import com.jdc.admin.model.entity.User;
import com.jdc.admin.model.mapper.InstructorMapper;
import com.jdc.admin.model.repo.InstructorRepo;
import com.jdc.admin.model.service.CourseService;
import com.jdc.admin.model.service.InstructorService;
import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepo instructorRepo;
    private final UserService userService;
    private final CourseService courseService;

    private final InstructorMapper instructorMapper;

    @Override
    public Instructor loadInstructorById(Long instructorId) {
        return instructorRepo.findById(instructorId).orElseThrow(()
                -> new EntityNotFoundException(String.format("instructor with id [%d] not found", instructorId)));
    }

    @Override
    public Page<InstructorDTO> findInstructorsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Instructor> instructorsPage = instructorRepo.findInstructorsByName(name, pageRequest);
        return new PageImpl<>(
                instructorsPage.getContent().stream().map(instructorMapper::fromInstructor).collect(Collectors.toList()),
                pageRequest,
                instructorsPage.getTotalElements()
        );
    }

    @Override
    public InstructorDTO loadInstructorByEmail(String email) {
        Instructor instructor = instructorRepo.findInstructorByEmail(email);
        return instructorMapper.fromInstructor(instructor);
    }

    @Override
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        User user = userService.createUser(instructorDTO.getUser().getEmail(), instructorDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Instructor");
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(user);
        Instructor savedInstructor = instructorRepo.save(instructor);

        return instructorMapper.fromInstructor(savedInstructor);
    }

    @Override
    public InstructorDTO updateInstructor(InstructorDTO instructorDTO) {
        Instructor loadedInstructor = loadInstructorById(instructorDTO.getInstructorId());
        Instructor instructor = instructorMapper.fromInstructorDTO(instructorDTO);
        instructor.setUser(loadedInstructor.getUser());
        instructor.setCourses(loadedInstructor.getCourses());
        Instructor updatedInstructor = instructorRepo.save(instructor);

        return instructorMapper.fromInstructor(updatedInstructor);
    }

    @Override
    public List<InstructorDTO> fetchInstructors() {
        return instructorRepo.findAll().stream().map(instructorMapper::fromInstructor).collect(Collectors.toList());
    }

    @Override
    public void removeInstructor(Long instructorId) {
        Instructor instructor = loadInstructorById(instructorId);

        for (Course course : instructor.getCourses()) {
            courseService.removeCourse(course.getCourseId());
        }

        instructorRepo.deleteById(instructorId);
    }
}
