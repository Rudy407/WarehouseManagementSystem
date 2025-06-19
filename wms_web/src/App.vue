<template>
  <div id="app">
    <IndexPage></IndexPage>
  </div>
</template>

<script>

import IndexPage from "@/components/IndexPage.vue";

export default {
  name: 'App',
  components: {
    IndexPage
  }
}
</script>

<style>
#app {
  margin: 0;
  padding: 0;
  height: 100%;
}
</style>
