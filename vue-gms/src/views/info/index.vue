<template>
    <div class="app-container">
      <div class="head" style="margin-bottom: 15px">
          <el-input v-model="searchModel.goodsname" placeholder="请输入物品名称" style="width: 200px;"></el-input>
          <el-select  v-model="searchModel.typename" filterable placeholder="物品类型"  style="width: 200px; margin-left: 10px;">
            <el-option  
              v-for="type in goodsType"
              :key="type.goodsTypeId"
              :label="type.goodsTypeName"
              :value="type.goodsTypeName" 
             />
             <!-- fix !!! -->
          </el-select>
          <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="getInfoList">
            搜索
          </el-button>
          <el-button type="warning" icon="el-icon-refresh" style="margin-left: 10px;" @click="resetSearch">
            重置
          </el-button>
          <el-button style="margin-left: 10px;" type="success" icon="el-icon-edit" @click="openEditUI(null)">
            添加物品
          </el-button>
      </div>

      <el-table
        :data="tableData"
        style="width: 100%; margin-top: 30px;"
        border
      >
        <el-table-column prop="goodsId" label="物品ID" width="100" />
        <el-table-column prop="goodsName" label="物品名称"  width="170"/>
        <el-table-column prop="goodsTypeName" label="物品类型"  />
        <el-table-column prop="goodsDesc" label="物品描述"  width="400"/>
        <el-table-column prop="goodsPrice" label="物品价格"  width="100"/>
        <!-- <el-table-column label="物品状态" width="100"> -->
          <!-- <template slot-scope="scope"> -->
            <!-- <el-tag v-if="scope.row.status === 0" >未申请</el-tag> -->
            <!-- <el-tag v-if="scope.row.status === 1" type="warning">申请中</el-tag>
            <el-tag v-if="scope.row.status === 2" type="success">已通过</el-tag>
            <el-tag v-if="scope.row.status === 3" type="danger" title="请联系管理员后再次申请">未通过</el-tag>
            <el-tag v-if="scope.row.status === 4" type="info">使用中</el-tag> -->
            <!-- 初始状态为“未申请”，操作栏按钮为“申请”，同一个用户点击“申请”后状态变为“申请中”，按钮仍为“申请”，若用户再次点击“申请”弹窗提示“请勿重复申请！”，管理员通过申请后，状态变为“已通过”，按钮变为“领用”
             用户点击“领用”后，状态变为“使用中”，按钮变为“申请”，若用户点击“申请”弹窗提示“请归还后申请！”，在物品借用管理页面，用户点击“归还”，此页面已经在“使用中”的物品状态变为“未申请”，按钮再次变为“申请”且可用
             如果管理员不通过申请，状态为“未通过”并悬浮提示“请联系管理员询问原因”，按钮仍为“申请”且可用-->
          <!-- </template> -->
        <!-- </el-table-column> -->
        <el-table-column fixed="right" label="操作" width="300">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status === 0" type="success" size="small" @click="apply(scope.row)">申请</el-button>
            <el-button v-if="scope.row.status === 1" type="success" size="small" @click="apply(scope.row)">申请</el-button>
            <el-button v-if="scope.row.status === 2" type="success" size="small" @click="apply(scope.row)">申请</el-button>
            <el-button v-if="scope.row.status === 3" type="success" size="small" @click="apply(scope.row)">申请</el-button>
            <el-button v-if="scope.row.status === 4" type="warning" size="small" @click="apply(scope.row)">申请</el-button>
            <el-button type="primary" size="small" @click="openEditUI(scope.row.goodsId)" v-if="hasRole(['admin'])">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteInfo(scope.row)" v-if="hasRole(['admin'])">删除</el-button>
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
      <el-form  :model="infoForm" ref="infoFormRef" :rules="rules" label-width="120px" >
        <el-form-item label="物品名称" prop="goodsName">
          <el-input v-model="infoForm.goodsName" />
        </el-form-item> 
        <el-form-item label="物品类型" prop="goodsTypeId">
          <el-select  v-model="infoForm.goodsTypeId"  placeholder="请选择类型"  >
            <el-option  
              v-for="type in goodsType"
              :key="type.goodsTypeId"
              :label="type.goodsTypeName"
              :value="type.goodsTypeId"
             />
          </el-select>
        </el-form-item>
        <el-form-item label="物品描述" prop="goodsDesc">
          <el-input v-model="infoForm.goodsDesc" />
        </el-form-item> 
        <el-form-item label="物品价格" prop="goodsPrice">
          <el-input v-model="infoForm.goodsPrice" />
        </el-form-item> 
        <el-form-item label="物品状态" prop="status">
          <el-select v-model="infoForm.status"  placeholder="请选择状态"  >
            <el-option label="未申请" :value="0" />
            <el-option label="申请中" :value="1" />
            <el-option label="已通过" :value="2" />
            <el-option label="未通过" :value="3" />
            <el-option label="使用中" :value="4" />
          </el-select>
        </el-form-item> 
        

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveInfo">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import infoApi from '@/api/goodsInfo'
import typeApi from '@/api/goodsType'
import { mapGetters } from 'vuex';
import { getInfo } from '@/api/user'

  export default {
    data(){
      return {
        infoForm: {},
        dialogFormVisible: false,
        title: "",
        total: 0,
        searchModel: {
          pageNo: 1, // 当前页码
          pageSize: 10, // 每页显示数量
          goodsname: '', // 物品名称
          typename: '', // 物品类型
        },
        tableData: [], // 表格数据
        goodsType: [], // 物品类型数据
        rules: {
          goodsName :{ required: true, message: '请输入物品名称', trigger: 'blur' },
          goodsTypeId :{ required: true, message: '请选择物品类型', trigger: 'blur' },
          goodsPrice: { required: true, message: '请输入物品价格', trigger: 'blur' },
          goodsDesc: { required: true, message: '请输入物品描述', trigger: 'blur' },
          status: { required: true, message: '请选择物品状态', trigger: 'change' },
          
        }
      }
    },
    computed: {
      ...mapGetters(['userId']),
      ...mapGetters(['roles'])
    },
    methods: {
      hasRole(requiredRoles) {
        return requiredRoles.some(role => this.roles.includes(role))
      },
      saveInfo() {
        //触发表单验证
        this.$refs.infoFormRef.validate((valid) => {
        if (valid) {
          infoApi.saveInfo(this.infoForm).then(response => {
            this.$message({
              message: response.msg,
              type: 'success'
            });
            this.dialogFormVisible = false;
            this.getInfoList();
          });
        } else {
            console.log("提交错误!");
            return false;
          }
        });
      },
      // 获取物品类型数据
        getGoodsType() {
          typeApi.getTypeList(this.searchModel) 
            .then(res => {
              this.goodsType = res.data;
            });
        },
        //删除
        deleteInfo(goodsInfo){
          this.$confirm(`您确认删除${goodsInfo.goodsName}？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
          }).then(() => {
            infoApi.deleteInfoById(goodsInfo.goodsId).then(response => {
              this.$message({
                type: 'success',
                message: '物品删除成功'
              });
              this.getInfoList();
            });
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
          this.getInfoList();
        },
        
        // 处理分页器中当前页码的变化
        handleCurrentChange(pageNo) {
          this.searchModel.pageNo = pageNo;
          this.getInfoList();
        
        },
        //获取类型数据
        getInfoList(){
          infoApi.getInfoList(this.searchModel).then(res => {
            this.tableData = res.data
            this.total = res.total
          })
        },
        // 重置搜索条件
        resetSearch() {
          this.searchModel.goodsname = '';
          this.searchModel.typename = '';
          this.getInfoList();
        },
        // 打开表单
        openEditUI(goodsId){
          if (goodsId == null){
            this.title = '新增物品';
          }else{
            this.title = '修改物品';
            //根据id查询物品信息
            infoApi.getInfoById(goodsId).then(response => {
              this.infoForm = response.data;
            });
          }
          this.dialogFormVisible = true;
        },
        // 清理表单验证
        clearForm() {
          this.infoForm = {};
          this.$refs.infoFormRef.clearValidate();
        },
        //初次申请
        apply(row) {
          // 假设已经在 Vuex 中存储了 token，可以通过 this.$store.state.user.token 访问
          const token = this.$store.state.user.token;
 
          getInfo(token).then(res => {
            const userId = res.data.userId;
            infoApi.apply(row.goodsId, userId).then(response => {
              this.$message.success('申请物品成功');
              this.getInfoList();
            }).catch(error => {
              this.$message.error('申请失败');
              console.error(error);
            });
          }).catch(error => {
            this.$message.error('获取用户信息失败');
            console.error(error);
          });
      },
      repeatApplyWarning() {
        this.$message.warning("请勿重复申请！");
      },
      applyReturnWarning() {
        this.$message.warning("请归还后申请！");
      },

    },
    created(){
      this.getInfoList();
      this.getGoodsType();
    }
  }
</script>

<style lang="scss" scoped>

</style>