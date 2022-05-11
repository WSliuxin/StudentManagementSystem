package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Leave;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Student;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxin
 * @since 2022-05-10
 */
public interface ILeaveService extends IService<Leave> {

    Page<Leave> findPage(Page<Leave> page, String studentId, String name, String floor, String id,String role);
}
