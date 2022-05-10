<template>
  <div>

    <!--    主体-->
    <div style="display: flex">
      <!--      侧边栏-->
      <Aside />
      <div style="width: 100%;">
        <!--    头部-->
        <Header :user="user" />
        <!--      内容区域-->
        <router-view style="flex: 1" @refreshUser="getUser" :user="user"/>
      </div>


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
      user: {},
      list: {}
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      let id = JSON.parse(localStorage.getItem("user")) ? JSON.parse(localStorage.getItem("user")).id : ""
      let list = JSON.parse(localStorage.getItem("user")) ? JSON.parse(localStorage.getItem("user")) : ""
      switch (list.role) {
        case "ROLE_ADMIN" :
          request.get("/user/" + id).then(res => {
            this.user = res.data
            this.user.nickName = this.user.username
          })
              break
        case "ROLE_USER":
          request.get("/student/username/" + id).then(res => {
            this.user = res.data
            this.user.nickName = this.user.name
          })
          break
        case "ROLE_TUBES":
          request.get("/dormitory/username/" + id).then(res => {
            this.user = res.data
          })
          break
      }

    }
  }
}
</script>

<style scoped>

</style>
