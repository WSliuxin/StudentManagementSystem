package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Attendance;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.service.IAttendanceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    @Resource
    private AttendanceMapper attendanceMapper;

    @Override
    public Page<Attendance> findPage(Page<Attendance> page, String studentId, String name,String id) {
        return attendanceMapper.findPage(page,studentId,name,id);
    }
}
