package com.jdc.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Long courseId;
    private String courseName;
    private String courseDuration;
    private String courseDescription;
    private InstructorDTO instructor;

}
