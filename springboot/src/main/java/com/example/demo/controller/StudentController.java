package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.controller.dto.Individual;
import com.example.demo.entity.*;
import com.example.demo.mapper.AttendanceMapper;
import com.example.demo.mapper.FloorMapper;
import com.example.demo.mapper.StudentMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.common.Result;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import com.example.demo.service.IStudentService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private IStudentService studentService;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private FloorMapper floorMapper;

    @Resource
    private AttendanceMapper attendanceMapper;


    @PostMapping
    public Result<?> save(@RequestBody Student student) {

        String floor = student.getFloor();
        if (StrUtil.isNotBlank(floor)) {
            String floorId = floor.substring(0, floor.indexOf("_"));
            student.setFloorId(Integer.valueOf(floorId));
            student.setFloor(floor.substring(floorId.length()+1));
        }
        // 新增
        if (StrUtil.isBlank(student.getPassword())){
            student.setPassword("123456");
        }
        student.setRole("ROLE_USER");
        studentService.saveOrUpdate(student);
        return  Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Student student) {
        //更新
        String floor = student.getFloor();
        if (StrUtil.isNotBlank(floor)) {
            String floorId = floor.substring(0, floor.indexOf("_"));
            System.out.println(floorId);
            student.setFloorId(Integer.valueOf(floorId));
            student.setFloor(floor.substring(floorId.length()+1));

            QueryWrapper<Attendance> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("student_id",student.getStudentId());
            List<Attendance> list = attendanceMapper.selectList(queryWrapper);
            for (Attendance item : list) {
                item.setFloorId(student.getFloorId());
                item.setFloor(student.getFloorName());
                attendanceMapper.updateById(item);
            }
        }
        studentService.updateById(student);


        return  Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        // 删除
        studentService.removeById(id);
        return  Result.success();
    }

    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        //批量删除
        studentService.removeByIds(ids);
        return Result.success();
    }


    @GetMapping("/page")
    public Page<Student> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String studentId,
                                  @RequestParam(defaultValue = "") String name,
                                  @RequestParam(defaultValue = "") String sex,
                                  @RequestParam(defaultValue = "") String floor,
                                  @RequestParam(defaultValue = "") String id) {

        // 分页查询
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();

        if (StrUtil.isNotBlank(id)){
            QueryWrapper<Floor> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("administrators",id);
            List<Floor> floors = floorMapper.selectList(queryWrapper2);
            queryWrapper.and(wrapper -> {
                for (Floor item : floors) {
                    String floorName = item.getFloorName();
                    wrapper.like("floor",floorName).or();
                }
            });
        }

        if (StrUtil.isNotBlank(name)){
            queryWrapper.like("name",name);
        }
        if (StrUtil.isNotBlank(studentId)){
            queryWrapper.like("student_id",studentId);
        }
        if (StrUtil.isNotBlank(sex)){
            queryWrapper.like("sex",sex);
        }
        if (StrUtil.isNotBlank(floor)){
            queryWrapper.like("floor",floor);
        }

        Page<Student> page = studentService.page(new Page<>(pageNum, pageSize), queryWrapper);

        return studentService.findPage(new Page<>(pageNum, pageSize), studentId, name, sex, floor,id);
    }


    /**
     * 登录接口
     * @param student
     * @return
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody Student student) {
        String studentId = String.valueOf(student.getStudentId());
        String password = student.getPassword();

        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",studentId);
        Student student1 = studentMapper.selectOne(queryWrapper);
        if (student1.getStudentId() == null) {
            return Result.error("-1","用户名错误");
        }else if (!password.equals(student1.getPassword())) {
            return Result.error("-1","密码错误");
        }

        return studentService.login(student);
    }

    @GetMapping("/username/{id}")
    public Result<?> getName(@PathVariable Integer id) {
        Student student = studentMapper.selectById(id);
        System.out.println(student);
        return Result.success(student);
    }

    /**
     * 修改密码接口
     * @param
     * @return
     */
    @PutMapping("/individual")
    public Result<?> upPerson(@RequestBody Individual individual) {
        String password = studentService.getById(individual.getId()).getPassword();
        if (password.equals(individual.getPassword())){
            Student student = studentService.getById(individual.getId());
            student.setPassword(individual.getNewpassword());
            studentService.updateById(student);
        }else {
            return Result.error("-1","原密码输入错误");
        }
        return  Result.success();
    }

    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        LambdaQueryWrapper<Student> wrapper = Wrappers.<Student>lambdaQuery();
        wrapper.select(Student.class,fieldInfo->!fieldInfo.getColumn().equals("role"));
        List<Student> list = studentMapper.selectList(wrapper);
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (Student student : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", student.getStudentId());
            map.put("name", student.getName());
            map.put("password", student.getPassword());
            map.put("sex", student.getSex());
            map.put("phone", student.getPhone());
            map.put("floor", floorMapper.selectById(student.getFloorId()).getFloorName());
            map.put("bedroom", student.getBedroom());
            list1.add(map);
        }
        //通过工具类创建writer 写出到磁盘路径
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("studentId","学号");
        writer.addHeaderAlias("name","姓名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("phone","电话");
        writer.addHeaderAlias("floor","宿舍楼");
        writer.addHeaderAlias("bedroom","寝室号");

        //一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list1,true);

        //设置浏览器响应的格式
        response.setContentType("application/vnh.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息","UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    /**
     * 导入接口
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/import")
    public Result<?> imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Student> list = reader.readAll(Student.class);
        list.forEach(item -> {
            if (StrUtil.isBlank(item.getPassword())) {
                item.setPassword("123456");
            }

            item.setRole("ROLE_USER");

            QueryWrapper<Floor> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("floor_name",item.getFloor());
            List<Floor> floors = floorMapper.selectList(queryWrapper);
            item.setFloorId(floors.get(0).getId());

            studentMapper.insert(item);
        });
        return Result.success();
    }
}

