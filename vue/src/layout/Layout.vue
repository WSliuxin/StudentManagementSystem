<template>
  <div>
    <!--    头部-->
    <Header :user="user" />
    <!--    主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <Aside />
      <!--      内容区域-->
      <router-view style="flex: 1" @refreshUser="getUser" :user="user"/>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Aside from "@/components/Aside";
import request from "@/utils/request";
export default {
  name: "layout",
  components: {
    Header,
    Aside
  },
  data(){
    return {
      user: {}
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      let id = JSON.parse(localStorage.getItem("user")) ? JSON.parse(localStorage.getItem("user")).id : ""
      request.get("/user/username/" + id).then(res => {
        this.user = res.data
      })
    }
  }
}
</script>

<style scoped>

</style>