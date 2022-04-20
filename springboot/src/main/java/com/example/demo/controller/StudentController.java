package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Dormitory;
import com.example.demo.entity.User;
import com.example.demo.mapper.StudentMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.Result;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IStudentService;
import com.example.demo.entity.Student;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @Resource
    private StudentMapper studentMapper;


    @PostMapping
    public Result<?> save(@RequestBody Student student) {
        // 新增
        if (StrUtil.isBlank(student.getPassword())){
            student.setPassword("123456");
        }
        student.setRole("ROLE_USER");
        studentService.saveOrUpdate(student);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Student student) {
        //更新
        studentService.updateById(student);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        studentService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        studentService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping("/page")
    public Page<Student> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String studentId,
                                  @RequestParam(defaultValue = "") String name) {
        // 分页查询
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        if (StrUtil.isNotBlank(studentId)){
            queryWrapper.like("student_id",studentId);
        }
        return studentService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }


    /**
     * 登录接口
     * @param student
     * @return
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Student student) {
        String studentId = String.valueOf(student.getStudentId());
        String password = student.getPassword();
        if (StrUtil.isBlank(studentId) || StrUtil.isBlank(password)) {
            return Result.error("-1","用户名或密码错误");
        }

        return studentService.login(student);
    }

    @GetMapping("/username/{id}")
    public Result<?> getName(@PathVariable Integer id) {
        return Result.success(studentMapper.selectById(id));
    }

}

