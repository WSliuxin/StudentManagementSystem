package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
 * @since 2022-05-10
 */
@Getter
@Setter
@TableName("sys_leave")
@ApiModel(value = "Leave对象", description = "")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("请假表id")
      @TableId(value = "leave_id", type = IdType.AUTO)
      private Integer leaveId;

      @ApiModelProperty("学号")
      private Integer studentId;

      @ApiModelProperty("开始时间")
      private Date startTime;

      @ApiModelProperty("结束时间")
      private Date endTime;

      @ApiModelProperty("理由")
      private String reason;

      @ApiModelProperty("审批人")
      private Integer approver;

      @ApiModelProperty("状态（-1、请假失败，0、审核中，1、请假中，2、未销假，3、已销假）")
      private Integer state;

      @TableField(exist = false)
      private String floorName;

      @TableField(exist = false)
      private String bedroom;

      @TableField(exist = false)
      private String name;


}
