package com.example.demo.service;

import com.example.demo.common.Result;
import com.example.demo.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
public interface IStudentService extends IService<Student> {

    Result<?> login(Student student);
}
