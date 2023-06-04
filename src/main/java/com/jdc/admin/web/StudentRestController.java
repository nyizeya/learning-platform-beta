package com.jdc.admin.web;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.dto.StudentDTO;
import com.jdc.admin.model.entity.User;
import com.jdc.admin.model.service.CourseService;
import com.jdc.admin.model.service.StudentService;
import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("students")
public class StudentRestController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final UserService userService;

    @GetMapping
    public Page<StudentDTO> searchStudents(
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        return studentService.loadStudentsByName(keyword, page, size);

    }

    @DeleteMapping("{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.removeStudent(studentId);
    }

    @PostMapping
    public StudentDTO saveStudent(@RequestBody  StudentDTO studentDTO) {
        User user = userService.loadUserByEmail(studentDTO.getUser().getEmail());

        if (null != user) throw new RuntimeException("email already exists");

        return studentService.createStudent(studentDTO);
    }

    @PutMapping("{studentId}")
    public StudentDTO updateStudent(
            @RequestBody StudentDTO studentDTO,
            @PathVariable Long studentId
    ) {

        studentDTO.setStudentId(studentId);
        return studentService.updateStudent(studentDTO);

    }

    @GetMapping("{studentId}/courses")
    public Page<CourseDTO> coursesByStudentId(
            @PathVariable Long studentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        return courseService.fetchCoursesForStudent(studentId, page, size);

    }

    @GetMapping("{studentId}/other-courses")
    public Page<CourseDTO> nonSubscribedCoursesByStudentId(
            @PathVariable Long studentId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {

        return courseService.fetchNonEnrolledInCoursesForStudent(studentId, page, size);

    }

    @GetMapping("find")
    public StudentDTO loadStudentByEmail(@RequestParam(name = "email", defaultValue = "") String email) {
        return studentService.loadStudentByEmail(email);
    }

}
