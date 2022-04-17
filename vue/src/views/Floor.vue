<template>
  <div style="padding: 20px 100px 20px 40px;">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">名称：
      <el-input v-model="search" placeholder="输入关键字" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column type="index" prop="id" label="编号" width="100" />
      <el-table-column prop="name" label="名称" width="150" />
      <el-table-column prop="synopsis" label="简介" />
      <el-table-column prop="administrators" label="管理员" width="150" />
      <el-table-column fixed="right" label="操作" width="150">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope)">编辑</el-button>
          <el-popconfirm title="确认删除吗?" @confirm="handleDelete(scope.row.id)">
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
          <el-form-item label="编号" >
            <el-input v-model="id" disabled style="width: 80%"/>
          </el-form-item>
          <el-form-item label="名称" >
            <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="简介" >
            <el-input v-model="form.synopsis" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="管理员" >
            <el-input v-model="form.administrators" style="width: 80%"/>
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

<script>

import request from "@/utils/request";

export default {
  name: 'Floor',
  components: {
  },
  data(){
    return {
      form: {},
      dialogVisible: false,
      search: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData:[],
      id: 0,
      user: {}
    }
  },
  created() {
    let userStr = localStorage.getItem("user") || {}
    this.user = JSON.parse(userStr)

    //请求服务器，确认当前登录用户的合法信息
    request.get("/user/"+this.user.id).then( res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })

    this.load()
  },
  methods: {
    load(){ //页面刷新
      request.get("/floor",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add(){ //新增弹出添加框
      this.id = this.total+1
      this.dialogVisible = true
      this.form = {}
    },
    save(){ //保存
      if (this.form.id){ //更新
        request.put("/floor",this.form).then(res => {
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
        request.post( "/floor",this.form).then(res => {
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
    handleEdit(scope){ //编辑
      this.id=scope.$index+1
      this.form = JSON.parse(JSON.stringify(scope.row))
      this.dialogVisible = true
    },
    handleDelete(id){ //删除
      request.delete("/floor/"+ id).then(res => {
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
    }
  }
}
</script>
