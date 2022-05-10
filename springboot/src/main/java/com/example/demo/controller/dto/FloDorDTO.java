package com.example.demo.controller.dto;

import com.example.demo.entity.Menu;
import lombok.Data;

import java.util.List;

@Data
public class FloDorDTO {
    private Integer id;

    private String nickName;

    private String name;

    private String sex;

    private String phone;

    private String floor;

    private Boolean enable;

    private List<String> floors;

}
