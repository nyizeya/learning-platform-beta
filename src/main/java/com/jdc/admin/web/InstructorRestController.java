package com.jdc.admin.web;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.dto.InstructorDTO;
import com.jdc.admin.model.entity.User;
import com.jdc.admin.model.service.CourseService;
import com.jdc.admin.model.service.InstructorService;
import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("instructors")
public class InstructorRestController {

    private final InstructorService instructorService;
    private final UserService userService;
    private final CourseService courseService;

    @GetMapping
    public Page<InstructorDTO> searchInstructors(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        return instructorService.findInstructorsByName(keyword,page, size);

    }

    @GetMapping("all")
    public List<InstructorDTO> findAllInstructors() {
        return instructorService.fetchInstructors();
    }

    @DeleteMapping("{instructorId}")
    public void deleteInstructor(@PathVariable("instructorId") Long instructorId) {
        instructorService.removeInstructor(instructorId);
    }

    @PostMapping
    public InstructorDTO saveInstructor(@RequestBody InstructorDTO instructorDTO) {
        User user = userService.loadUserByEmail(instructorDTO.getUser().getEmail());

        if (null != user) throw new RuntimeException("email already exists");

        return instructorService.createInstructor(instructorDTO);

    }

    @PutMapping("{instructorId}")
    public InstructorDTO updateInstructor(
                @RequestBody InstructorDTO instructorDTO,
                @PathVariable("instructorId") Long instructorId
            ) {

        instructorDTO.setInstructorId(instructorId);

        return instructorService.updateInstructor(instructorDTO);
    }

    @GetMapping("{instructorId}/courses")
    public Page<CourseDTO> coursesByInstructorId(
            @PathVariable Long instructorId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        return courseService.fetchCoursesForInstructor(instructorId, page, size);

    }

    @GetMapping("find")
    public InstructorDTO loadInstructorByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return instructorService.loadInstructorByEmail(email);
    }

}
