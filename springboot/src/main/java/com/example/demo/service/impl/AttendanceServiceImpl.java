package com.example.demo.service.impl;

import com.example.demo.entity.Attendance;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.service.IAttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@Service
public class AttendanceServiceImpl extends ServiceImpl<AttendanceMapper, Attendance> implements IAttendanceService {

}
