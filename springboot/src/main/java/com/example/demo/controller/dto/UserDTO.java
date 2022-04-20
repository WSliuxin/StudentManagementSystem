package com.example.demo.controller.dto;

import cn.hutool.core.annotation.Alias;
import com.example.demo.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Integer id;

    private String token;

    @Alias("姓名")
    private String username;

    @Alias("学号")
    private Integer studentId;

    @Alias("性别")
    private String sex;

    @Alias("地址")
    private String address;

    @Alias("用户级别")
    private String role;

    private List<Menu> menus;
}
