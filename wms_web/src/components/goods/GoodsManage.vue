<script>
import SelectUser from "@/components/user/SelectUser.vue";

export default {
  name: "GoodsManage",
  components: {SelectUser},
  data() {
    let checkCount = (rule, value, callback) => {
      if(value>9999){
        callback(new Error('数量输⼊过⼤'));
      }
      else if(this.inputForm.action==2&&value>this.currentRow.count){
        callback(new Error('仓库剩余库存不足'));
      }
      else{
        callback();
      }
    };
    return {
      tempUser:null,
      user: JSON.parse((sessionStorage.getItem("CurUser"))),
      storageData:[],
      goodstypeData:[],
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
      inputDialogVisible:false,
      innerVisible:false,
      currentRow:{},
      form:{
        name: "",
        goodstype:null,
        count:null,
        storage:null,
        remark:""
      },
      inputForm:{
        name: "",
        count:null,
        goods:null,
        username:"",
        adminId:null,
        userid:null,
        remark:"",
        action:null
      },
      isEdit:false,
      rules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        goodstype: [
          {required: true, message: '请选择物品类型', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
        storage: [
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
      },
      inputRules: {
        name: [
          {required: true, message: '请输入物品名', trigger: 'blur'},
        ],
        username: [
          {required: true, message: '请选择申请人', trigger: 'blur'},
        ],
        count: [
          {required: true, message: '请输⼊数量', trigger: 'blur'},
          {pattern: /^([1-9][0-9]*){1,4}$/,message: '数量必须为正整数字',trigger: "blur"},
          {validator:checkCount,trigger: 'blur'}
        ],
        storage: [
          {required: true, message: '请选择仓库', trigger: 'blur'},
        ],
      }
    }
  },
  methods:{
    formatterStorage(row){
      let temp=this.storageData.find(item=>{
        return item.id==row.storage;
      })
      return temp.name;
    },
    formatterType(row){
      let temp=this.goodstypeData.find(item=>{
        return item.id==row.goodstype;
      })
      return temp.name;
    },
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
    loadStorage(){
      this.$axios.get(this.$httpUrl+'/storage/list').then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){
          this.storageData=res.data;
          this.total=res.total;
        }
        else{
          alert('获取数据失败');
        }
      })
    },
    loadType(){
      this.$axios.get(this.$httpUrl+'/goodstype/list').then(res=>res.data).then(res=>{
        console.log(res);
        if(res.code==200){
          this.goodstypeData=res.data;
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
    input(){
      if(!this.currentRow.id){
        alert("请选择记录");
        return;
      }
      this.inputDialogVisible=true;
      this.inputForm.name=this.currentRow.name;
      this.inputForm.goods=this.currentRow.id;
      this.inputForm.adminId=this.user.id;
      this.inputForm.action=1;
    },
    output(){
      if(!this.currentRow.id){
        alert("请选择记录");
        return;
      }
      this.inputDialogVisible=true;
      this.inputForm.name=this.currentRow.name;
      this.inputForm.goods=this.currentRow.id;
      this.inputForm.adminId=this.user.id;
      this.inputForm.action=2;
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
    selectUser(){
      this.innerVisible=true;
    },
    doSelectUser(val){
      this.tempUser=val;
    },
    confirmUser(){
      this.inputForm.userid=this.tempUser.id;
      this.inputForm.username=this.tempUser.name;
      this.innerVisible=false;
    },
    inputSave(){
      this.$refs.inputForm.validate((valid) => {
        if (valid) {
          this.$axios.post(this.$httpUrl+'/record/save',this.inputForm).then(res=>res.data).then(res=> {
            if (res.code == 200) {
              this.inputDialogVisible=false;
              this.$message({
                message:"操作成功",
                type:"success"
              })
              this.loadPost();
            } else {
              this.$message({
                message:"操作失败",
                type:"error"
              })
            }
          })
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
    inputDialogClose(){
      this.inputForm = {
        name: "",
        count:null,
        adminId:null,
        userid:null,
        remark:""
      };
      if (this.$refs.inputForm) {
        this.$refs.inputForm.resetFields();
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
    },
    selectCurrentChange(val) {
      this.currentRow = val;
    }
  },
  beforeMount() {
    this.loadPost();
    this.loadStorage();
    this.loadType();
  }
}
</script>

<template>
  <div>
    <div>
      <el-input v-model="name" placeholder="请输入物品名" style="width: 200px;margin-bottom: 5px" @keyup.enter.native="loadPost"></el-input>
      <el-select v-model="goodstype" filterable placeholder="请选择物品类别" style="margin-left: 5px">
        <el-option
            v-for="item in goodstypeData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-select v-model="storage" filterable placeholder="请选择仓库" style="margin-left: 5px">
        <el-option
            v-for="item in storageData"
            :key="item.id"
            :label="item.name"
            :value="item.id">
        </el-option>
      </el-select>
      <el-button type="primary" style="margin-left: 10px" @click="loadPost">查询</el-button>
      <el-button type="danger" @click="resetQuery">重置</el-button>
      <el-button type="success" @click="add" v-if="user.roleId!=2">新增</el-button>
      <el-button type="primary" @click="input" v-if="user.roleId!=2">入库</el-button>
      <el-button type="primary" @click="output" v-if="user.roleId!=2">出库</el-button>
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border
              highlight-current-row
              @current-change="selectCurrentChange">
      <el-table-column prop="id" label="Id" align="center">
      </el-table-column>
      <el-table-column prop="name" label="物品名" align="center">
      </el-table-column>
      <el-table-column prop="goodstype" label="物品类型" align="center" :formatter="formatterType">
      </el-table-column>
      <el-table-column prop="storage" label="仓库" align="center" :formatter="formatterStorage">
      </el-table-column>
      <el-table-column prop="count" label="数量" align="center">
      </el-table-column>
      <el-table-column prop="remark" label="备注" align="center">
      </el-table-column>
      <el-table-column prop="operate" label="操作" align="center" v-if="user.roleId!=2">
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
            <el-select v-model="form.goodstype" filterable placeholder="请选择物品类别" style="width: 100%">
              <el-option
                  v-for="item in goodstypeData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="仓库" prop="storage">
          <el-col :span="20">
            <el-select v-model="form.storage" filterable placeholder="请选择仓库" style="width: 100%">
              <el-option
                  v-for="item in storageData"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="物品数量" prop="count">
          <el-col :span="20">
            <el-input v-model="form.count"></el-input>
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
    <el-dialog
        title="物品"
        :visible.sync="inputDialogVisible"
        width="30%"
        center
        @close="inputDialogClose">
      <el-dialog
          width="70%"
          title="用户选择"
          :visible.sync="innerVisible"
          append-to-body>
        <SelectUser @doSelectUser="doSelectUser"></SelectUser>
        <span>
          <el-button @click="innerVisible=false">取消</el-button>
          <el-button type="primary" @click="confirmUser">确定</el-button>
        </span>
      </el-dialog>
      <el-form ref="inputForm" :model="inputForm" label-width="80px" :rules="inputRules">
        <el-form-item label="物品名">
          <el-col :span="20">
            <el-input v-model="inputForm.name" readonly></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="申请人" prop="username">
          <el-col :span="20">
            <el-input v-model="inputForm.username" readonly @click.native="selectUser"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="物品数量" prop="count">
          <el-col :span="20">
            <el-input v-model="inputForm.count"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-col :span="20">
            <el-input v-model="inputForm.remark"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="inputDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="inputSave">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style scoped>

</style>