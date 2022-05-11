<template>
  <div style="padding: 20px;min-height: calc(100vh - 70px)">
<!--    搜索区域-->
    <div style="margin: 10px 0" v-if="user.role !== 'ROLE_USER'">
      <el-select v-model="value" placeholder="学号" style="width: 80px;margin-right: 10px">
        <el-option
            v-for="item in options2"
            :key="item.value"
            :label="item.label"
            :value="item.value"
        />
      </el-select>
      <el-input v-model="name" placeholder="输入内容" style="width: 15%;margin-right: 10px" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
<!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center" v-if="user.role === 'ROLE_ADMIN'"></el-table-column>
      <el-table-column prop="studentId" label="学号" width="80" sortable />
      <el-table-column prop="name" label="姓名"  align="center "/>
      <el-table-column prop="floorName" label="宿舍楼" align="center "/>
      <el-table-column prop="bedroom" label="寝室号" align="center "/>
      <el-table-column prop="startTime" label="开始时间" align="center "/>
      <el-table-column prop="endTime" label="结束时间" align="center "/>
      <el-table-column prop="reason" label="请假原因" align="center ">
        <template #default="scope">
          <el-button size="mini" type="warning" @click="look(scope.row)">请假原因</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="state" label="状态" align="center ">
        <template #default="scope">
          <el-tag class="ml-2" type="danger" v-if="scope.row.state === -1">请假未通过</el-tag>
          <el-tag v-if="scope.row.state === 0">审批中</el-tag>
          <el-tag class="ml-2" type="success" v-if="scope.row.state === 1">请假中</el-tag>
          <el-tag class="ml-2" type="warning" v-if="scope.row.state === 2">未销假</el-tag>
          <el-tag class="ml-2" type="info" v-if="scope.row.state === 3">已销假</el-tag>
        </template>
      </el-table-column>>
      <el-table-column align="center " width="300" fixed="right" label="操作">
        <template #default="scope" v-if="user.role === 'ROLE_USER'">
          <el-button size="mini" @click="handle(scope.row)" v-if="scope.row.state === 2||scope.row.state ===1">销假</el-button>
          <el-popconfirm title="是否撤销吗?" @confirm="handleDelete(scope.row)" cancel-button-text="我再想想" v-if="scope.row.state === 0||scope.row.state ===-1">
            <template #reference>
              <el-button size="mini" type="primary">撤销</el-button>
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

      <el-dialog v-model="dialogVisible" title="请假原因" width="30%" draggable>
        <span>{{reason}}</span>
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
  name: 'LeManage',
  components: {
  },
  data(){
    return {
      user: JSON.parse(localStorage.getItem("user")) ? JSON.parse(localStorage.getItem("user")): {},
      name:"",
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData:[],
      row: '/0',
      multipleTableRef: [],
      options: {},
      value:"",
      type: "",
      dialogVisible:false,
      reason:"",
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){ //页面刷新
      if (this.user.role === "ROLE_USER"){
          this.type = this.user.studentId
      }
      switch (this.value) {
        case "姓名" :
          request.get("/leave/page",{
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              name: this.name,
              id: this.type
            }
          }).then(res => {
            this.tableData = res.records ? res.records : {}
            this.total = res.total ? res.total : 0
          });
          break
        case "性别" :
          this.sex = this.name
          request.get("/leave/page",{
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              sex: this.sex,
              id: this.type
            }
          }).then(res => {
            this.tableData = res.records ? res.records : {}
            this.total = res.total ? res.total : 0
          })
          break
        case "宿舍楼" :
          this.floor = this.name
          request.get("/leave/page",{
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              floor: this.floor,
              id: this.type
            }
          }).then(res => {
            this.tableData = res.records ? res.records : {}
            this.total = res.total ? res.total : 0
          })
          break
        default :
          this.studentId = this.name
          request.get("/leave/page",{
            params: {
              pageNum: this.currentPage,
              pageSize: this.pageSize,
              studentId: this.studentId,
              id: this.type,
              role: this.user.role
            }
          }).then(res => {
            this.tableData = res.records ? res.records : {}
            this.total = res.total ? res.total : 0
          })
          break
      }
    },
    reset (){
      this.studentId = ""
      this.name = ""
      this.value = ""
      this.load()
    },
    handleSelectionChange(val){
      this.multipleTableRef = val
    },
    delBatch(){   //批量删除
      let ids = this.multipleTableRef.map( v => v.id)   //[{},{},{}] = > [1,2,3]
      request.post("/student/del/batch",ids).then(res => {
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
    handle(row){
      request.put("/leave/update/"+row.leaveId).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "销假成功"
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
    handleDelete(row){
      request.delete("/leave/del/"+ row.leaveId).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "撤销成功"
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
    look(row){
      this.reason = row.reason
      this.dialogVisible = true
    }
  }
}
</script>
