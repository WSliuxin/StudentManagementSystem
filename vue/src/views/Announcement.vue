<template>
  <div style="padding: 20px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增</el-button>
      <el-button type="primary">导入</el-button>
      <el-button type="primary">导出</el-button>
    </div>
    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="输入发布人信息" style="width: 20%" clearable/>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    内容-->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column type="index" prop="id" label="编号" sortable width="100" />
      <el-table-column prop="title" label="标题"  />
      <el-table-column prop="author" label="发布人" />
      <el-table-column prop="time" label="时间"  />
      <el-table-column fixed="right" label="操作">
        <template #default="scope">
          <el-button size="mini" @click="details(scope.row)">详情</el-button>
          <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
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

      <el-dialog v-model="dialogVisible" title="编辑信息" width="50%">
        <el-form :model="form"  label-width="120px">
          <el-form-item label="标题" >
            <el-input v-model="form.title" style="width: 50%"/>
          </el-form-item>

          <div id="div1">

          </div>
<!--          <el-form-item label="公告内容" >-->
<!--            <el-input type="textarea" v-model="form.content" style="width: 80%"/>-->
<!--          </el-form-item>-->
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 认</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog v-model="vis" title="详情" width="50%">
        <el-card>
          <div v-html="detail.content" style="min-height: 100px"></div>
        </el-card>
      </el-dialog>
    </div>
  </div>
</template>

<script>

import request from "@/utils/request";
import E from 'wangeditor'

let  editor;

export default {
  name: 'Announcement',
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
      detail: {},
      vis: false
    }
  },
  created() {
    this.load()
  },
  mounted() {
  },
  methods: {
    details(row){ //详情按钮
      this.detail = row
      this.vis = true
    },
    load(){ //页面刷新
      request.get("/announcement",{
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
      this.dialogVisible = true
      this.form = {}

      this.$nextTick( () => {
        //关联弹窗里面的div，new一个editor对象
        editor = new E('#div1')

        // 配置 server 接口地址
        editor.config.uploadImgServer = '/upload-img'

        editor.create()
      })
    },
    save(){ //保存
      this.form.content = editor.txt.html()   //获取编辑器内的内容，赋值到实体中

      if (this.form.id){ //更新
        request.put("/announcement",this.form).then(res => {
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

        let userStr = sessionStorage.getItem("user") || "{}"
        let user = JSON.parse(userStr)
        this.form.author = user.nickName

        request.post( "/announcement",this.form).then(res => {
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


      this.$nextTick( () => {
        if (!editor) {
          //关联弹窗里面的div，new一个editor对象
          editor = new E('#div1')
          editor.create()
        }
        editor.txt.html(row.content)
      })


    },
    handleDelete(id){ //删除
      console.log(id)
      request.delete("/announcement/"+ id).then(res => {
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
  }
}
</script>
