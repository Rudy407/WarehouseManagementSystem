<script>


export default {
  name: "HeaderOption",
  data(){
    return{
      user: JSON.parse((sessionStorage.getItem("CurUser")))
    }
  },
  props:{
    icon:String
  },
  methods:{
    toUser(){
      console.log("to user");
    },
    logout(){
      console.log("log out");
      this.$confirm('您确定要退出吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '退出登录成功!'
        });
        this.$router.push("/");
        sessionStorage.clear();
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消退出登录'
        });
      });

    },
    collapse(){
      this.$emit("doCollapse");
    }
  }
}
</script>

<template>
  <div style="display: flex;line-height: 60px" >
    <div style="line-height: 60px;font-size: 30px">
      <i :class="icon" @click="collapse" style="cursor: pointer"></i>
    </div>
    <div style="flex: 1;text-align: center;font-size: 30px">Warehouse</div>
    <span>{{user.name}}</span>
    <el-dropdown trigger="click" style="cursor: pointer">
      <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

  </div>
</template>

<style scoped>

</style>