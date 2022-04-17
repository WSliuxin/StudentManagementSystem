<template>
  <div>
    <el-menu
        style="width: 200px;min-height: calc(100vh - 50px)"
        :default-active="path"
        :default-openeds="opens"
        router
        class="el-menu-vertical-demo">
        <div v-for="item in menus" :key="item.id">
          <div v-if="item.path">
            <el-menu-item :index="item.path">
              <span><component :is="item.icon" style="width: 56px; height:56px;padding:15px" />{{ item.name }}</span>
            </el-menu-item>
          </div>

          <div v-else>
            <el-sub-menu :index="item.id + '' ">
              <template #title>
                <span><component :is="item.icon" style="width: 56px; height:56px;padding:15px" />{{ item.name }}</span>
              </template>
              <div v-for="subItem in item.children" :key="subItem.id">
                <el-menu-item :index="subItem.path">
                  <span><component :is="subItem.icon" style="width: 56px; height:56px;padding:15px" />{{ subItem.name }}</span>
                </el-menu-item>
              </div>
            </el-sub-menu>
          </div>
        </div>
    </el-menu>
  </div>
</template>

<script>
import request from "@/utils/request";
import {Menu as IconMenu,} from '@element-plus/icons-vue'
export default {
  name: "Aside",
  components: {IconMenu},
  data(){
    return{
      user: {},
      path: this.$route.path,  //默认的高亮设置
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : [],
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