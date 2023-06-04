package com.jdc.admin.model.service.impl;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.entity.Course;
import com.jdc.admin.model.entity.Instructor;
import com.jdc.admin.model.entity.Student;
import com.jdc.admin.model.mapper.CourseMapper;
import com.jdc.admin.model.repo.CourseRepo;
import com.jdc.admin.model.repo.InstructorRepo;
import com.jdc.admin.model.repo.StudentRepo;
import com.jdc.admin.model.service.CourseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;
    private final InstructorRepo instructorRepo;
    private final StudentRepo studentRepo;

    private final CourseMapper courseMapper;

    @Override
    public Course loadCourseById(Long courseId) {
        return courseRepo.findById(courseId).orElseThrow(()
                -> new EntityNotFoundException(String.format("course with id [%d] not found", courseId)));
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        Long instructorId = courseDTO.getInstructor().getInstructorId();

        Course course = courseMapper.fromCourseDTO(courseDTO);

        Instructor instructor = instructorRepo.findById(instructorId).orElseThrow(()
                -> new EntityNotFoundException(String.format("instructor with id [%d] not found", instructorId)));

        course.setInstructor(instructor);

        Course savedCourse = courseRepo.save(course);

        return courseMapper.fromCourse(savedCourse);
    }

    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        Course loadedCourse = loadCourseById(courseDTO.getCourseId());
        Long instructorId = courseDTO.getInstructor().getInstructorId();
        Instructor instructor = instructorRepo.findById(instructorId).orElseThrow(()
                -> new EntityNotFoundException(String.format("instructor with id [%d] not found", instructorId)));

        Course course = courseMapper.fromCourseDTO(courseDTO);
        course.setInstructor(instructor);
        course.setStudents(loadedCourse.getStudents());

        Course updatedCourse = courseRepo.save(course);

        return courseMapper.fromCourse(updatedCourse);
    }

    @Override
    public Page<CourseDTO> findCoursesByCourseName(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursesPage = courseRepo.findCoursesByCourseNameContains(keyword, pageRequest);

        return new PageImpl<>(
                coursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),
                pageRequest,
                coursesPage.getTotalElements()
        );
    }

    @Override
    public void assignStudentToCourse(Long courseId, Long studentId) {

        Student student = studentRepo.findById(studentId).orElseThrow(()
                -> new EntityNotFoundException(String.format("student with id [%d] not found", studentId)));

        Course course = loadCourseById(courseId);

        course.assignStudentToCourse(student);

    }

    @Override
    public Page<CourseDTO> fetchCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> studentCoursesPage = courseRepo.getCoursesByStudentId(studentId, pageRequest);
        return new PageImpl<>(
                studentCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),
                pageRequest,
                studentCoursesPage.getTotalElements()
        );
    }

    @Override
    public Page<CourseDTO> fetchNonEnrolledInCoursesForStudent(Long studentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> nonEnrolledInCoursesPage = courseRepo.getNonEnrolledInCoursesByStudentId(studentId, pageRequest);

        return new PageImpl<>(
                nonEnrolledInCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),
                pageRequest,
                nonEnrolledInCoursesPage.getTotalElements()
        );
    }

    @Override
    public void removeCourse(Long courseId) {
        courseRepo.deleteById(courseId);
    }

    @Override
    public Page<CourseDTO> fetchCoursesForInstructor(Long instructorId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> instructorCoursesPage = courseRepo.getCoursesByInstructorId(instructorId, pageRequest);

        return new PageImpl<>(
                instructorCoursesPage.getContent().stream().map(courseMapper::fromCourse).collect(Collectors.toList()),
                pageRequest,
                instructorCoursesPage.getTotalElements()
        );
    }
}
