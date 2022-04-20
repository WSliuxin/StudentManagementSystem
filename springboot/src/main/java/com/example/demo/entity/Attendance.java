package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

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
@ApiModel(value = "Attendance对象", description = "")
public class Attendance implements Serializable {

      private static final long serialVersionUID = 1L;

      @ApiModelProperty("ID")
      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("学号")
      private String studentId;

      @ApiModelProperty("姓名")
      private String name;

      @ApiModelProperty("宿舍楼")
      private String floor;

      @ApiModelProperty("寝室号")
      private String bedroom;

      @ApiModelProperty("备注")
      private String content;

      @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT-8")
      @ApiModelProperty("日期")
      private Date date;


}
