package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Leave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxin
 * @since 2022-05-10
 */
@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {

    Page<Leave> findPage(Page<Leave> page, @Param("studentId") String studentId, @Param("name") String name,@Param("floor") String floor,@Param("id") String id,@Param("role") String role);
}
