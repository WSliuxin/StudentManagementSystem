package com.example.demo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Role;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.example.demo.common.Result;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IMenuService;
import com.example.demo.entity.Menu;

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
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;


    @PostMapping
    public Result<?> save(@RequestBody Menu menu) {
        // 新增或者更新
        menuService.saveOrUpdate(menu);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        menuService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        menuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Menu findOne(@PathVariable Integer id) {
        //根据id查询
        return menuService.getById(id);
    }

    @PutMapping
    public Result<?> update(@RequestBody Menu menu) {
        //更新
        menuService.updateById(menu);
        return  Result.success();
    }

    @GetMapping("/page")
    public Page<Menu> findPage(@RequestParam String name,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        // 分页查询
        QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name",name);
        queryWrapper.orderByDesc("id");
        return menuService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }

}

