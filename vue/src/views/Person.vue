<template>
  <div>
    <el-card class="box-card" style="width: 40%; margin: 10px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickName" ></el-input>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="form.age" ></el-input>
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="form.sex" ></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input type="textarea" v-model="form.address" ></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" show-password></el-input>
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
    let str = sessionStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    load() {

    },
    update(){
      console.log(this.form)
      request.put("/user",this.form).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "保存成功"
          })
          sessionStorage.setItem("user",JSON.stringify(this.form))
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