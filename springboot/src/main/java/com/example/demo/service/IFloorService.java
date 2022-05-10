package com.example.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Floor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 宿舍楼管理表 服务类
 * </p>
 *
 * @author liuxin
 * @since 2022-05-04
 */
public interface IFloorService extends IService<Floor> {

    Page<Floor> findPage(Page<Floor> page, String search);
}
