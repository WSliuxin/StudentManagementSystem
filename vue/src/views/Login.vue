<template>
  <div class="wrapper" style="width: 100%; height: 100vh;background-color: darkslateblue; overflow: hidden">
    <div style="width: 350px;height: 300px; margin: 200px auto;background-color: #ffffff;padding: 20px;border-radius: 12px">
      <div style="color: #161515;font-size: 30px;text-align: center;padding: 30px 0">登录</div>
      <el-form style="margin: 0 20px;" ref="form" :model="form" size="normal" :rules="rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="UserFilled" v-model="form.username" >
            <template #prefix>
              <el-icon class="el-input__icon">
                <user-filled/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" v-model="form.password" show-password >
            <template #prefix>
              <el-icon class="el-input__icon">
                <lock/>
              </el-icon>
            </template>
          </el-input>
        </el-form-item>
        <div style="text-align: center">
          <el-button type="primary" @click="login">登录</el-button>
          <el-button color="#FF6B58" style="color: #FFFFFF" @click="register">注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style>
.wrapper {
  background-image: linear-gradient(to bottom right, #fc7e6b, #01ebe5);
  overflow: hidden;
}
</style>
<script>

import { UserFilled,Lock} from "@element-plus/icons";
import request from "@/utils/request";
import {resetRouter, setRoutes} from "@/router";
export default {
  name: "Login",
  components: {
    UserFilled,
    Lock
  },
  data() {
    return{
      form: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/user/login", this.form).then( res => {
            if (res.code === '0'){
              this.$message({
                type: "success",
                message: "登录成功"
              })
              localStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
              localStorage.setItem("menus",JSON.stringify(res.data.menus)) //缓存用户信息
              this.$router.push("/") //登录成功之后进行页面的跳转

              //动态设置当前用户的路由
              setRoutes()
            }else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    },
    register() {
      this.$router.push("/register")
    }
  }
}
</script>

<style scoped>

</style>