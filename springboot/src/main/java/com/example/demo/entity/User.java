package com.example.demo.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@TableName("user")
@Data
@ToString
public class User {

    @TableId(type = IdType.AUTO)
    @Alias("ID")
    private Integer id;

    @Alias("学号")
    private Integer studentId;

    @Alias("姓名")
    private String username;

    @Alias("密码")
    private String password;


    @Alias("性别")
    private String sex;

    @Alias("地址")
    private String address;

    @Alias("用户级别")
    private String role;


}
