<script>
export default {
  name: "StorageManage",
  data() {
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.name){
        return callback();
      }
      if(this.isEdit){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/storage/findByName?name="+this.form.name).then(res=>res.data).then(res=>{
        if(res.code==200){
          callback(new Error('仓库已经存在'))
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
      remark:"",
      centerDialogVisible: false,
      form:{
        name:"",
        remark:""
        
      },
      isEdit:false,
      rules: {
        name: [
          {required: true, message: '请输入仓库名', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    loadGet(){
      this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
        console.log(res);
        this.tableData=res;
      })
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/storage/listPageC',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          name:this.name,
          remark:this.remark
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
      this.remark='';
      this.loadPost();
    },
    add(){
      this.centerDialogVisible=true;
      this.isEdit=false;
    },
    mod(scope){
      this.form = {
        id: scope.id,
        name: scope.name,
        remark: scope.remark
      };
      this.centerDialogVisible=true;
      this.isEdit=true;
    },
    del(scope){
      this.form={id: scope.id};
      this.$axios.post(this.$httpUrl+'/storage/delete',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.$message({
            message:"删除仓库成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"删除仓库失败",
            type:"error"
          })
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/storage/update',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"编辑仓库成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"编辑仓库失败",
            type:"error"
          })
        }
      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/storage/save',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"新建仓库成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"新建仓库失败",
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
        name: "",
        remark: ""
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
      <el-input v-model="name" placeholder="请输入仓库名" style="width: 200px;margin-bottom: 5px" @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetQuery">重置</el-button>
      <el-button type="success" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border>
      <el-table-column prop="id" label="Id" align="center">
      </el-table-column>
      <el-table-column prop="name" label="仓库名" align="center">
      </el-table-column>
      <el-table-column prop="remark" label="备注" align="center">
      </el-table-column>
      <el-table-column prop="operate" label="操作" align="center">
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
        title="仓库"
        :visible.sync="centerDialogVisible"
        width="30%"
        center
        @close="dialogClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="仓库名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name" :disabled="isEdit"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input v-model="form.remark"></el-input>
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