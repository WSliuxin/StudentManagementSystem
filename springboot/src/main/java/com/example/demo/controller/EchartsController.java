package com.example.demo.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/example")
    public Result<?> get() {
        Map<String, Object> map = new HashMap<>();
        map.put("x", CollUtil.newArrayList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        map.put("y",CollUtil.newArrayList(150,230,224,218,147,260,100));
        return Result.success(map);
    }

    @GetMapping("/members")
    public Result<?> members() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        List<User> list = userMapper.selectList(wrapper);

        int q1 = 0; //  系统管理员
        int q2 = 0; //  宿舍管理员
        int q3 = 0; //  用户
        int q4 = 0; //  未定义用户

        for (User user : list) {
            Integer role = user.getRole();
            switch (role) {
                case 1 : q1++; break;
                case 2 : q2++; break;
                case 3 : q3++; break;
                default: q4++; break;
            }
        }

        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
}
