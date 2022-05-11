package com.example.demo.controller.dto;

import lombok.Data;
import java.util.Date;

@Data
public class LeaveDTO {

    private Integer studentId;

    private Date startTime;

    private Date endTime;

    private String reason;

    private String string;

}
