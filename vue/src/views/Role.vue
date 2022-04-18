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
<!--      <el-upload  action="http://localhost:9090/role/import" :on-success="handleExcelImportSuccess" :on-error="handleExcelImportError" :show-file-list="false" accept="xlsx" style="display: inline-block;margin-left: 12px">-->
<!--        <el-button type="primary" >导入</el-button>-->
<!--      </el-upload>-->
<!--      <el-button type="primary"  style="margin-left: 12px" @click="exp" >导出</el-button>-->
    </div>
<!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="name" placeholder="输入名称" style="width: 20%;margin-right: 20px" clearable suffix-icon="el-icon-search"/>
<!--      <el-input v-model="userName" placeholder="输入姓名" style="width: 20%;margin-right: 20px" clearable/>-->
<!--      <el-input v-model="nickName" placeholder="输入昵称" style="width: 20%;margin-right: 20px" clearable/>-->
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
<!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="id" label="ID" width="80" style="width: 80px;" sortable />
      <el-table-column prop="name" label="名称"  align="center "/>
      <el-table-column prop="key" label="唯一标识"  align="center "/>
      <el-table-column prop="description" label="描述" align="center "/>
      <el-table-column align="center " fixed="right" label="操作">
        <template #default="scope">
          <el-button type="info" size="mini" @click="selectMenu(scope.row)">分配菜单</el-button>
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

      <el-dialog v-model="dialogVisible" title="角色信息" width="30%">
        <el-form :model="form"  label-width="120px">
          <el-form-item label="名称" >
          <el-input v-model="form.name" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="唯一标识" >
            <el-input v-model="form.key" style="width: 80%"/>
          </el-form-item>
          <el-form-item label="描述" >
            <el-input v-model="form.description" style="width: 80%"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 认</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog v-model="MenuDialogVis" title="菜单分配" width="30%">
        <el-tree
            :data="menuData"
            :props="props"
            show-checkbox
            node-key="id"
            ref="tree"
            :default-expanded-keys="expends"
            :default-checked-keys="checks">
          <template #default="{ node, data }">
        <span class="custom-tree-node">
           <component :is="data.icon" style="width: 25px; height:25px;padding-top: 8px" />{{ data.name }}
        </span>
          </template>
        </el-tree>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="MenuDialogVis = false">取 消</el-button>
            <el-button type="primary" @click="saveRoleMenu">确 认</el-button>
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
import {Menu as IconMenu} from "@element-plus/icons-vue";
import router, {resetRouter} from "@/router";

export default {
  name: 'Role',
  components: {
    IconMenu
  },
  data(){
    return {
      form: {},
      dialogVisible: false,
      MenuDialogVis: false,
      name: '',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData:[],
      row: '/0',
      multipleTableRef: [],
      menuData: [],
      props: {
        label: 'name'
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleLKey: '',
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){ //页面刷新
      request.get("/role/page",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        console.log(res)
        this.tableData = res ? res.records : {}
        this.total = res ? res.total : 0
      })
    },
    reset (){
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
      if (this.form.id){ //更新
        request.put("/role",this.form).then(res => {
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
        console.log(this.form)
        request.post( "/role",this.form).then(res => {
          console.log(res.code)
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
      request.delete("/role/"+ id).then(res => {
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
      request.post("/role/del/batch",ids).then(res => {
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
    selectMenu(role) {

      this.roleId = role.id
      this.roleLKey = role.key
      //请求菜单数据
      request.get("/menu").then(res => {
        this.menuData = res.data

        //把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      //
      request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data

        request.get("/menu/ids").then(r => {
          const ids = r.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              this.$nextTick(() => {
                this.$refs.tree.setChecked(id,false)
              })
            }
          })
        })
        this.MenuDialogVis = true
      })
    },
    saveRoleMenu(){
      request.post("/role/roleMenu/"+this.roleId,this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "分配成功"
          })
          if (this.roleLKey === 'ROLE_ADMIN') {
            localStorage.removeItem("user")
            localStorage.removeItem("menus")
            router.push('/login')
            resetRouter()
          }
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.MenuDialogVis = false
      })
    }
  }
}
</script>