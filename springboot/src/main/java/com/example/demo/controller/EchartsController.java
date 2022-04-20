package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.Dormitory;
import com.example.demo.entity.Student;
import com.example.demo.entity.User;
import com.example.demo.mapper.DormitoryMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private DormitoryMapper dormitoryMapper;

    @Resource
    private StudentMapper studentMapper;

    @GetMapping("/members")
    public Result<?> members() {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();

        List<Dormitory> list1 = dormitoryMapper.selectList(queryWrapper);

        QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();

        List<Student> list2 = studentMapper.selectList(queryWrapper2);

        int q1 = list1.size(); //  宿舍管理员
        int q2 = list2.size(); //  学生


        return Result.success(CollUtil.newArrayList(q1,q2));
    }
}
