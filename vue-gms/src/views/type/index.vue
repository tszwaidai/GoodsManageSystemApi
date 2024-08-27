<template>
  <div class="app-container">
    <div class="head" style="margin-bottom: 15px">
        <el-input v-model="searchModel.typename" placeholder="请输入类型名" style="width: 200px;"></el-input>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="getTypeList">
          搜索
        </el-button>
        <el-button style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="openEditUI(null)">
          添加分类
        </el-button>
    </div>

      <el-table
        :data="tableData"
        style="width: 100%; margin-top: 30px;"
        border
      >
        <el-table-column prop="goodsTypeId" label="物品类型ID"  />
        <el-table-column prop="goodsTypeName" label="物品类型名称"  />
        <el-table-column prop="goodsTypeDesc" label="物品类型描述"  />
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="openEditUI(scope.row.goodsTypeId)" v-if="hasRole(['admin'])">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteType(scope.row)" v-if="hasRole(['admin'])">删除</el-button>
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
      <el-form  :model="typeForm" ref="typeFormRef" :rules="rules" label-width="120px" >
        <el-form-item label="物品类型名称" prop="goodsTypeName">
          <el-input v-model="typeForm.goodsTypeName" />
        </el-form-item> 
        <el-form-item label="物品类型描述" prop="goodsTypeDesc">
          <el-input v-model="typeForm.goodsTypeDesc" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveType">确 定</el-button>
      </div>
    </el-dialog>

  </div>


</template>

<script>
import typeApi  from '@/api/goodsType'
import { mapGetters } from 'vuex';

  export default {
    data() {
      return{
        typeForm: {},
        dialogFormVisible: false,
        title: "",
        total: 0,
        searchModel: {
          pageNo: 1, // 当前页码
          pageSize: 10 // 每页显示数量
        },
        tableData: [], // 表格数据
        rules: {
          goodsTypeName :{ required: true, message: '请输入类型名称', trigger: 'blur' },
          goodsTypeDesc :{ required: true, message: '请输入类型描述', trigger: 'blur' },
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
      saveType() {
          //触发表单验证
          this.$refs.typeFormRef.validate((valid)=>{
            if (valid) {
              //提交请求给后台
              typeApi.saveType(this.typeForm).then(response => {
                //成功提示
                this.$message({
                  message: response.msg,
                  type: 'success'
                });
                //关闭对话框
                this.dialogFormVisible = false;
                //刷新表格
                this.getTypeList();
              });
                
            }else{
              console.log("提交错误!");
              return false;
            }
          });
          
        },
      //删除类型
      deleteType(goodsType){
          this.$confirm(`您确认删除${goodsType.goodsTypeName}？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
          }).then(() => {
            typeApi.deleteTypeById(goodsType.goodsTypeId).then(response => {
              this.$message({
                type: 'success',
                message: '类型删除成功'
              });
              this.getTypeList();
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            });          
          });
        },
      // 处理分页器中每页显示数量的变化
        handleSizeChange(pageSize) {
          this.searchModel.pageSize = pageSize;
          this.getTypeList();
        },
        
        // 处理分页器中当前页码的变化
        handleCurrentChange(pageNo) {
          this.searchModel.pageNo = pageNo;
          this.getTypeList();
        
        },
        //获取类型数据
        getTypeList(){
          typeApi.getTypeList(this.searchModel).then(res => {
            this.tableData = res.data
            this.total = res.total
          })
        },
        //清理表单数据
        clearForm(){
          this.typeForm = {};
          this.$refs.typeFormRef.clearValidate();
        },
        // 打开表单
        openEditUI(goodsTypeId){
          if (goodsTypeId == null){
            this.title = '新增类型';
          }else{
            this.title = '修改类型';
            //根据id查询类型信息
            typeApi.getTypeById(goodsTypeId).then(response => {
              this.typeForm = response.data;
            });
          }
          this.dialogFormVisible = true;
        }
    },
    created() {
      this.getTypeList();
    }
  }
</script>

<style lang="scss" scoped>

</style>