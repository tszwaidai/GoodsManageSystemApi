<template>
    <div class="app-container">
      <div class="head" style="margin-bottom: 15px">
        <el-input v-model="searchModel.username" placeholder="请输入用户名" style="width: 200px;"></el-input>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="getUserList">
          搜索
        </el-button>
        <el-button style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="openEditUI(null)">
          添加用户
        </el-button>
       
      </div>
  
      <el-table
        :data="tableData"
        style="width: 100%; margin-top: 30px;"
        border
      >
        <el-table-column prop="userId" label="用户ID"  />
        <el-table-column prop="userName" label="用户名称"  />
        <el-table-column prop="userPassword" label="用户密码"  />
        <el-table-column prop="tele" label="联系电话"  />
        <el-table-column prop="club" label="所属社团"  />
        <el-table-column label="用户身份" >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isAdmin === 1" type="success">管理员</el-tag>
            <el-tag v-else type="warning">学生</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="openEditUI(scope.row.userId)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteUser(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
  
      <el-pagination
        style="margin-top: 15px;"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="searchModel.pageNo"
        :page-sizes="[5, 10, 20, 50]"
        :page-size="searchModel.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>


      <el-dialog @close="clearForm" :title="title" :visible.sync="dialogFormVisible" width="30%">
      <!--普通表单-->
      <el-form  :model="userForm" ref="userFormRef" :rules="rules" label-width="80px">
        <el-form-item label="用户名称" prop="userName">
          <el-input v-model="userForm.userName" />
        </el-form-item> 
        <el-form-item v-if="userForm.userId == null || userForm.userId == undefined" label="用户密码" prop="userPassword">
          <el-input v-model="userForm.userPassword" />
        </el-form-item>
        <el-form-item label="联系电话" prop="tele">
          <el-input v-model="userForm.tele" />
        </el-form-item>
        <el-form-item label="所属社团" prop="club">
          <el-input v-model="userForm.club" />
        </el-form-item>
        <el-form-item label="身份" prop="isAdmin">
          <el-radio v-model="userForm.isAdmin" :label="1">管理员</el-radio>
          <el-radio v-model="userForm.isAdmin" :label="0">学生</el-radio>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveUser">确 定</el-button>
      </div>
    </el-dialog>
    </div>
  </template>
  
  <script>
  import userApi from '@/api/userManage'
  
  export default {
    data() {
      return {
        userForm: {},
        dialogFormVisible: false,
        title: "",
        total: 0,
        searchModel: {
          pageNo: 1, // 当前页码
          pageSize: 10 // 每页显示数量
        },
        tableData: [], // 表格数据
        rules: {
          userName :{ required: true, message: '请输入用户名', trigger: 'blur' },
          userPassword :{ required: true, message: '请输入用户密码', trigger: 'blur' },
          tele :{ required: true, message: '请输入联系方式', trigger: 'blur' },
          club :{ required: true, message: '请输入所属社团', trigger: 'blur' },
        }
      }
    },
    
    methods: {
      saveUser() {
          //触发表单验证
          this.$refs.userFormRef.validate((valid)=>{
            if (valid) {
              //提交请求给后台
              userApi.saveUser(this.userForm).then(response => {
                //成功提示
                this.$message({
                  message: response.msg,
                  type: 'success'
                });
                //关闭对话框
                this.dialogFormVisible = false;
                //刷新表格
                this.getUserList();
              });
                
            }else{
              console.log("提交错误!");
              return false;
            }
          });
          
        },
        //删除用户
        deleteUser(user){
          this.$confirm(`您确认删除${user.userName}？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
          }).then(() => {
            userApi.deleteUserById(user.userId).then(response => {
              this.$message({
                type: 'success',
                message: response.msg
              });
              this.getUserList();
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });          
          });
        },
        //清理表单数据
        clearForm(){
          this.userForm = {};
          this.$refs.userFormRef.clearValidate();
        },
        // 打开表单
        openEditUI(userId){
          if (userId == null){
            this.title = '新增用户';
          }else{
            this.title = '修改用户';
            //根据id查询用户信息
              userApi.getUserById(userId).then(response => {
              this.userForm = response.data;
            });
          }
          this.dialogFormVisible = true;
        },
        // 处理分页器中每页显示数量的变化
        handleSizeChange(pageSize) {
          this.searchModel.pageSize = pageSize;
          this.getUserList();
        },
        
        // 处理分页器中当前页码的变化
        handleCurrentChange(pageNo) {
          this.searchModel.pageNo = pageNo;
          this.getUserList();
        
        },
        // 获取数据
        getUserList(){
          userApi.getUserList(this.searchModel).then(response =>{
            this.tableData = response.data;
            this.total = response.total;
          })
        },
        
    },
    //钩子函数
    created() {
      this.getUserList();
    }

  }
  </script>
  
  <style lang="scss" scoped>
  
  </style>
  