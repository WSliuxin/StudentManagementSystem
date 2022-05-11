package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Leave;
import com.example.demo.mapper.LeaveMapper;
import com.example.demo.service.ILeaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxin
 * @since 2022-05-10
 */
@Service
public class LeaveServiceImpl extends ServiceImpl<LeaveMapper, Leave> implements ILeaveService {

    @Resource
    LeaveMapper leaveMapper;

    @Override
    public Page<Leave> findPage(Page<Leave> page, String studentId, String name, String floor, String id,String role) {
        return leaveMapper.findPage(page,studentId,name,floor,id,role);
    }
}
