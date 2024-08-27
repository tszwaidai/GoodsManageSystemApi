<template>
  <div class="app-container">
    <div class="head" style="margin-bottom: 15px">
        <el-input  v-model="searchModel.username" placeholder="请输入用户名" style="width: 200px;"></el-input>
        <el-input  v-model="searchModel.goodsname" placeholder="请输入物品名称" style="width: 200px; margin-left: 10px;"></el-input>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="getBorrowList">
          搜索
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" style="margin-left: 10px;" @click="resetSearch">
            重置
          </el-button>
        <el-button style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="openEditUI(null)">
          添加借用记录
        </el-button>
    </div>

    <el-table
        :data="tableData"
        style="width: 100%; margin-top: 30px;"
        border
      >

        <el-table-column prop="userName" label="申请人"  />
        <el-table-column prop="goodsName" label="申请物品"  />
        <el-table-column prop="borrowTime" label="借用时间"  />
        <el-table-column prop="returnTime" label="归还时间"  />
        <el-table-column label="状态" >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 0" >申请中</el-tag>
            <el-tag v-if="scope.row.status === 1" type="warning">已通过</el-tag>
            <el-tag v-if="scope.row.status === 2" type="danger" title="请联系管理员后再次申请">未通过</el-tag>
            <el-tag v-if="scope.row.status === 3" type="info">使用中</el-tag>
            <el-tag v-if="scope.row.status === 4" type="danger" >已归还</el-tag>
            <el-tag v-if="scope.row.status === 5" type="info">已丢失</el-tag>
            <el-tag v-if="scope.row.status === 6" >丢失已解决</el-tag>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" width="500">
          <template slot-scope="scope">
            <el-button type="warning" size="small" @click="approve(scope.row)" v-if="hasRole(['admin'])">通过</el-button>
            <el-button type="danger" size="small"  @click="reject(scope.row)" v-if="hasRole(['admin'])">不通过</el-button>
            <el-button v-if="scope.row.status === 1 ? 'primary' : 'default'" type="primary" size="small" @click="complete(scope.row)" :disabled="scope.row.status !== 1">领用</el-button>
            <el-button type="warning" size="small" @click="returnGoods(scope.row)">归还</el-button>
            <el-button type="success" size="small" @click="lost(scope.row)">丢失</el-button>
            <el-button type="primary" size="small" @click="openEditUI(scope.row.borrowId)" v-if="hasRole(['admin'])">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteBorrow(scope.row)" v-if="hasRole(['admin'])">删除</el-button>
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
      <el-form  :model="borrowForm" ref="borrowFormRef" :rules="rules" label-width="120px" >
        <el-form-item label="申请人" prop="userId">
          <el-select  v-model="borrowForm.userId"  placeholder="请选择申请人"  >
            <el-option  
              v-for="type in users"
              :key="type.userId"
              :label="type.userName"
              :value="type.userId"
             />
          </el-select>
        </el-form-item>

        <el-form-item label="申请物品" prop="goodsId">
          <el-select  v-model="borrowForm.goodsId"  placeholder="请选择申请物品"  >
            <el-option  
              v-for="type in goodsInfo"
              :key="type.goodsId"
              :label="type.goodsName"
              :value="type.goodsId"
             />
          </el-select>          
        </el-form-item>

        <el-form-item label="借用时间" prop="borrowTime">
          <el-date-picker v-model="borrowForm.borrowTime" type="datetime" placeholder="请选择借用时间" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveBorrow">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import borrowApi from '@/api/borrow'
import infoApi from '@/api/goodsInfo'
import userApi from '@/api/userManage'
import { getInfo } from '@/api/user';
import { mapGetters } from 'vuex'
import borrow from '@/api/borrow';
  export default {
    data(){
      return {
        borrowForm: {},
        dialogFormVisible: false,
        title: '',
        total: 0,
        searchModel: {
          pageNo: 1, // 当前页码
          pageSize: 10, // 每页显示数量
          goodsname: '', // 物品名称
          username: '', // 用户名
        },
        tableData: [],
        users: [],
        goodsInfo: [],
        rules: {
          userId :{ required: true, message: '请选择申请人', trigger: 'blur' },
          goodsId :{ required: true, message: '请选择申请物品', trigger: 'blur' },
        }
      }
    },
    computed: {
    ...mapGetters(['roles'])
    },
    methods: {
      hasRole(requiredRoles) {
        return requiredRoles.some(role => this.roles.includes(role))
      },
      reject(borrow) {
        borrowApi.rejectBorrow(borrow.borrowId).then(res => {
          this.$message.success('物品申请不通过');
          this.getBorrowList();
        }).catch(error => {
          this.$message.error('操作失败');
          console.error(error);
        });
      },
      getInfoList(){
          infoApi.getInfoList(this.searchModel).then(res => {
            this.tableData = res.data
            this.total = res.total
          })
        },
      approve(borrow) {
        borrowApi.approveBorrow(borrow.borrowId).then(res => {
          this.$message.success('物品申请已通过');
          this.getBorrowList();
        }).catch(error => {
          this.$message.error('操作失败');
          console.error(error);
        });
      },
      // 领用物品
      complete(borrow) {
            borrowApi.complete(borrow.borrowId).then(response => {
              this.$message.success('领用物品成功');
              this.getBorrowList();
            }).catch(error => {
              this.$message.error('领用失败');
              console.error(error);
            });
          
      },
      returnGoods(borrow) {
        borrowApi.returnGoods(borrow.borrowId).then(res => {
          this.$message.success('物品已归还');
          this.getBorrowList();
        }).catch(error => {
          this.$message.error('归还失败');
          console.error(error);
        });
      },
      lost(row) {
        // 假设已经在 Vuex 中存储了 token，可以通过 this.$store.state.user.token 访问
        const token = this.$store.state.user.token;
        getInfo(token).then(res => {
            const userId = res.data.userId;
            borrowApi.lostGoods(row.goodsId, userId).then(response => {
              this.$message.success('丢失上报成功');
              // this.getBorrowList();
            }).catch(error => {
              this.$message.error('上报失败');
              console.error(error);
            });
          }).catch(error => {
            this.$message.error('获取用户信息失败');
            console.error(error);
          });
      },
      saveBorrow() {
        //触发表单验证
        this.$refs.borrowFormRef.validate((valid) => {
        if (valid) {
          borrowApi.saveBorrow(this.borrowForm).then(response => {
            this.$message({
              message: response.msg,
              type: 'success'
            });
            this.dialogFormVisible = false;
            this.getBorrowList();
          });
        } else {
            console.log("提交错误!");
            return false;
          }
        });
      },
      //删除
      deleteBorrow(borrow){
        this.$confirm(`您确认删除这条记录？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
          }).then(() => {
            borrowApi.deleteBorrowById(borrow.borrowId).then(response => {
              this.$message({
                type: 'success',
                message: '记录删除成功'
              });
              this.getBorrowList();
            });
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });
          });
      },
      //获取表格数据
      getBorrowList(){
        borrowApi.getBorrowList(this.searchModel).then(res => {
          this.tableData = res.data;
          this.total = res.total;
        });
      },
      // 处理分页器中每页显示数量的变化
      handleSizeChange(pageSize) {
          this.searchModel.pageSize = pageSize;
          this.getBorrowList();
        },
      // 处理分页器中当前页码的变化
      handleCurrentChange(pageNo) {
        this.searchModel.pageNo = pageNo;
        this.getBorrowList();
      
      },
      // 重置
      resetSearch() {
        this.searchModel.username = '';
        this.searchModel.goodsname = '';
        this.getBorrowList();
      },
      // 清理表单验证
      clearForm(){
        this.borrowForm = {};
        this.$refs.borrowFormRef.clearValidate();
      },
      // 打开表单
      openEditUI(borrowId){
        if (borrowId == null){
          this.title = '新增借阅记录';
        }else{
          this.title = '修改借阅记录';
          //根据id查询类型信息
          borrowApi.getBorrowById(borrowId).then(response => {
            this.borrowForm = response.data;
          });
        }
        this.dialogFormVisible = true;
      },
      // 获取物品数据
      getGoods() {
        infoApi.getInfoList(this.searchModel) 
          .then(res => {
            this.goodsInfo = res.data;
          });
      },
      getUser() {
        userApi.getUserList(this.searchModel)
        .then(res => {
            this.users = res.data;
          });
      },

    },
    created(){
      // this.getInfoList();
      this.getBorrowList();
      this.getGoods();
      this.getUser();
    }
  }
</script>

<style lang="scss" scoped>

</style>