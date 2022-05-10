package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.mapper.FloorMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.example.demo.common.Result;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IFloorService;
import com.example.demo.entity.Floor;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 宿舍楼管理表 前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-05-04
 */
@RestController
@RequestMapping("/floor")
public class FloorController {

    @Resource
    FloorMapper floorMapper;

    @Resource
    IFloorService iFloorService;

    @PostMapping
    public Result<?> save(@RequestBody Floor floor) {
        floorMapper.insert(floor);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Floor floor) {
        floorMapper.updateById(floor);
        return  Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    {

        Page<Floor> page = iFloorService.findPage(new Page<>(pageNum,pageSize),search);
        return Result.success(page);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        floorMapper.deleteById(id);
        return  Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll() {
        QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
        return Result.success(floorMapper.selectList(queryWrapper));
    }

    @GetMapping("/getId/{id}")
    public Result<?> findId(@PathVariable String id) {
        Floor floor = floorMapper.selectById(id);
        System.out.println(floor.getFloorName());
        return Result.success(floor);
    }
}

