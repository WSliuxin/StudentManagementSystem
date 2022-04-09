package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("floor")
@Data
public class Floor {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String synopsis;
    private String administrators;
}
