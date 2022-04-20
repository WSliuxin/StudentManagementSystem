package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.DormitoryMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import com.example.demo.common.Result;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IDormitoryService;
import com.example.demo.entity.Dormitory;

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
@RequestMapping("/dormitory")
public class DormitoryController {

    @Resource
    private IDormitoryService dormitoryService;

    @Resource
    private DormitoryMapper dormitoryMapper;

    @PostMapping
    public Result<?> save(@RequestBody Dormitory dormitory) {
        if (dormitory.getPassword()==null){
            dormitory.setPassword("123456");
        }
        dormitory.setRole("ROLE_TUBES");
        dormitory.setEnable(true);
        // 新增
        dormitoryService.saveOrUpdate(dormitory);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Dormitory dormitory) {
        //更新
        dormitoryService.updateById(dormitory);
        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        dormitoryService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        dormitoryService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Dormitory findOne(@PathVariable Integer id) {
        //根据id查询
        return dormitoryService.getById(id);
    }

    @GetMapping("/page")
    public Page<Dormitory> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize,
                                    @RequestParam(defaultValue = "") String name) {
        // 分页查询
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        return dormitoryService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }

    /**
     * 更新信息接口
     * @param
     * @return
     */
    @PostMapping("/update")
    public Result<?> update2(@RequestBody Dormitory dormitory) {
        dormitoryService.updateById(dormitory);
        return  Result.success();
    }

    @GetMapping("/findAll")
    public Result findAll() {
        return Result.success(dormitoryService.list());
    }


    /**
     * 登录接口
     * @param dormitory
     * @return
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Dormitory dormitory) {
        String nickName = dormitory.getNickName();
        String password = dormitory.getPassword();
        if (StrUtil.isBlank(nickName) || StrUtil.isBlank(password)) {
            return Result.error("-1","用户名或密码错误");
        }

        return dormitoryService.login(dormitory);
    }

    @GetMapping("/username/{id}")
    public Result<?> getName(@PathVariable Integer id) {
        return Result.success(dormitoryMapper.selectById(id));
    }
}

