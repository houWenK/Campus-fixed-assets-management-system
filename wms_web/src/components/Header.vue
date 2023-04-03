<template>
  <div style="display: flex; line-height: 60px; justify-content: space-between">
    <div style="font-size: 20px" @click="collapse">
      <i :class="icon"></i>
    </div>
    <div style="text-align: center; font-size: 27px">
      <span>欢迎来到学校固定资产管理系统</span>
    </div>

    <el-dropdown>
      <span>{{ user.name }}</span>
      <i class="el-icon-arrow-down" style="margin-right: 5px"></i>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item @click.native="toUser">个人中心</el-dropdown-item>
        <el-dropdown-item @click.native="layOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>
<script>
export default {
  name: "Header",
  data(){
    return {
      user:JSON.parse(sessionStorage.getItem('CurUser'))
    }
  },
  props: {
    icon: String,
  },
  methods: {
    toUser() {
      this.$router.push("/Home")
    },
    layOut() {
      this.$confirm('您确定取消登录吗？','提示',{
        confirmButtonText:'确定',
        type:"warning",
        center:true,
      }).then(()=>{
            this.$message({
              type:'success',
              message:'退出登录成功'
            }) 
             this.$router.push('/')
      sessionStorage.clear()    
      }).catch(()=>
      {
        this.$message({
          type:'info',
          message:'已取消退出'
        })
      })
    
    },
    collapse() {
      this.$emit("doCollapse");
    },
  },
  created(){
    this.$router.push("/Home")
  }
};
</script>
