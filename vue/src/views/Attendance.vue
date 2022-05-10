<template>
  <div style="padding: 20px;min-height: calc(100vh - 70px)">
<!--    功能区域-->
    <div style="margin: 10px 0" v-if="user.role!=='ROLE_USER'">
      <el-button type="primary" @click="add">新增</el-button>
      <el-popconfirm style="margin-left: 5px" type="success" title="确认批量删除吗?" cancel-button-text="我再想想" @confirm="delBatch">
        <template #reference>
          <el-button type="success">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-select v-model="value" placeholder="学号" style="width: 80px;margin-right: 10px">
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="name" placeholder="输入姓名" style="width: 15%;margin-right: 10px" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
<!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="date" label="日期" width="100" sortable />
      <el-table-column prop="studentId" label="学号" width="80" sortable />
      <el-table-column prop="name" label="姓名"  align="center "/>
      <el-table-column prop="floorName" label="宿舍楼" align="center "/>
      <el-table-column prop="content" label="备注" align="center "/>
      <el-table-column prop="bedroom" label="寝室号" align="center "/>
      <el-table-column align="center " width="150" fixed="right" label="操作" v-if="user.role !== 'ROLE_USER'">
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

      <el-dialog v-model="dialogVisible" title="缺勤信息" width="30%">
        <el-form :model="form"  label-width="120px">
          <el-form-item label="日期" >
            <el-date-picker v-model="form.date" value-format="YYYY-MM-DD" type="date" style="width: 80%;" clearable></el-date-picker>
          </el-form-item>
          <el-form-item label="学号" >
            <el-input v-model="form.studentId" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="备注" >
            <el-input v-model="form.content" style="width: 80%"/>
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
  name: 'Attendance',
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
      options: {},
      user: JSON.parse(localStorage.getItem("user")) ? JSON.parse(localStorage.getItem("user")) : {},
      value: "",
      options2: [
        {
          value: '学号',
          label: '学号',
        },
        {
          value: '姓名',
          label: '姓名',
        },
      ],
      type: "",
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
      if (this.user.role==="ROLE_TUBES"||this.user.role==="ROLE_USER"){
        this.type = this.user.id
      }
      if (this.user.role==="ROLE_USER") {
        request.get("/attendance/page",{
          params: {
            pageNum: this.currentPage,
            pageSize: this.pageSize,
            studentId: this.user.studentId,
          }
        }).then(res => {
          this.tableData = res.records ? res.records : {}
          this.total = res.total ? res.total : 0
        })
      }else {
        switch (this.value) {
          case "姓名" :
            request.get("/attendance/page",{
              params: {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                name: this.name,
                id: this.type
              }
            }).then(res => {
              this.tableData = res.records ? res.records : {}
              this.total = res.total ? res.total : 0
            })
            break
          default :
            this.studentId = this.name
            request.get("/attendance/page",{
              params: {
                pageNum: this.currentPage,
                pageSize: this.pageSize,
                studentId: this.studentId,
                id: this.type
              }
            }).then(res => {
              this.tableData = res.records ? res.records : {}
              this.total = res.total ? res.total : 0
            })
        }
      }
    },
    reset (){
      this.studentId = ""
      this.name = ""
      this.load()
    },
    add(){ //新增弹出添加框
      this.dialogVisible = true
      this.form = {}
      request.get("/floor/findAll").then(res => {
        this.options = res.data
      })
    },
    handleSelectionChange(val){
      this.multipleTableRef = val
    },
    save(){ //保存
      console.log(this.form.date)
      if (this.form.id){ //更新
        request.put("/attendance",this.form).then(res => {
          if (res.code === '0'){
            this.$message({
              type: "success",
              message: "修改成功"
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
        request.post( "/attendance",this.form).then(res => {
          if (res.code === '0'){
            this.$message({
              type: "success",
              message: "添加成功"
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
      request.get("/floor/findAll").then(res => {
        this.options = res.data
      })
    },
    handleDelete(id){ //删除
      request.delete("/attendance/"+ id).then(res => {
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
      request.post("/attendance/del/batch",ids).then(res => {
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
  }
}
</script>
