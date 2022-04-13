<template>
  <div style="height: 50px;line-height: 50px; border-bottom: 1px solid #ccc;display: flex">
    <div style="width: 200px;padding-left: 30px;font-weight:bold;color:royalblue ">后台管理</div>
    <div style="flex: 1"></div>
    <div style="width: 100px;cursor: pointer;color: var(--el-color-primary);display: flex;align-items: center;">
      <el-dropdown prefix-icon="ArrowDown">
        <span class="el-dropdown-link">
          {{ user.nickName }}
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="Information">个人信息</el-dropdown-item>
            <el-dropdown-item @click="quit">退出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
        <el-icon><arrow-down-bold/></el-icon>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {ArrowDownBold} from "@element-plus/icons-vue";
import router from "@/router";
export default {
  name: "Header",
  props: {
    user: Object
  },
  components: {
    ArrowDownBold
  },
  created() {
    this.load()
  },
  data() {
    return {
    }
  },
  methods: {
    quit() {
      this.$router.push("/")
      localStorage.clear()
    },
    load() {
      if (localStorage.getItem("user")==null){
        return
      }
      //console.log(user)
      // this.user.name = JSON.parse(localStorage.getItem("user")).nickname
    },
    Information(){
      this.$router.push("/person")
    }
  }
}
</script>

<style scoped>

</style>