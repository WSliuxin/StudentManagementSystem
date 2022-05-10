package com.example.demo.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.entity.Student;
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
public interface StudentMapper extends BaseMapper<Student> {

    Page<Student> findPage(Page<Student> page, @Param("studentId") String studentId, @Param("name") String name,
                           @Param("sex") String sex,@Param("floor") String floor,@Param("id") String id);
}
