<template>
  <div class="wrapper" style="width: 100%; height: 100vh;background-color: darkslateblue; overflow: hidden">
    <div style="width: 360px;height: 520px; margin-top: 100px;margin-left:1035px;background-color: #ffffff;padding: 20px;border-radius: 12px">
      <div class="wrapper2" style="color: #161515;font-size: 30px;text-align: center;padding: 30px 0">登录</div>
      <el-form style="margin: 0 20px;width: 80%" ref="form" :model="form" size="normal" :rules="rules">
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
        <el-form-item label="角色：" prop="role">
          <el-radio v-model="form.role" label="管理员">管理员</el-radio>
          <el-radio v-model="form.role" label="宿舍管理员">宿舍管理员</el-radio>
          <el-radio v-model="form.role" label="学生">学生</el-radio>
        </el-form-item>
          <el-row :span="24">
            <el-col :span="12">
              <el-input v-model="formLogin.code" auto-complete="off" placeholder="请输入验证码" prop="code"/>
            </el-col>
            <el-col :span="11" style="padding-left: 30px">
              <div class="login-code" width="100%" @click="refreshCode">
                <!--验证码组件-->
                <s-identify :identifyCode="identifyCode"></s-identify>
              </div>
            </el-col>
          </el-row>
        <div style="text-align: center;margin-top: 30px">
          <el-button type="primary" @click="login">登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<style>
.wrapper {
  background-image: url("../assets/背景.png");
  overflow: hidden;
  background-repeat:no-repeat ;
  background-size:100% 100%;
}
</style>
<script>

import { UserFilled,Lock} from "@element-plus/icons";
import request from "@/utils/request";
import {resetRouter, setRoutes} from "@/router";
import SIdentify from "@/views/SIdentify";
export default {
  name: "Login",
  components: {
    UserFilled,
    Lock,
    SIdentify,
  },
  data() {
    return{
      form: {},
      formLogin: {
        code: "",
      },
      identifyCodes: '1234567890abcdefjhijklinopqrsduvwxyz',//随机串内容
      identifyCode: '',
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
        role: [
          { required: true, methods: '请选择登录角色',triggered: 'blur'},
        ]
      }
    }
  },
  mounted() {
    this.identifyCode = ''
    this.makeCode(this.identifyCodes,4)
  },
  methods: {
    login() {
      if (this.formLogin.code.toLowerCase() !== this.identifyCode.toLowerCase()) {
        this.$message.error('请填写正确的验证码')
        this.refreshCode()
        return
      }
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.form.role === "宿舍管理员") {
            this.form.nickName = this.form.username
            request.post("/dormitory/login", this.form).then( res => {
              if (res.code === '0'){
                this.$message({
                  type: "success",
                  message: "登录成功"
                })
                localStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
                localStorage.setItem("menus",JSON.stringify(res.data.menus)) //缓存用户信息

                //动态设置当前用户的路由
                setRoutes()
                this.$router.push("/") //登录成功之后进行页面的跳转



              }else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }else if (this.form.role === "管理员"){
            request.post("/user/login", this.form).then( res => {
              if (res.code === '0'){
                this.$message({
                  type: "success",
                  message: "登录成功"
                })
                localStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
                localStorage.setItem("menus",JSON.stringify(res.data.menus)) //缓存用户信息
                //动态设置当前用户的路由
                setRoutes()
                this.$router.push("/") //登录成功之后进行页面的跳转


              }else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }else if (this.form.role === "学生") {
            this.form.studentId = this.form.username
            console.log(this.form.studentId)
            request.post("/student/login", this.form).then( res => {
              if (res.code === '0'){
                this.$message({
                  type: "success",
                  message: "登录成功"
                })
                localStorage.setItem("user",JSON.stringify(res.data)) //缓存用户信息
                localStorage.setItem("menus",JSON.stringify(res.data.menus)) //缓存用户信息
                //动态设置当前用户的路由
                setRoutes()
                this.$router.push("/") //登录成功之后进行页面的跳转

              }else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            })
          }
        }
      })
    },
    register() {
      this.$router.push("/register")
    },
    // 重置验证码
    refreshCode () {
      this.identifyCode = ''
      this.makeCode(this.identifyCodes, 4)
    },
    makeCode (o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[this.randomNum(0, this.identifyCodes.length)]
      }
    },
    randomNum (min, max) {
      return Math.floor(Math.random() * (max - min) + min)
    },
  }
}
</script>

<style scoped>

</style>