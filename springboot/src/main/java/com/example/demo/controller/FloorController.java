package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Floor;
import com.example.demo.mapper.FloorMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/floor")
public class FloorController {

    @Resource
    FloorMapper floorMapper;

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
        LambdaQueryWrapper<Floor> wrapper = Wrappers.<Floor>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Floor::getName,search);
        }
        Page<Floor> floorPage = floorMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(floorPage);
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

}
