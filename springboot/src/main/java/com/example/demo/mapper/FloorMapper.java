package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Floor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 宿舍楼管理表 Mapper 接口
 * </p>
 *
 * @author liuxin
 * @since 2022-05-04
 */
@Mapper
public interface FloorMapper extends BaseMapper<Floor> {

    Page<Floor> findPage(Page<Floor> page, String search);
}
