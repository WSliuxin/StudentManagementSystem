package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.DorDTO;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.Dormitory;
import com.example.demo.entity.Menu;
import com.example.demo.entity.User;
import com.example.demo.mapper.DormitoryMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.RoleMenuMapper;
import com.example.demo.service.IDormitoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.service.IMenuService;
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
public class DormitoryServiceImpl extends ServiceImpl<DormitoryMapper, Dormitory> implements IDormitoryService {

    @Resource
    DormitoryMapper dormitoryMapper;

    @Resource
    RoleMapper roleMapper;

    @Resource
    private IMenuService menuService;

    @Resource
    RoleMenuMapper roleMenuMapper;

    @Override
    public Result<?> login(Dormitory dormitory) {
        QueryWrapper<Dormitory> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("nick_name",dormitory.getNickName());
        queryWrapper.eq("password",dormitory.getPassword());
        Dormitory one = getOne(queryWrapper);

        DorDTO dorDTO = new DorDTO();
        dorDTO = Information(one);
        return Result.success(dorDTO);
    }

    @Override
    public Page<Dormitory> findPage(Page<Dormitory> page, String name) {
        return dormitoryMapper.findPage(page,name);
    }

    /**
     * 将User转成UserDTO
     * @param res
     * @return
     */
    private DorDTO Information(Dormitory res){

        DorDTO dorDTO = new DorDTO();

        BeanUtil.copyProperties(res,dorDTO,true);

        //Token
        String token = TokenUtils.genToken(res.getRole()+res.getId().toString(),res.getPassword());
        dorDTO.setToken(token);


        String role = res.getRole();

        //设置用户菜单列表
        List<Menu> roleMenus = getRoleMenu(role);
        dorDTO.setMenus(roleMenus);

        return dorDTO;
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
