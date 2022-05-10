package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 宿舍楼管理表
 * </p>
 *
 * @author liuxin
 * @since 2022-05-04
 */
@Getter
@Setter
@ApiModel(value = "Floor对象", description = "宿舍楼管理表")
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("编号")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("名称")
      private String floorName;

      @ApiModelProperty("简介")
      private String synopsis;

      @ApiModelProperty("管理员id")
      private Integer administrators;

      @TableField(exist = false)
      private String adminName;


}
