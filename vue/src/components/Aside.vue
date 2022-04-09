<template>
  <div>
    <el-menu
        style="width: 200px;width: 100%;min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-sub-menu index="1">
        <template #title>
          <span>系统管理</span>
        </template>
          <el-menu-item index="/user">用户管理</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/floor">
        <span>宿舍楼管理</span>
      </el-menu-item>
      <el-menu-item index="3">
        <span>选项三</span>
      </el-menu-item>
      <el-menu-item index="4">
        <span>选项四</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data(){
    return{
      path: this.$route.path  //默认的高亮设置
    }
  },
  created() {
  }
}
</script>

<style scoped>
</style>