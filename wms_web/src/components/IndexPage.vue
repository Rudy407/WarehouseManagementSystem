<script>
import AsideOption from "@/components/AsideOption.vue";
import HeaderOption from "@/components/HeaderOption.vue";
import MainOption from "@/components/MainOption.vue";

export default {
  name: "IndexPage",
  components: {MainOption, HeaderOption, AsideOption},
  data(){
    return{
      isCollapse:false,
      asideWidth:'200px',
      asideIcon:'el-icon-s-fold'
    };
  },
  methods:{
    doCollapse(){
      this.isCollapse=!this.isCollapse;
      if(this.isCollapse){
        this.asideWidth='64px';
        this.asideIcon='el-icon-s-unfold';
      }
      else{
        this.asideWidth='200px';
        this.asideIcon='el-icon-s-fold';
      }
    }
  }
}
</script>

<template>
  <el-container style="height: 100vh; border: 1px solid #eee">
    <el-aside :width="asideWidth" style="background-color: #ffffff">
      <AsideOption :isCollapse="isCollapse"></AsideOption>
    </el-aside>

    <el-container>
      <el-header style="text-align: right; font-size: 12px">
        <HeaderOption @doCollapse="doCollapse" :icon="asideIcon"></HeaderOption>
      </el-header>

      <el-main>
        <MainOption></MainOption>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
  .el-header {
    color: #333;
    line-height: 60px;
  }

  .el-aside {
    color: #333;
  }
</style>

