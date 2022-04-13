<template>
  <div >
    <el-card class="box-card" style="width:90%;height: calc(100vh - 90px) ;margin: 20px 50px;">
      <el-form label-position="right" ref="form" :model="form" style="margin: 0 auto;width: 30%">
        <el-form-item label="姓名">
          <el-input v-model="form.username" style="width: 40%;"></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" style="width: 40%;"></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" style="width: 30%;"></el-input>
        </el-form-item>
        <el-form-item label="性别" >
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
          <el-radio v-model="form.sex" label="未知">未知</el-radio>
        </el-form-item>
        <el-form-item label="密码" >
          <el-input v-model="form.password" show-password style="width: 60%;"></el-input>
        </el-form-item>
        <el-form-item label="地址" >
          <el-input type="textarea" v-model="form.address" style="width: 80%;"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center">
        <el-button type="primary" @click="update">保存</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",
  data() {
    return {
      form: {}
    }
  },
  created() {
    let str = localStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    load() {

    },
    update(){
      request.put("/user",this.form).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "保存成功"
          })
          this.form.password=""
          localStorage.setItem("user",JSON.stringify(this.form))
          location.reload()
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>