<script>
export default {
  name: "AsideOption",
  data() {
    return {}
  },
  computed: {
    "menu": {
      get() {
        return this.$store.state.menu;
      }
    }
  },
  props: {
    isCollapse: Boolean
  },
  methods: {}
}
</script>

<template>
  <el-menu default-active="/index/home" :collapse="isCollapse" :collapse-transition="false" router>
    <el-menu-item index="/index/home">
      <i class="el-icon-s-home"></i>
      <span slot="title">首页</span>
    </el-menu-item>
    <el-menu-item :index="'/index/'+item.menuclick" v-for="(item,i) in menu" :key="i">
      <i :class="item.menuicon"></i>
      <span slot="title">{{ item.menuname }}</span>
    </el-menu-item>
  </el-menu>
</template>

<style scoped>

</style>