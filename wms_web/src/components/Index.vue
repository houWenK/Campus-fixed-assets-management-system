<template>
  <el-container style="height: 100%; border: 1px solid #eee">
    <el-aside
      :width="aside_witdh"
      style="background-color: rgb(238, 241, 246); height: 100vh;"
    >
      <Aside :isCollapse="isCollapse"></Aside>
    </el-aside>

    <el-container style="height: 100%">
      <el-header style="text-align: right; font-size: 12px height: 100%;">
        <Header @doCollapse="doCollapse" :icon="icon"></Header>
      </el-header>
      <el-main style="height: 100%">
          <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import Aside from "./Aside.vue";
import Header from "./Header.vue";
export default {
  components: { Aside, Header},
  name: "Index",
  data() {
    return {
      isCollapse: false,
      aside_witdh: "200px",
      icon: "el-icon-s-fold"
    };
  },
  methods: {
    doCollapse() {
      this.isCollapse = !this.isCollapse;
      if (!this.isCollapse) {
        //展开
        this.aside_witdh = "200px";
        this.icon = "el-icon-s-fold";
      } else {
        //关闭
        this.aside_witdh = "64px";
        this.icon = "el-icon-s-unfold";
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}
</style>
