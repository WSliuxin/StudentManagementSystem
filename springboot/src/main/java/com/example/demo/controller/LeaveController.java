package com.example.demo.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.controller.dto.LeaveDTO;
import com.example.demo.mapper.DormitoryMapper;
import com.example.demo.mapper.FloorMapper;
import com.example.demo.mapper.LeaveMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.example.demo.common.Result;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.ILeaveService;
import com.example.demo.entity.Leave;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-05-10
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Resource
    private ILeaveService leaveService;

    @Resource
    private LeaveMapper leaveMapper;

    @Resource
    private DormitoryMapper dormitoryMapper;

    @Resource
    private FloorMapper floorMapper;


    @PutMapping("/update")
    public Result<?> update(@RequestBody LeaveDTO leaveDTO) {

        QueryWrapper<Leave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",leaveDTO.getStudentId());
        List<Leave> list = leaveMapper.selectList(queryWrapper);

        for (Leave leave : list) {
            if (leave.getState() == 0) {
                return Result.error("-1","已经有一条请假在审核中，请勿重复添加");
            }

        }

        Leave leave = new Leave();
        BeanUtil.copyProperties(leaveDTO,leave,true);

        String string = leaveDTO.getString();
        String approvedId = string.substring(0, string.indexOf("_"));

        leave.setApprover(Integer.valueOf(approvedId));
        leave.setState(0);

        leaveService.save(leave);

        return  Result.success();
    }

    @GetMapping("/page")
    public Page<Leave> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String studentId,
                                @RequestParam(defaultValue = "") String name,
                                @RequestParam(defaultValue = "") String floor,
                                @RequestParam(defaultValue = "") String id,
                                @RequestParam(defaultValue = "ROLE_ADMIN") String role) {


        return leaveService.findPage(new Page<>(pageNum, pageSize), studentId, name, floor,id,role);
    }

    @DeleteMapping("/del/{id}")
    public Result<?> delete(@PathVariable Long id){
        try {
            leaveMapper.deleteById(id);
        }catch (Exception e) {
            return Result.error("-1","撤销失败");
        }

        return Result.success();
    }
    @PutMapping("/update/{id}")
    private  Result<?> update(@PathVariable Long id) {
        try {
            Leave leave = leaveMapper.selectById(id);
            leave.setState(3);
            leaveMapper.updateById(leave);
        }catch (Exception e) {
            return Result.error("-1","销假失败");
        }

        return Result.success();
    }

}

