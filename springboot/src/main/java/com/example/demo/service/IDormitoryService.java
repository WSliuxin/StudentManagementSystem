package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Dormitory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
public interface IDormitoryService extends IService<Dormitory> {

    Result<?> login(Dormitory dormitory);

    Page<Dormitory> findPage(Page<Dormitory> page, String name);
}
