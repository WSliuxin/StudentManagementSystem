package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IRoleService;
import com.example.demo.entity.Role;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-04-14
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;


    @PostMapping
    public Result<?> save(@RequestBody Role role) {
        // 新增
        roleService.saveOrUpdate(role);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Role role) {
        //更新
        roleService.updateById(role);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        roleService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        roleService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Role findOne(@PathVariable Integer id) {
        //根据id查询
        return roleService.getById(id);
    }

    @GetMapping("/page")
    public Page<Role> findPage(@RequestParam String name,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        // 分页查询
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return roleService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }

}

