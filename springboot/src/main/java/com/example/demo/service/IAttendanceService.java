package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Attendance;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
public interface IAttendanceService extends IService<Attendance> {

    Page<Attendance> findPage(Page<Attendance> page, String studentId, String name,String id);
}
