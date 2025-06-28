<script>
export default {
  name: "RecordManage",
  data() {
    return {
      user: JSON.parse((sessionStorage.getItem("CurUser"))),
      tableData: [],
      storageData:[],
      goodstypeData:[],
      pageSize: 10,
      pageNum: 1,
      total: 0,
      id:null,
      goodsname:"",
      goodstype:null,
      storage:null,
      centerDialogVisible: false,
      isEdit:false
    }
  },
  methods:{
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
    loadPost(){
      this.$axios.post(this.$httpUrl+'/record/listPage',{
        pageNum:this.pageNum,
        pageSize:this.pageSize,
        param:{
          goodsname:this.goodsname,
          storage:this.storage,
          goodstype:this.goodstype,
          userid:this.user.id,
          roleId:this.user.roleId
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
      this.goodsname='';
      this.goodstype=null;
      this.storage=null;
      this.loadPost();
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
    this.loadType();
    this.loadStorage();
  }
}
</script>

<template>
  <div>
    <div>
      <el-input v-model="goodsname" placeholder="请输入物品名" style="width: 200px;margin-bottom: 5px" @keyup.enter.native="loadPost"></el-input>
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
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border>
<!--      <el-table-column prop="id" label="Id" align="center">-->
<!--      </el-table-column>-->
      <el-table-column prop="goodsname" label="物品名" align="center">
      </el-table-column>
      <el-table-column prop="goodstypename" label="物品类型" align="center" >
      </el-table-column>
      <el-table-column prop="storagename" label="仓库" align="center">
      </el-table-column>
      <el-table-column prop="adminname" label="操作人" align="center">
      </el-table-column>
      <el-table-column prop="username" label="申请人" align="center">
      </el-table-column>
      <el-table-column prop="count" label="数量" align="center">
      </el-table-column>
      <el-table-column prop="createtime" label="时间" align="center">
      </el-table-column>
      <el-table-column prop="remark" label="备注" align="center">
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
  </div>
</template>

<style scoped>

</style>