package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.DorDTO;
import com.example.demo.controller.dto.StuDTO;
import com.example.demo.entity.Dormitory;
import com.example.demo.entity.Menu;
import com.example.demo.entity.Student;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.IMenuService;
import com.example.demo.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxin
 * @since 2022-04-20
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {

    @Resource
    RoleMapper roleMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Resource
    StudentMapper studentMapper;

    @Override
    public Result<?> login(Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",student.getStudentId());
        queryWrapper.eq("password",student.getPassword());
        Student one = getOne(queryWrapper);

        StuDTO stuDTO = new StuDTO();
        stuDTO = Information(one);
        return Result.success(stuDTO);
    }

    @Override
    public Page<Student> findPage(Page<Student> page, String studentId, String name, String sex,String floor,String id) {
        return studentMapper.findPage(page,studentId,name,sex,floor,id);
    }

    /**
     * 将User转成UserDTO
     * @param res
     * @return
     */
    private StuDTO Information(Student res){

        StuDTO stuDTO = new StuDTO();

        stuDTO.setId(res.getId());
        stuDTO.setName(res.getName());
        stuDTO.setPhone(res.getPhone());
        stuDTO.setStudentId(res.getStudentId());
        stuDTO.setRole(res.getRole());
        stuDTO.setSex(res.getSex());


        //Token
        String token = TokenUtils.genToken(res.getRole()+res.getId().toString(),res.getPassword());
        stuDTO.setToken(token);

        String role = res.getRole();

        //设置用户菜单列表
        List<Menu> roleMenus = getRoleMenu(role);
        stuDTO.setMenus(roleMenus);


        return stuDTO;
    }

    /**
     * 获取当前角色的菜单项
     * @param role
     * @return
     */
    private List<Menu> getRoleMenu(String role) {

        Integer roleId = roleMapper.selectByKey(role);
        //当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);
        //查出系统所有的菜单
        List<Menu> menus = menuService.findMenu("");
        //最后筛选完成后的list
        List<Menu> roleMenus = new ArrayList<>();

        //筛选当前用户角色菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();

            // removeIf() 移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }

        return roleMenus;
    }
}
