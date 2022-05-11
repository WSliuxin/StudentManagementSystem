<template>
<div>
  <el-card class="box-card" style="width:90%;height: calc(100vh - 200px) ;margin: 20px 50px;">
    <div style="height: 50px">申请请假</div>
    <el-form label-position="right" ref="form" :model="form" style="margin: 0 auto;width: 30%" :rules="rules">
      <el-form-item prop="password">
        <span style="width: 80px">学号：</span>
        <el-input v-model="form.studentId" style="width: 50%" disabled/>
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">姓名：</span>
        <el-input v-model="form.name" style="width: 50%" disabled/>
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">宿舍楼：</span>
        <el-input v-model="form.floorName" style="width: 50%" disabled/>
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">寝室号：</span>
        <el-input v-model="form.bedroom" style="width: 40%" disabled/>
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">开始时间：</span>
        <el-date-picker
            v-model="user.startTime"
            type="datetime"
            placeholder="开始时间"
            value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">结束时间：</span>
        <el-date-picker
            v-model="user.endTime"
            type="datetime"
            placeholder="结束时间"
            value-format="YYYY-MM-DD HH:mm"
        />
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">请假原因：</span>
        <el-input v-model="user.reason" style="width: 50%" type="textarea" />
      </el-form-item>
      <el-form-item prop="password">
        <span style="width: 80px">审批人：</span>
        <el-select clearable v-model="user.string" placeholder="请选择">
          <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.id+'_'+item.name" >
            {{item.name}}
          </el-option>
        </el-select>
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
  name: "Application",
  data() {
    return {
      form: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      user: {},
      options: {},
    }
  },
  created() {
    request.get("/dormitory/findAll").then(res => {
      this.options = res.data
    })
  },
  methods: {
    update() {
      this.user.studentId = this.form.studentId
      request.put("/leave/update",this.user).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "添加成功"
          })
          this.$router.push("/home")
        }else {
          this.$message({
            type: "warning",
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
