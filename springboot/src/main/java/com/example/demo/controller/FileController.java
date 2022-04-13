package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Files;
import com.example.demo.entity.User;
import com.example.demo.mapper.FileMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")     //获取端口
    private String port;

    private static final String ip = "http://localhost";

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private FileMapper fileMapper;
    /**
     * 文件上传接口
     * @param file  前端传递过来的文件
     * @return
     * @throws IOException
     */
    @PostMapping("/upload2")
    public String upload2(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);
        if (!uploadFile.getParentFile().exists()){
            uploadFile.getParentFile().mkdirs();
        }

        String md5;
        String url;
        //上传文件
        file.transferTo(uploadFile);
        // 获取文件的MD5
        md5 = SecureUtil.md5(uploadFile);
        //查询是否存在相同的记录
        Files files = getFilesMD5(md5);
        if (files!=null){
            url=files.getUrl();
            //由于文件已存在，所以删除刚才上传的重复文件
            uploadFile.delete();
        }else {
            // 数据库若不存在重复文件，则不删除刚才上传的文件
            url = "http://localhost:9090/files/"+fileUUID;
        }

        //存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);

        //保存数据到数据库
        fileMapper.insert(saveFile);
        return url;
    }

    /**
     * 上传接口
     * */
//    @PostMapping("/upload")
//    public Result<?> upload(MultipartFile file) throws IOException {
//        String filename = file.getOriginalFilename(); //获取源文件的名称
//        //定义文件的唯一标识（前缀）
//        String flag = IdUtil.fastSimpleUUID();
//        String rootFilePath = System.getProperty("user.dir")+"/springboot/src/main/resources/files/" + flag + "_" + filename; //获取上传的路径名称
//        FileUtil.writeBytes(file.getBytes(), rootFilePath); //把文件写入到上传的路径
//        return Result.success(ip + ":" + port + "/files/" + flag); //返回结果 URL
//    }

    /**
     * 文件下载接口   http://localhost:9090/files/{fileUUID}
     * @param fileUUID
     * @param response
     * @throws IOException
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        //设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileUUID,"UTF-8"));
        response.setContentType("application/octet-stream");

        //读取文件字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }
    /**
     * 富文本上传接口
     * */
    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename(); //获取源文件的名称
        //定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        String rootFilePath = System.getProperty("user.dir")+"/springboot/src/main/resources/files/" + flag + "_" + filename; //获取上传的路径名称
        FileUtil.writeBytes(file.getBytes(), rootFilePath); //把文件写入到上传的路径
        String url = ip + ":" + port + "/files/" + flag;
        JSONObject json = new JSONObject();
        json.set("errno",0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        data.set("url",url);
        arr.add(data);
        json.set("data",arr);
        return json; //返回结果 URL
    }
    /**
     * 下载接口
     * */
//    @GetMapping("/{flag}")
//    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
//        OutputStream os;  //新建一个输入流对象
//        String basePath = System.getProperty("user.dir")+"/springboot/src/main/resources/files/";  //定于文件上传的根路径
//        List<String> fileNames = FileUtil.listFileNames(basePath);  //获取所有文件名称
//        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");    //找到和参数一致的文件
//        try {
//            if (StrUtil.isNotBlank(fileName)) {
//                response.addHeader("Content-Disposition","attachment;filename=" + URLEncoder.encode(fileName,"UTF-8"));
//                response.setContentType("application/octet-stream");
//                byte[] bytes = FileUtil.readBytes(basePath+fileName);   //通过文件的路径读取文件字节流
//                os = response.getOutputStream();    //通过输出流返回文件
//                os.write(bytes);
//                os.flush();
//                os.close();
//            }
//        } catch (Exception e) {
//            System.out.println("文件下载失败");
//        }
//    }


    /**
     * 同文件的MD5查询文件是否存在
     * @param md5
     * @return
     */
    private Files getFilesMD5(String md5) {

        //查询文件的MD5是否存在
        QueryWrapper<Files> wrapper = new QueryWrapper<>();
        wrapper.eq("md5",md5);
        List<Files> filesList = fileMapper.selectList(wrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    /**
     * 查询信息接口
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String name)
    {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete",false);
        queryWrapper.orderByDesc("id");
        if (StrUtil.isNotBlank(name)) {
            queryWrapper.like("name",name);
        }
        Page<Files> filesPage = fileMapper.selectPage(new Page<>(pageNum,pageSize), queryWrapper);
        return Result.success(filesPage);
    }

    /**
     * 更新信息接口
     * @param
     * @return
     */
    @PostMapping("/update")
    public Result<?> save(@RequestBody Files files) {
        fileMapper.updateById(files);
        return  Result.success();
    }
    /**
     * 批量删除信息接口
     * @param ids
     * @return
     */
    @PostMapping("/del/batch")
    public Result<?> deleteBatch(@RequestBody List<Integer> ids){
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    /**
     * 删除信息接口
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return  Result.success();
    }
}
