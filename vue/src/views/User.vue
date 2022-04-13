<template>
  <div style="padding: 20px;min-height: calc(100vh - 50px)">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm style="margin-left: 5px" type="success" title="确认批量删除吗?" cancel-button-text="我再想想" @confirm="delBatch">
        <template #reference>
          <el-button type="success">批量删除</el-button>
        </template>
      </el-popconfirm>
      <el-upload  action="http://localhost:9090/user/import" :on-success="handleExcelImportSuccess" :on-error="handleExcelImportError" :show-file-list="false" accept="xlsx" style="display: inline-block;margin-left: 12px">
        <el-button type="primary" >导入</el-button>
      </el-upload>
      <el-button type="primary"  style="margin-left: 12px" @click="exp" >导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="id" placeholder="输入ID" style="width: 20%;margin-right: 20px" clearable suffix-icon="el-icon-search"/>
      <el-input v-model="userName" placeholder="输入姓名" style="width: 20%;margin-right: 20px" clearable/>
      <el-input v-model="nickName" placeholder="输入昵称" style="width: 20%;margin-right: 20px" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
<!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="学号" width="80" sortable />
      <el-table-column label="头像" width="80" align="center ">
        <template #default="scope">
          <el-image
              style="width: 80%; height: 80%"
              :src="scope.row.cover"
              :preview-src-list="[scope.row.cover]"
              preview-teleported="true"/>
        </template>
      </el-table-column>
      <el-table-column prop="username" label="姓名"  align="center "/>
      <el-table-column prop="nickName" label="昵称" align="center "/>
      <el-table-column prop="age" label="年龄"  align="center "/>
      <el-table-column prop="sex" label="性别" align="center "/>
      <el-table-column prop="address" label="地址" align="center "/>
      <el-table-column label="角色" align="center ">
        <template #default="scope">
          <span v-if="scope.row.role === 1">系统管理员</span>
          <span v-if="scope.row.role === 2">宿舍管理员</span>
          <span v-if="scope.row.role === 3">学生</span>
        </template>
      </el-table-column>
      <el-table-column align="center " width="150" fixed="right" label="操作">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)" cancel-button-text="我再想想">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
<!--    分页-->
    <div style="margin: 10px 0">
      <el-pagination
          v-model:currentPage="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      />

      <el-dialog v-model="dialogVisible" title="编辑信息" width="30%">
        <el-form :model="form"  label-width="120px">
          <el-form-item prop="username" label="头像" >
            <el-upload ref="uploading" action="http://localhost:9090/files/upload" :show-file-list="false" :on-success="fileUploadSuccess">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="姓名" >
          <el-input v-model="form.username" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="昵称" >
            <el-input v-model="form.nickName" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="年龄" >
            <el-input v-model="form.age" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="性别" >
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
            <el-radio v-model="form.sex" label="未知">未知</el-radio>
          </el-form-item>
          <el-form-item label="地址" >
            <el-input  type="textarea" v-model="form.address" style="width: 80%"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 认</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<style>
</style>

<script>

import request from "@/utils/request";
import axios from "axios";

export default {
  name: 'Home',
  components: {
  },
  data(){
    return {
      form: {},
      dialogVisible: false,
      id: '',
      nickName:"",
      userName:"",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData:[],
      row: '/0',
      multipleTableRef: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    exp() {
      window.open("http://localhost:9090/user/export")
    },
    fileUploadSuccess(res) {
      this.form.cover = res.data
    },
    load(){ //页面刷新
      request.get("/user",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          id: this.id,
          userName: this.userName,
          nickName: this.nickName,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data ? res.data.records : {}
        this.total = res.data ? res.data.total : 0
      })
    },
    reset (){
      this.id = ""
      this.userName = ""
      this.nickName = ""
      this.load()
    },
    add(){ //新增弹出添加框
      this.dialogVisible = true
      this.form = {}
      this.$refs.uploading.clearFiles //清除历史文件上传列表
    },
    handleSelectionChange(val){
      this.multipleTableRef = val
    },
    save(){ //保存
      if (this.form.id){ //更新
        request.put("/user",this.form).then(res => {
          if (res.code === '0'){
            this.$message({
              type: "success",
              message: "更新成功"
            })
          }else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
        this.load()//刷新表格
        this.dialogVisible = false
      }else { //新增
        request.post( "/user",this.form).then(res => {
          if (res.code === '0'){
            this.$message({
              type: "success",
              message: "新增成功"
            })
          }else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
        this.load()//刷新表格
        this.dialogVisible = false
      }
    },
    handleEdit(row){ //编辑
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true

    },
    handleDelete(id){ //删除
      request.delete("/user/"+ id).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "删除成功"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() //删除后刷新
      })

    },
    delBatch(){   //批量删除
      let ids = this.multipleTableRef.map( v => v.id)   //[{},{},{}] = > [1,2,3]
      request.post("/user/del/batch",ids).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "批量删除成功"
          })
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load() //删除后刷新
      })
    },
    handleSizeChange(pageSize){ //改变当前每页的个数
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){ //改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    handleExcelImportSuccess() {
      this.$message.success("导入成功")
      this.load()
    },
    handleExcelImportError() {
      let a=axios.interceptors.response.use()
      this.$message.error("导入失败")
      this.load()
    }
  }
}
</script>