<template>
  <div>
    <el-menu
        style="width: 200px;width: 100%;min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-menu-item index="/home">
        <span>主页</span>
      </el-menu-item>
      <el-sub-menu index="1" v-if="user.role === 1">
        <template #title>
          <span>系统管理</span>
        </template>
        <el-menu-item index="/user">用户管理</el-menu-item>
        <el-menu-item index="/role">角色管理</el-menu-item>
        <el-menu-item index="/menu">菜单管理</el-menu-item>
        <el-menu-item index="/file">文件管理</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="2" v-if="user.role === 1">
        <template #title>
          <span>宿舍管理</span>
        </template>
        <el-menu-item index="/floor">宿舍楼管理</el-menu-item>
        <el-menu-item index="/announcement">宿舍公告管理</el-menu-item>
      </el-sub-menu>
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
    let userStr = localStorage.getItem("user") || {}
    this.user = JSON.parse(userStr)

    //请求服务器，确认当前登录用户的合法信息
    // request.get("/user/"+this.user.id).then( res => {
    //   if (res.code === '0') {
    //     this.user = res.data
    //   }
    // })

  }
}
</script>

<style scoped>
</style>