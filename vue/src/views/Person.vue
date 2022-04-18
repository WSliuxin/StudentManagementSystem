<template>
  <div >
    <el-card class="box-card" style="width:90%;height: calc(100vh - 90px) ;margin: 20px 50px;">
      <el-form label-position="right" ref="form" :model="form" style="margin: 0 auto;width: 30%">
        <el-upload
            class="avatar-uploader"
            action="http://localhost:9090/files/upload2"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
        >
          <img v-if="form.cover" :src="form.cover" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
        <el-form-item label="姓名" style="margin-left: 10px">
          <el-input v-model="form.username" style="width: 40%;"></el-input>
        </el-form-item>
        <el-form-item label="昵称" style="margin-left: 10px">
          <el-input v-model="form.nickName" style="width: 40%;"></el-input>
        </el-form-item>
        <el-form-item label="年龄" style="margin-left: 10px">
          <el-input v-model="form.age" style="width: 30%;"></el-input>
        </el-form-item>
        <el-form-item label="性别" style="margin-left: 10px">
          <el-radio v-model="form.sex" label="男">男</el-radio>
          <el-radio v-model="form.sex" label="女">女</el-radio>
          <el-radio v-model="form.sex" label="未知">未知</el-radio>
        </el-form-item>
        <el-form-item label="地址" style="margin-left: 10px">
          <el-input type="textarea" v-model="form.address" style="width: 80%;"></el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center;margin-left: 10px">
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
      form: {},
    }
  },
  created() {
    let str = localStorage.getItem("user") || "{}"
    this.form = JSON.parse(str)
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/"+this.user.username)).data()
    },
    update(){
      this.form.nickName = this.form.nickname
      request.put("/user/individual",this.form).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "保存成功"
          })
          localStorage.setItem("user",JSON.stringify(res))
          this.form=res.data
          this.$emit("refreshUser")
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.cover = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
}
</style>