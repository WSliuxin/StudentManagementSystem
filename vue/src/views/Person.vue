<template>
  <div>
    <el-card class="box-card" style="width:90%;height: calc(100vh - 200px) ;margin: 20px 50px;">
      <el-form label-position="right" ref="form" :model="form" style="margin: 0 auto;width: 30%" :rules="rules">
        <el-form-item prop="password">
          <span style="width: 80px">原密码：</span>
          <el-input :prefix-icon="Lock" v-model="form.password" style="width: 40%" show-password >
            <template #prefix>
              <el-icon class="el-input__icon">
                <lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="newpassword">
          <span style="width: 80px">新密码：</span>
          <el-input :prefix-icon="Lock" v-model="form.newpassword" style="width: 40%" show-password >
            <template #prefix>
              <el-icon class="el-input__icon">
                <lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="newpassword2">
          <span style="width: 80px">确认密码：</span>
          <el-input :prefix-icon="Lock" v-model="form.newpassword2" style="width: 40%" show-password >
            <template #prefix>
              <el-icon class="el-input__icon">
                <lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
      </el-form>
      <div style="text-align: center;margin-right: 70px">
        <el-button type="primary" @click="update">确认</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Person",
  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.newpassword) {
        callback(new Error("两次密码输入不重复"))
      } else {
        callback()
      }
    }
    return {
      form: {},
      role: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).role : "",
      rules: {
        password: [
          { required: true, message: '请输入原密码', trigger: 'blur' },
        ],
        newpassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { pattern: "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{6,}$", message: '至少6字符，必需包含字母和数字' }
        ],
        newpassword2: [
          { validator: validatePass2, trigger: 'blur'},
        ]
      }
    }
  },
  created() {
  },
  methods: {
    async getUser() {
      return (await this.request.get("/user/username/"+this.user.username)).data()
    },
    update(){
      let str=""
      if (this.form.newpassword !== this.form.newpassword2){
        this.$message.error('两次密码输入不正确')
        this.refreshCode()
        return
      }
      switch(this.role) {
        case "ROLE_ADMIN" :
          str = "user"
          break
        case "ROLE_TUBES" :
          str = "dormitory"
          break
        default :
          str = "student"
          break
      }
      this.form.id = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).id : "";
      request.put("/"+str+"/individual", this.form).then(res => {
        if (res.code === '0'){
          this.$message({
            type: "success",
            message: "修改成功"
          })
          localStorage.clear()
          this.$router.push("/login")
        }else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    validatePass2() {
      return 1
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