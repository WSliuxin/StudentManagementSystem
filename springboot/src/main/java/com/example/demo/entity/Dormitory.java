package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName("sys_dormitory")
@ApiModel(value = "Dormitory对象", description = "")
public class Dormitory implements Serializable {

      private static final long serialVersionUID = 1L;

      @ApiModelProperty("ID")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("用户名")
      private String nickName;

      @ApiModelProperty("密码")
      private String password;

      @ApiModelProperty("姓名")
      private String name;

      @ApiModelProperty("性别")
      private String sex;

      @ApiModelProperty("电话")
      private String phone;

      @ApiModelProperty("宿舍楼")
      private String floor;

      @ApiModelProperty("是否禁用")
      private Boolean enable;

      @ApiModelProperty("角色")
      private String role;


}
