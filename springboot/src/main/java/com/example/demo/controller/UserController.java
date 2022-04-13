package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.controller.dto.UserDTO;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    /**
     * 导出接口
     * @param response
     * @throws Exception
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception{

        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        wrapper.select(User.class,fieldInfo->!fieldInfo.getColumn().equals("cover")&&!fieldInfo.getColumn().equals("role"));
        List<User> list = userMapper.selectList(wrapper);
        List<Map<String,Object>> list1 = new ArrayList<>();
        for (int i = 0 ;i < list.size() ;i++ ){
            Map<String,Object> map = new HashMap<>();
            map.put("id",list.get(i).getId());
            map.put("username",list.get(i).getUsername());
            map.put("password",list.get(i).getPassword());
            map.put("nickName",list.get(i).getNickName());
            map.put("age",list.get(i).getAge());
            map.put("sex",list.get(i).getSex());
            map.put("address",list.get(i).getAddress());
            list1.add(map);
        }

        //通过工具类创建writer 写出到磁盘路径
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","ID");
        writer.addHeaderAlias("username","姓名");
        writer.addHeaderAlias("password","密码");
        writer.addHeaderAlias("nickName","昵称");
        writer.addHeaderAlias("age","年龄");
        writer.addHeaderAlias("sex","性别");
        writer.addHeaderAlias("address","地址");

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
        List<User> list = reader.readAll(User.class);
        list.forEach(item -> {
            userMapper.insert(item);
        });
        return Result.success();
    }

    /**
     * 新增信息接口
     * @param user
     * @return
     */
    @PostMapping
    public Result<?> save(@RequestBody User user) {
        if (user.getPassword()==null){
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return  Result.success();
    }

    /**
     * 修改保存信息接口
     * @param user
     * @return
     */
    @PutMapping
    public Result<?> updata(@RequestBody User user) {
        userMapper.updateById(user);
        return  Result.success();
    }

    /**
     * 批量删除信息接口
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        userMapper.deleteBatchIds(ids);
        return Result.success();
    }

    /**
     * 查询信息接口
     * @param pageNum
     * @param pageSize
     * @param id
     * @param userName
     * @param nickName
     * @return
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String id,
                              @RequestParam(defaultValue = "") String userName,
                              @RequestParam(defaultValue = "") String nickName)
    {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(nickName)) {
            wrapper.like(User::getNickName,nickName);
        }
        if (StrUtil.isNotBlank(userName)){
            wrapper.like(User::getUsername,userName);
        }
        if (StrUtil.isNotBlank(id)){
            wrapper.like(User::getId,id);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum,pageSize), wrapper);
        return Result.success(userPage);
    }

    /**
     * 删除信息接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userMapper.deleteById(id);
        return  Result.success();
    }

    /**
     * 登录接口
     * @param user
     * @return
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        try {
            User res = userMapper.selectOne(wrapper.eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
            if (res ==null){
                return Result.error("-1","用户名或密码错误");
            }
            UserDTO userDTO = new UserDTO();
            userDTO = Information(res);
            return  Result.success(userDTO);
        } catch (Exception e){
            return Result.error("-2","系统错误");
        }


    }

    /**
     * 注册接口
     * @param user
     * @return
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = Wrappers.<User>lambdaQuery();
        User res = userMapper.selectOne(wrapper.eq(User::getUsername, user.getUsername()));
        if (res != null) {
            return Result.error("-1","用户名重复");
        }
        if (user.getPassword() == null) {
            user.setPassword("123456");
        }
        userMapper.insert(user);
        return  Result.success();
    }

    /**
     * 查询id接口
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) { return Result.success(userMapper.selectById(id)); }



    private UserDTO Information(User res){

        UserDTO userDTO = new UserDTO();
        //ID
        userDTO.setId(res.getId());
        //名字
        userDTO.setUsername(res.getUsername());
        //昵称
        userDTO.setNickname(res.getNickName());
        //权限
        userDTO.setRole(res.getRole());
        //头像地址
        userDTO.setAvatarUrl(res.getCover());
        //Token
        String token = TokenUtils.genToken(res.getId().toString(),res.getPassword());
        userDTO.setToken(token);
        //性别
        userDTO.setSex(res.getSex());
        //年龄
        userDTO.setAge(res.getAge());
        //地址
        userDTO.setAddress(res.getAddress());

        return userDTO;

    }
}
