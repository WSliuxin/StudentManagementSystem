package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Announcement;
import com.example.demo.entity.Floor;
import com.example.demo.mapper.AnnouncementMapper;
import com.example.demo.mapper.FloorMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    @Resource
    AnnouncementMapper announcementMapper;

    @PostMapping
    public Result<?> save(@RequestBody Announcement announcement) {
        announcement.setTime(new Date());
        announcementMapper.insert(announcement);
        return  Result.success();
    }

    @PutMapping
    public Result<?> updata(@RequestBody Announcement announcement) {
        announcementMapper.updateById(announcement);
        return  Result.success();
    }

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search)
    {
        LambdaQueryWrapper<Announcement> wrapper = Wrappers.<Announcement>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Announcement::getAuthor,search);
        }
        Page<Announcement> announcementPage = announcementMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(announcementPage);
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) { return Result.success(announcementMapper.selectById(id)); }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        announcementMapper.deleteById(id);
        return  Result.success();
    }

}
