package com.jdc.admin.web;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping
    public Page<CourseDTO> searchCourses(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
        ) {

        return courseService.findCoursesByCourseName(keyword, page, size);

    }

    @DeleteMapping("{courseId}")
    public void deleteCourse(@PathVariable("courseId") Long courseId) {
        courseService.removeCourse(courseId);
    }

    @PostMapping
    public CourseDTO saveCourse(
            @RequestBody CourseDTO courseDTO
    ) {
        return courseService.createCourse(courseDTO);
    }

    @PutMapping("{courseId}")
    public CourseDTO updateCourse(
            @PathVariable("courseId") Long courseId,
            @RequestBody CourseDTO courseDTO
    ) {
        courseDTO.setCourseId(courseId);
        return courseService.updateCourse(courseDTO);
    }

    @PostMapping("{courseId}/enrollment/students/{studentId}")
    public void enrollStudentInCourse(@PathVariable Long courseId, @PathVariable Long studentId) {

        courseService.assignStudentToCourse(courseId, studentId);

    }

}
