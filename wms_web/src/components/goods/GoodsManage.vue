<script>
export default {
  name: "GoodsManage",
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('数量输⼊过⼤'));
      }else{
        callback();
      }
    };
    let checkDuplicate =(rule,value,callback)=>{
      if(this.form.name){
        return callback();
      }
      if(this.isEdit){
        return callback();
      }
      this.$axios.get(this.$httpUrl+"/goods/findByName?name="+this.form.name).then(res=>res.data).then(res=>{
        if(res.code==200){
          callback(new Error('物品已经存在'))
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
      goodstype:null,
      count:null,
      storage:null,
      remark:"",
      centerDialogVisible: false,
      form:{
        name: "",
        goodstype:null,
        count:null,
        storage:null,
        remark:""
      },
      isEdit:false,
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        goodstype: [
          {required: true, message: '请输入物品类型', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
        storage: [
          {required: true, message: '请输入仓库名', trigger: 'blur'},
          {validator:checkDuplicate,trigger: 'blur'}
        ],
      }
    }
  },
  methods:{
    loadGet(){
      this.$axios.get(this.$httpUrl+'/goods/list').then(res=>res.data).then(res=>{
        console.log(res);
        this.tableData=res;
      })
    },
    loadPost(){
      this.$axios.post(this.$httpUrl+'/goods/listPageC',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          name:this.name,
          goodstype:this.goodstype,
          storage:this.storage
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
      this.goodstype=null;
      this.storage=null;
      this.count=null;
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
        goodstype: scope.goodstype,
        storage: scope.storage,
        count: scope.count,
        remark: scope.remark
      };
      this.centerDialogVisible=true;
      this.isEdit=true;
    },
    del(scope){
      this.form={id: scope.id};
      this.$axios.post(this.$httpUrl+'/goods/delete',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.$message({
            message:"删除物品成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"删除物品失败",
            type:"error"
          })
        }
      })
    },
    doMod(){
      this.$axios.post(this.$httpUrl+'/goods/update',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"编辑物品成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"编辑物品失败",
            type:"error"
          })
        }
      })
    },
    doSave(){
      this.$axios.post(this.$httpUrl+'/goods/save',this.form).then(res=>res.data).then(res=> {
        if (res.code == 200) {
          this.centerDialogVisible=false;
          this.$message({
            message:"新建物品成功",
            type:"success"
          })
          this.loadPost();
        } else {
          this.$message({
            message:"新建物品失败",
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
        goodstype: null,
        storage: null,
        count: null,
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
      <el-input v-model="name" placeholder="请输入物品名" style="width: 200px;margin-bottom: 5px" @keyup.enter.native="loadPost"></el-input>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetQuery">重置</el-button>
      <el-button type="success" @click="add">新增</el-button>
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border>
      <el-table-column prop="id" label="Id" align="center">
      </el-table-column>
      <el-table-column prop="name" label="物品名" align="center">
      </el-table-column>
      <el-table-column prop="goodstype" label="物品类型" align="center">
      </el-table-column>
      <el-table-column prop="count" label="数量" align="center">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" align="center">
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
        title="物品"
        :visible.sync="centerDialogVisible"
        width="30%"
        center
        @close="dialogClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="物品名" prop="name">
          <el-col :span="20">
            <el-input v-model="form.name" :disabled="isEdit"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="物品类型" prop="goodstype">
          <el-col :span="20">
            <el-input v-model="form.goodstype"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="物品数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-input v-model="form.storage"></el-input>
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