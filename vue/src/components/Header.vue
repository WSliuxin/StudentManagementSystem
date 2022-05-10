<template>
  <div style="height: 70px;line-height: 70px; border-bottom: 2px solid #a7b1b1;display: flex">
    <div style="flex: 1">
      <span style="cursor: pointer;font-size: 20px" @click="collapse"></span>
      <el-breadcrumb separator="/" style="display:inline-block; margin-left: 20px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div style="width: 100px;cursor: pointer;color: var(--el-color-primary);display: flex;align-items: center;">
      <el-dropdown prefix-icon="ArrowDown">
        <span class="el-dropdown-link">{{ user.nickName }}
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
import router, {resetRouter} from "@/router";
export default {
  name: "Header",
  props: {
    user: Object
  },
  watch: {
    currentPathName(newVal,oldval) {
      console.log(newVal)
    },

  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;
    }
  },
  components: {
    ArrowDownBold,

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
      localStorage.clear()
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      router.push('/login')
      resetRouter()
    },
    load() {
      if (localStorage.getItem("user")==null){

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
