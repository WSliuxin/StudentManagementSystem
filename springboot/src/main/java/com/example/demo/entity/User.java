package com.example.demo.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@TableName("user")
@Data
@ToString
public class User {

    @TableId(type = IdType.AUTO)
    @Alias("ID")
    private Integer id;

    @Alias("姓名")
    private String username;

    @Alias("密码")
    private String password;

    @Alias("昵称")
    private String nickName;

    @Alias("年龄")
    private Integer age;

    @Alias("性别")
    private String sex;

    @Alias("地址")
    private String address;

    @Alias("用户头像地址")
    private String cover;

    @Alias("用户级别")
    private Integer role;

}
