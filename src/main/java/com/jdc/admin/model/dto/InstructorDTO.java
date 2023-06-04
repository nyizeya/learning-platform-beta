package com.jdc.admin.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstructorDTO {
    private Long instructorId;
    private String firstName;
    private String lastName;
    private String summary;
    private UserDTO user;
}
