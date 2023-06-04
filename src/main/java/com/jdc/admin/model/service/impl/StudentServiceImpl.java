package com.jdc.admin.model.service.impl;

import com.jdc.admin.model.dto.StudentDTO;
import com.jdc.admin.model.entity.Course;
import com.jdc.admin.model.entity.Student;
import com.jdc.admin.model.entity.User;
import com.jdc.admin.model.mapper.StudentMapper;
import com.jdc.admin.model.repo.StudentRepo;
import com.jdc.admin.model.service.CourseService;
import com.jdc.admin.model.service.StudentService;
import com.jdc.admin.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Iterator;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final UserService userService;
    private final CourseService courseService;

    private final StudentMapper studentMapper;

    @Override
    public Student loadStudentById(Long studentId) {
        return studentRepo.findById(studentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("student with id [%d] not found", studentId)));
    }

    @Override
    public Page<StudentDTO> loadStudentsByName(String name, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Student> studentsPage = studentRepo.findStudentsByName(name, pageRequest);

        return new PageImpl<>(
                studentsPage.getContent().stream().map(studentMapper::fromStudent).collect(Collectors.toList()),
                pageRequest,
                studentsPage.getTotalElements()
        );
    }

    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        User user = userService.createUser(studentDTO.getUser().getEmail(), studentDTO.getUser().getPassword());
        userService.assignRoleToUser(user.getEmail(), "Student");
        Student student = studentMapper.fromStudentDTO(studentDTO);
        student.setUser(user);
        Student createdStudent = studentRepo.save(student);

        return studentMapper.fromStudent(createdStudent);
    }

    @Override
    public StudentDTO loadStudentByEmail(String email) {
        return studentMapper.fromStudent(studentRepo.findStudentByEmail(email));
    }

    @Override
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        Student loadedStudent = loadStudentById(studentDTO.getStudentId());
        Student student = studentMapper.fromStudentDTO(studentDTO);
        student.setUser(loadedStudent.getUser());
        student.setCourses(loadedStudent.getCourses());
        Student updatedStudent = studentRepo.save(student);

        return studentMapper.fromStudent(updatedStudent);
    }

    @Override
    public void removeStudent(Long studentId) {
        Student student = loadStudentById(studentId);

        Iterator<Course> iterator = student.getCourses().iterator();

        if (iterator.hasNext()) {
            Course course = iterator.next();
            course.removeStudentFromCourse(student);
        }

        studentRepo.deleteById(studentId);
    }
}
