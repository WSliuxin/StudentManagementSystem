package com.example.demo.controller.dto;

import com.example.demo.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class StuDTO {

    private Integer id;

    private String token;

    private Integer studentId;

    private String password;

    private String name;

    private String sex;

    private String phone;

    private String role;

    private String floorName;

    private Integer bedroom;

    private List<Menu> menus;
}
