<template>
  <div>
    <el-menu
        style="width: 200px;width: 100%;min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-sub-menu index="1" v-if="user.role === 1">
        <template #title>
          <span>系统管理</span>
        </template>
          <el-menu-item index="/user">用户管理</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/floor">
        <span>宿舍楼管理</span>
      </el-menu-item>
      <el-menu-item index="/announcement">
        <span>宿舍公告管理</span>
      </el-menu-item>
      <el-menu-item index="4">
        <span>选项四</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Aside",
  data(){
    return{
      user: {},
      path: this.$route.path  //默认的高亮设置
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || {}
    this.user = JSON.parse(userStr)

    //请求服务器，确认当前登录用户的合法信息
    request.get("/user/"+this.user.id).then( res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })

  }
}
</script>

<style scoped>
</style>