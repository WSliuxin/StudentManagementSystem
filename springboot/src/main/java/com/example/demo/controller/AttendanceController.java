package com.example.demo.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Student;
import com.example.demo.service.IStudentService;
import com.sun.org.apache.bcel.internal.generic.DDIV;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.example.demo.common.Result;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IAttendanceService;
import com.example.demo.entity.Attendance;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Resource
    private IAttendanceService attendanceService;

    @Resource
    private IStudentService studentService;

    @PostMapping
    public Result<?> save(@RequestBody Attendance attendance) {
        // 新增
        Integer student = Integer.valueOf(attendance.getStudentId());
        List<Student> list = studentService.list();
        for (Student students : list) {
            Integer studentId = students.getStudentId();
            if (Objects.equals(studentId, student)){
                attendance.setFloor(students.getFloor());
                attendance.setName(students.getName());
                attendance.setBedroom(students.getBedroom().toString());
            }
        }
        attendance.setDate(attendance.getDate());
        attendanceService.saveOrUpdate(attendance);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Attendance attendance) {
        //更新
        attendanceService.updateById(attendance);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        attendanceService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        attendanceService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Attendance findOne(@PathVariable Integer id) {
        //根据id查询
        return attendanceService.getById(id);
    }

    @GetMapping("/page")
    public Page<Attendance> findPage(@RequestParam Integer pageNum,
                                     @RequestParam Integer pageSize,
                                     @RequestParam(defaultValue = "") String studentId,
                                     @RequestParam(defaultValue = "") String name) {
        // 分页查询
        QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        if (StrUtil.isNotBlank(studentId)){
            queryWrapper.like("student_id",studentId);
        }
        return attendanceService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }

}

