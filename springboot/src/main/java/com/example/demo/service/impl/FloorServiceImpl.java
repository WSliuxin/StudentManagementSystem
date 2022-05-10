package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Floor;
import com.example.demo.mapper.FloorMapper;
import com.example.demo.service.IFloorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 宿舍楼管理表 服务实现类
 * </p>
 *
 * @author liuxin
 * @since 2022-05-04
 */
@Service
public class FloorServiceImpl extends ServiceImpl<FloorMapper, Floor> implements IFloorService {

    @Resource
    FloorMapper floorMapper;

    @Override
    public Page<Floor> findPage(Page<Floor> page, @Param("name") String search) {
        return floorMapper.findPage(page,search);
    }
}
