package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Dormitory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@Mapper
public interface DormitoryMapper extends BaseMapper<Dormitory> {

    Page<Dormitory> findPage(Page<Dormitory> page, @Param("name") String name);
}
