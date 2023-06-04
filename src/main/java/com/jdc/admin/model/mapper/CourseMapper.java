package com.jdc.admin.model.mapper;

import com.jdc.admin.model.dto.CourseDTO;
import com.jdc.admin.model.entity.Course;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseMapper {

    private final InstructorMapper instructorMapper;

    public CourseDTO fromCourse(Course course) {
        CourseDTO dto = new CourseDTO();
        BeanUtils.copyProperties(course, dto);
        dto.setInstructor(instructorMapper.fromInstructor(course.getInstructor()));

        return dto;
    }

    public Course fromCourseDTO(CourseDTO dto) {
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);

        return course;
    }

}
