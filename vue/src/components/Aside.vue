<template>
  <div>

    <el-menu
        style="width: 200px;min-height: 100vh"
        active-text-color="#ffd04b"
        background-color="#2E3336FF"
        text-color="#b7b7c1"
        :default-active="path"
        :default-openeds="opens"
        :collapse-transition="false"
        router
        class="el-menu-vertical-demo">
      <div style="height: 70px;color: #fff;font-size: 25px;
      text-align: center;line-height: 70px;
      background-color: #25292a;border-bottom: 2px solid #000;" ><b>宿舍管理</b></div>
        <div v-for="item in menus" :key="item.id">
          <div v-if="item.path">
            <el-menu-item :index="item.path">
              <span><component :is="item.icon" style="width: 40px; height:56px;padding:20px 10px" />{{ item.name }}</span>
            </el-menu-item>
          </div>

          <div v-else>
            <el-sub-menu :index="item.id + '' ">
              <template #title>
                <span><component :is="item.icon" style="width: 40px; height:56px;padding:20px 10px" />{{ item.name }}</span>
              </template>
              <div v-for="subItem in item.children" :key="subItem.id">
                <el-menu-item :index="subItem.path">
                  <span><component :is="subItem.icon" style="width: 40px; height:56px;padding:20px 10px" />{{ subItem.name }}</span>
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