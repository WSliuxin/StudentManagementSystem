<template>
  <div style="padding: 20px;min-height: calc(100vh - 50px)">
<!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary"  style="margin-left: 12px" @click="exp" >导出</el-button>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="name" placeholder="输入姓名" style="width: 20%;margin-right: 20px" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
<!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column prop="name" label="姓名"  align="center "/>
      <el-table-column prop="sex" label="性别" align="center "/>
      <el-table-column prop="phone" label="联想电话" align="center "/>
      <el-table-column prop="floor" label="宿舍楼" align="center "/>
      <el-table-column prop="nickName" label="用户名" align="center "/>
      <el-table-column prop="enable" label="启用" >
        <template #default="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
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

      <el-dialog v-model="dialogVisible" title="用户信息" width="30%">
        <el-form :model="form"  label-width="120px">
          <el-form-item label="用户名" >
          <el-input v-model="form.nickName" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="姓名" >
            <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="性别" >
            <el-radio v-model="form.sex" label="男">男</el-radio>
            <el-radio v-model="form.sex" label="女">女</el-radio>
          </el-form-item>
          <el-form-item label="联系电话" >
            <el-input v-model="form.phone" style="width: 80%"/>
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
  name: 'Student',
  components: {
  },
  data(){
    return {
      form: {},
      dialogVisible: false,
      studentId:'',
      name:"",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData:[],
      row: '/0',
      multipleTableRef: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    exp() {
      window.open("http://localhost:9090/student/export")
    },
    fileUploadSuccess(res) {
      this.form.cover = res.data
    },
    load(){ //页面刷新
      request.get("/dormitory/page",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          studentId: this.studentId,
          name: this.name,
        }
      }).then(res => {
        console.log(res.records)
        this.tableData = res.records ? res.records : {}
        this.total = res.total ? res.total : 0
      })


    },
    reset (){
      this.studentId = ""
      this.name = ""
      this.load()
    },
    add(){ //新增弹出添加框
      this.dialogVisible = true
      this.form = {}
    },
    handleSelectionChange(val){
      this.multipleTableRef = val
    },
    save(){ //保存
      console.log(this.form.id)
      if (this.form.id){ //更新
        request.put("/dormitory",this.form).then(res => {
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
        request.post( "/dormitory",this.form).then(res => {
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
      request.delete("/dormitory/"+ id).then(res => {
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
    handleSizeChange(pageSize){ //改变当前每页的个数
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){ //改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    changeEnable(row) {
      request.post("/dormitory/update",row).then(res => {
        if (res.code === "0") {
          this.$message({
            type: "success",
            message: "操作成功"
          })
        }

      })
    }
  }
}
</script>