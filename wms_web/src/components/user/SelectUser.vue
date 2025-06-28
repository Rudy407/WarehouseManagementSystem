<script>
export default {
  name: "SelectUser",
  data() {
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
        roleId:2,
        phone:"",
        password:""
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
          roleId:2
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
      this.roleId=2;
      this.sex=null;
      this.loadPost();
    },
    selectCurrentChange(val) {
      this.$emit("doSelectUser",val);
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
    </div>
    <el-table :data="tableData" :header-cell-style="{background:'#EDF5FF',color:'black'}" size="small" border
              highlight-current-row
              @current-change="selectCurrentChange">
      <el-table-column prop="id" label="Id" align="center">
      </el-table-column>
      <el-table-column prop="no" label="账号" align="center">
      </el-table-column>
      <el-table-column prop="name" label="姓名" align="center">
      </el-table-column>
      <el-table-column prop="sex" label="性别" align="center">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.sex === 1 ? 'primary' : 'success'"
              disable-transitions>{{scope.row.sex === 1 ? '男' : '女'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="age" label="年龄" align="center">
      </el-table-column>
      <el-table-column prop="roleId" label="权限" align="center">
        <template slot-scope="scope">
          <el-tag
              :type="scope.row.roleId === 0 ? 'danger' : (scope.row.roleId === 1 ? 'primary' : 'success')"
              disable-transitions>{{scope.row.roleId === 0 ? '超级管理员' : (scope.row.roleId === 1 ? '管理员' : '用户')}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话号码" align="center" style="width: 200px">
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