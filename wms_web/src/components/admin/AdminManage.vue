<template>
    <div>
      <div style="margin-bottom: 10px">
        <el-input
          v-model="name"
          placeholder="请输入名字"
          style="width: 200px"
          suffix-icon="el-icon-search"
          @keyup.enter.native="loadPost"
        ></el-input>
        <el-select
          v-model="sex"
          filterable
          placeholder="请选择性别"
          style="margin-left: 7px"
        >
          <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
        <el-button type="primary" style="margin-left: 7px" @click="loadPost"
          >查询</el-button
        >
        <el-button type="success" @click="resetParam">重置</el-button>
        <el-button type="primary" style="margin-left: 7px" @click="add"
          >新增</el-button
        >
      </div>
      <el-table
        :data="tableData"
        :header-cell-style="{ background: '#f2f5fc', color: '#555' }"
        border
      >
        <el-table-column prop="id" label="ID" width="60"> </el-table-column>
        <el-table-column prop="no" label="账号" width="180"> </el-table-column>
        <el-table-column prop="name" label="姓名" width="180"> </el-table-column>
        <el-table-column prop="age" label="年龄" width="80"> </el-table-column>
        <el-table-column prop="sex" label="性别" width="80">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.sex === 1 ? 'primary' : 'success'"
              disable-transitions
              >{{ scope.row.sex === 1 ? "男" : "女" }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="roleId" label="角色" width="120">
          <template slot-scope="scope">
            <el-tag
              :type="
                scope.row.roleId === 0
                  ? 'danger'
                  : scope.row.roleId === 1
                  ? 'primary'
                  : 'success'
              "
              disable-transitions
              >{{
                scope.row.roleId === 0
                  ? "超级管理员"
                  : scope.row.roleId === 1
                  ? "管理员"
                  : "用户"
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="电话" width="180"> </el-table-column>
        <el-table-column prop="operate" label="操作">
          <template slot-scope="scope">
          <el-button type="success" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm
            title="确定删除吗？"
            @confirm="del(scope.row.id)"
            style="margin-left: 7px;"
            >
      <el-button slot="reference" type="danger" size="small" >删除</el-button>
            </el-popconfirm>
   
        </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
      <el-dialog
        title="提示"
        :visible.sync="centerDialogVisible"
        width="30%"
        center
        :before-close="handleClose"
      >
        <el-form ref="form" :model="form" label-width="80px" :rules="rules">
          <el-form-item label="账号" prop="no">
            <el-col :span="17">
              <el-input v-model="form.no"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-col :span="17">
              <el-input v-model="form.password"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-col :span="17">
              <el-input v-model="form.name"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <el-radio-group v-model="form.sex">
              <el-radio label="1">男</el-radio>
              <el-radio label="0">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-col :span="17">
              <el-input v-model="form.age"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="电话" prop="phone">
            <el-col :span="17">
              <el-input v-model="form.phone"></el-input>
            </el-col>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="centerDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </template>
  <script>
  export default {
    name: "AdminManage",
    data() {
      let checkAge = (rule, value, callback) => {
        if (value > 150) {
          callback(new Error("年龄输入过大"));
        } else {
          callback();
        }
      };
      let checkDuplicate=(rule, value, callback) => {
        if (this.form.id) {
          callback();
        } 
        this.$axios.get(this.$httpUrl + "/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{ 
           if(res.code!=200){
              callback()
            }else{
                callback(new Error('账号已存在'));
            }}
        )
      };
      return {
        tableData: [],
        pageSize: 10,
        pageNum: 1,
        total: 0,
        name: "",
        sex: "",
        sexs: [
          {
            value: "1",
            label: "男",
          },
          {
            value: "0",
            label: "女",
          },
        ],
        centerDialogVisible: false,
        form: {
          id:"",
          no: "",
          name: "",
          password: "",
          age: "",
          phone: "",
          sex: "0",
          roleId:'1'
        },
        rules: {
          no: [
            { required: true, message: "请输入账号", trigger: "blur" },
            { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
            { validator: checkDuplicate, trigger: "blur" },
          ],
          name: [{ required: true, message: "请输入名字", trigger: "blur" }],
          password: [
            { required: true, message: "请输入密码", trigger: "blur" },
            { min: 3, max: 8, message: "长度在 3 到 8 个字符", trigger: "blur" },
          ],
          age: [
            { required: true, message: "请输入年龄", trigger: "blur" },
            { min: 1, max: 3, message: "长度在 1 到 3 位", trigger: "blur" },
            {
              pattern: /^([1-9][0-9]*){1,3}$/,
              message: "年龄必须为正整数",
              trigger: "blur",
            },
            { validator: checkAge, trigger: "blur" },
          ],
          phone: [
            { required: true, message: "请输入手机号", trigger: "blur" },
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              message: "手机号不存在",
              trigger: "blur",
            },
          ],
        },
      };
    },
    methods: {
      resetForm() {
        this.$refs.form.resetFields();
      },
      mod(row){
          this.centerDialogVisible = true;
          this.$nextTick(() => {
            this.form.id=row.id
          this.form.no=row.no
          this.form.name=row.name
          this.form.password=''
          this.form.age=row.age+''
          this.form.sex=row.sex+''
          this.form.phone=row.phone
          this.form.roleId=row.roleId
        });
      },
      del(id){
        this.$axios
              .get(this.$httpUrl + "/user/delete?id="+id)
              .then((res) => res.data)
              .then((res) => {
                if (res.code == 200) {
                  this.$message({
                    message: "操作成功！",
                    type: "success",
                  });
                  this.loadPost();
                } else {
                  this.$message({
                    message: "操作失败！",
                    type: "error",
                  });
                }
              });
  
      },
      save() {
        this.$refs.form.validate((valid) => {
          if (valid) {
            this.$axios
              .post(this.$httpUrl + "/user/saveOrMod", this.form)
              .then((res) => res.data)
              .then((res) => {
                if (res.code == 200) {
                  this.$message({
                    message: "操作成功！",
                    type: "success",
                  });
                  this.centerDialogVisible = false;
                  this.loadPost();
                } else {
                  this.$message({
                    message: "操作失败！",
                    type: "error",
                  });
                }
              });
          } else {
            console.log("error submit!!");
            return false;
          }
        });
      },
      add() {
        this.centerDialogVisible = true;
        this.$nextTick(() => {
          this.resetForm();
          this.form.id = ''
        });
      },
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageNum = 1;
        this.pageSize = val;
        this.loadPost();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageNum = val;
        this.loadPost();
      },
      loadGet() {
        this.$axios
          .get(this.$httpUrl + "/user/list")
          .then((res) => res.data)
          .then((res) => {
            console.log(res);
          });
      },
      loadPost() {
        this.$axios
          .post(this.$httpUrl + "/user/listPage", {
            pageSize: this.pageSize,
            pageNum: this.pageNum,
            param: {
              name: this.name,
              sex: this.sex,
              roleId:'1'
            },
          })
          .then((res) => res.data)
          .then((res) => {
            if (res.code == 200) {
              this.tableData = res.data;
              this.total = res.total;
            } else alert("获取数据失败");
          });
      },
      resetParam() {
        this.name = "";
        this.sex = "";
      },
      handleClose(done) {
        this.$confirm("确认关闭？")
          .then((_) => {
            done();
          })
          .catch((_) => {});
      },
    },
    beforeMount() {
      // this.loadGet();
      this.loadPost();
    },
  };
  </script>
  