package com.example.demo.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@Getter
@Setter
@TableName("sys_student")
@ApiModel(value = "Student对象", description = "")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    @Alias("ID")
    private Integer id;

    @ApiModelProperty("学号")
    @Alias("学号")
    private Integer studentId;

    @ApiModelProperty("密码")
    @Alias("密码")
    private String password;

    @ApiModelProperty("姓名")
    @Alias("姓名")
    private String name;

    @ApiModelProperty("性别")
    @Alias("性别")
    private String sex;

    @ApiModelProperty("宿舍楼")
    @Alias("宿舍楼")
    private String floor;

    @ApiModelProperty("寝室")
    @Alias("寝室号")
    private Integer bedroom;

    @ApiModelProperty("电话")
    @Alias("电话")
    private String phone;

    @ApiModelProperty("角色")
    @Alias("角色")
    private String role;

    @ApiModelProperty("宿舍楼id")
    @Alias("宿舍楼ID")
    private Integer floorId;

    @TableField(exist = false)
    private String floorName;
}
