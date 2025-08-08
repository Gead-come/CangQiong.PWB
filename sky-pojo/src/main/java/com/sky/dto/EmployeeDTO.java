package com.sky.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class EmployeeDTO implements Serializable {

    private Long id;

    private String idNumber;

    private String username;

    private String name;

    private String phone;

    private String sex;



}
