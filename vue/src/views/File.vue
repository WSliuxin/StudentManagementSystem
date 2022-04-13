<template>
  <div style="padding: 20px;min-height: calc(100vh - 50px)">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-upload  action="http://localhost:9090/files/upload2" :on-success="handleFileUploadSuccess" :on-error="handleFileUploadError" :show-file-list="false" style="display: inline-block;margin-right: 12px">
        <el-button type="primary" >上传文件</el-button>
      </el-upload>
      <el-popconfirm style="margin-left: 5px" type="success" title="确认批量删除吗?" cancel-button-text="我再想想" @confirm="delBatch">
        <template #reference>
          <el-button type="success">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="name" placeholder="输入文件名称" style="width: 20%;" clearable />
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
      <el-button type="warning" style="margin-left: 5px" @click="reset">清空</el-button>
    </div>
    <!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="handleSelectionChange" >
      <el-table-column type="selection" width="55" align="center"></el-table-column>
      <el-table-column prop="name" label="文件名称"  />
      <el-table-column prop="type" label="文件类型"  />
      <el-table-column prop="size" label="文件大小/kb" />
      <el-table-column prop="url" label="下载" >
        <template #default="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="enable" label="启用" >
        <template #default="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column align="center " width="150" fixed="right" label="操作">
        <template #default="scope">
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
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
import axios from "axios";

export default {
  name: "File" ,
  data() {
    return {
      tableData: [],
      name: "",
      multipleTableRef: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    fileUploadSuccess(res) {
      this.form.cover = res.data
    },
    load(){ //页面刷新
      request.get("/files/page",{
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data ? res.data.records : {}
        this.total = res.data ? res.data.total : 0
      })
    },
    reset (){
      this.name = ""
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
    handleDelete(id){ //删除
      request.delete("/files/"+ id).then(res => {
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
      request.post("/files/del/batch",ids).then(res => {
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
    handleFileUploadSuccess(res) {
      this.$message({
        type: "success",
        message: "导入成功"
      })
      this.load()
    },
    handleFileUploadError(res) {

    },
    download(url) {
      window.open(url)
    },
    changeEnable(row) {
      request.post("/files/update",row).then(res => {
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

<style scoped>

</style>