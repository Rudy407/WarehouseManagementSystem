<script>
export default {
  name: "AdminManage",
  data() {
    let checkAge = (rule, value, callback) => {
      if(value>150){
        callback(new Error('年龄输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.id){
        return callback();
      }
      if(this.isEdit){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/user/findByNo?no="+this.form.no).then(res=>res.data).then(res=>{
        if(res.code==200){
          callback(new Error('账号已经存在'))
        }else{
          callback();
        }
      })
    };
    return {
      tableData: [],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      name: "",
      sex: null,
      sexs:[{
        value: 1,
        label: '男'
      }, {
        value: 0,
        label: '女'
      }],
      centerDialogVisible: false,
      form:{
        no:"",
        name:"",
        sex:1,
        age:null,
        roleId:1,
        phone:"",
        password:""
      },
      isEdit:false,
      rules: {
        no: [
          {required: true, message: '请输入账号', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'}
        ],
        age: [
          {required: true, message: '年龄', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,3}$/,message: '年龄必须为正整数字',trigger: "blur"},
          {validator:checkAge,trigger: 'blur'}
        ],
        phone: [
          {required: true,message: "⼿机号不能为空",trigger: "blur"},
          {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输⼊正确的⼿机号码", trigger:
                "blur"}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    loadGet(){
      this.$axios.get(this.$httpUrl+'/user/list').then(res=>res.data).then(res=>{
        console.log(res);
        this.tableData=res;
      })
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/user/listPageC',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          name:this.name,
          sex:this.sex,
          roleId:1
        }
      },).then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){
          this.tableData=res.data;
          this.total=res.total;
        }
        else{
          alert('获取数据失败');
        }
      })
    },
    resetQuery(){
      this.name='';
      this.roleId=1;
      this.sex=null;
      this.loadPost();
    },
    add(){
      this.centerDialogVisible=true;
      this.isEdit=false;
    },
    mod(scope){
      this.form = {
        id: scope.id,
        no: scope.no,
        name: scope.name,
        sex: scope.sex,
        age: scope.age,
        roleId: scope.roleId,
        phone: scope.phone,
        password: scope.password
      };
      this.centerDialogVisible=true;
      this.isEdit=true;
    },
    del(scope){
      this.form={id: scope.id};
      this.$axios.post(this.$httpUrl+'/user/delete',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.$message({
            message:"删除用户成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"删除用户失败",
            type:"error"
          })
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/user/update',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"编辑用户成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"编辑用户失败",
            type:"error"
          })
        }
      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/user/save',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"新建用户成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"新建用户失败",
            type:"error"
          })
        }
      })
    },
    save(){
      this.$refs.form.validate((valid) => {
        if (valid) {
          if(this.isEdit){
            this.doMod();
          }
          else{
            this.doSave();
          }
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    dialogClose(){
      this.form = {
        no: "",
        name: "",
        sex: 1,
        age: null,
        roleId: 1,
        phone: "",
        password: ""
      };
      if (this.$refs.form) {
        this.$refs.form.resetFields();
      }
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.pageNum=1;
      this.pageSize=val;
      this.loadPost();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.pageNum=val;
      this.loadPost();
    }
  },
  beforeMount() {
    this.loadPost();
  }
}
</script>

<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="请输入名字" style="width: 200px;margin-bottom: 5px" @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="sex" filterable placeholder="请选择性别" style="margin-left: 5px">
        <el-option
            v-for="item in sexs"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetQuery">重置</el-button>
      <el-button type="success" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border>
      <el-table-column prop="id" label="Id" width="100" align="center">
      </el-table-column>
      <el-table-column prop="no" label="账号" width="150" align="center">
      </el-table-column>
      <el-table-column prop="name" label="姓名" width="150" align="center">
      </el-table-column>
      <el-table-column prop="sex" label="性别" width="120" align="center">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.sex === 1 ? 'primary' : 'success'"
              disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" width="120" align="center">
      </el-table-column>
      <el-table-column prop="roleId" label="权限" width="150" align="center">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话号码" width="180" align="center">
      </el-table-column>
      <el-table-column prop="password" label="密码" align="center">
      </el-table-column>
      <el-table-column prop="operate" label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="mod(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="del(scope.row)" style="margin-left: 5px">
            <el-button type="danger" slot="reference" size="small">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[1, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
    >
    </el-pagination>
    <el-dialog
        title="管理员"
        :visible.sync="centerDialogVisible"
        width="30%"
        center
        @close="dialogClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="账号" prop="no">
          <el-col :span="20">
            <el-input v-model="form.no" :disabled="isEdit"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio :label="1">男</el-radio>
            <el-radio :label="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-col :span="20">
            <el-input v-model="form.age"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="电话号码" prop="phone">
          <el-col :span="20">
            <el-input v-model="form.phone"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-col :span="20">
            <el-input v-model="form.password"></el-input>
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

<style scoped>

</style>