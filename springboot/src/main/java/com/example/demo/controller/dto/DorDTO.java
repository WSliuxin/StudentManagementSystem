package com.example.demo.controller.dto;

import com.example.demo.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class DorDTO {

    private Integer id;

    private String token;

    private String nickName;

    private String password;

    private String name;

    private String sex;

    private String phone;

    private String floor;

    private Boolean enable;

    private String role;

    private List<Menu> menus;
}
