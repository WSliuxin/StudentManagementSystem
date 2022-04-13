package com.example.demo.controller.dto;

import cn.hutool.core.annotation.Alias;
import lombok.Data;

@Data
public class UserDTO {

    private Integer id;

    private String username;

    private String nickname;

    private String cover;

    private Integer role;;

    private String token;

    private Integer age;

    private String sex;

    private String address;
}
