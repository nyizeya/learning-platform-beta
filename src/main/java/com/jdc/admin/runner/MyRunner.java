package com.jdc.admin.runner;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.dto.InstructorDTO;
import com.jdc.admin.model.dto.StudentDTO;
import com.jdc.admin.model.dto.UserDTO;
import com.jdc.admin.model.service.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class MyRunner implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final StudentService studentService;

    @Override
    public void run(String... args) throws Exception {
        createRoles();
        createAdmin();
        createInstructors();
        createCourses();
        StudentDTO studentDTO = createStudent();
        assignCourseToStudent(studentDTO);
        createStudents();
    }

    private void createAdmin() {
        userService.createUser("admin@gmail.com", "1234");
        userService.assignRoleToUser("admin@gmail.com", "Admin");
    }

    private void createRoles() {
        Arrays.asList("Admin", "Instructor", "Student").forEach(roleService::createRole);
    }

    private void createInstructors() {
        for (int i = 1; i < 11; i++) {
            InstructorDTO dto = new InstructorDTO();
            dto.setFirstName("instructor" + i + "FN");
            dto.setLastName("instructor" + i + "LN");
            dto.setSummary("master" + i);

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("instructor" + i + "@gmail.com");
            userDTO.setPassword("1234");
            dto.setUser(userDTO);

            instructorService.createInstructor(dto);
        }
    }

    private void createCourses() {
        for (int i = 1; i < 11; i++) {
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.setCourseName("Java Course " + i);
            courseDTO.setCourseDuration(i + " Hours");
            courseDTO.setCourseDescription("Java" + i);

            InstructorDTO instructorDTO = new InstructorDTO();
            instructorDTO.setInstructorId(1L);
            courseDTO.setInstructor(instructorDTO);

            courseService.createCourse(courseDTO);
        }
    }

    private void createStudents() {
        for (int i = 1; i < 11; i++) {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setFirstName("studentFN");
            studentDTO.setLastName("studentLN");
            studentDTO.setLevel("Intermediate");

            UserDTO userDTO = new UserDTO();
            userDTO.setEmail("student" + i + "@gmail.com");
            userDTO.setPassword("1234");

            studentDTO.setUser(userDTO);

            studentService.createStudent(studentDTO);
        }
    }

    private StudentDTO createStudent() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setFirstName("studentFN");
        studentDTO.setLastName("studentLN");
        studentDTO.setLevel("Intermediate");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("student@gmail.com");
        userDTO.setPassword("1234");

        studentDTO.setUser(userDTO);

        return studentService.createStudent(studentDTO);
    }

    private void assignCourseToStudent(StudentDTO studentDTO) {
        courseService.assignStudentToCourse(1L, studentDTO.getStudentId());
    }

}
