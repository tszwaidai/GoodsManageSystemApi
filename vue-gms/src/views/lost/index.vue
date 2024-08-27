<template>
    <div class="app-container">
        <div class="head" style="margin-bottom: 15px">
        <el-input  v-model="searchModel.username" placeholder="请输入用户名" style="width: 200px;"></el-input>
        <el-input  v-model="searchModel.goodsname" placeholder="请输入物品名称" style="width: 200px; margin-left: 10px;"></el-input>
        <el-button type="primary" icon="el-icon-search" style="margin-left: 10px;" @click="getLostList">
          搜索
        </el-button>
        <el-button type="warning" icon="el-icon-refresh" style="margin-left: 10px;" @click="resetSearch">
            重置
        </el-button>
    </div>
    <el-table
        :data="tableData"
        style="width: 100%; margin-top: 30px;"
        border
      >
        <el-table-column prop="lostId" label="丢失记录ID" />
        <el-table-column prop="userName" label="申请人"  />
        <el-table-column prop="goodsName" label="申请物品"  />
        <el-table-column prop="lostTime" label="丢失时间"  />
        <el-table-column fixed="right" label="操作" width="200">
          <template slot-scope="scope">
            <el-button type="primary" size="small" @click="solve(scope.row)">丢失上报</el-button>
            <el-button type="danger" size="small" @click="deleteLost(scope.row)" v-if="hasRole(['admin'])">删除</el-button>
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


    </div>
</template>

<script>
import lostApi from '@/api/lost'
import { mapGetters } from 'vuex'
    export default {
        data(){
            return {
                total: 0,
                    searchModel: {
                    pageNo: 1, // 当前页码
                    pageSize: 10, // 每页显示数量
                    goodsname: '', // 物品名称
                    username: '', // 用户名
                },
                tableData: [],
            }
        },
        computed: {
        ...mapGetters(['roles'])
        },
        methods: {
            hasRole(requiredRoles) {
                return requiredRoles.some(role => this.roles.includes(role))
            },
            solve(lost) {
                lostApi.solveLost(lost.lostId).then(res => {
                    this.$message.success('物品丢失解决成功');
                }).catch(error => {
                    this.$message.error('操作失败');
                    console.error(error);   
                });
            },
            getLostList(){  
                lostApi.getLostList(this.searchModel).then(res => {
                    this.tableData = res.data;
                    this.total = res.total;
                })
            },
            // 处理分页器中每页显示数量的变化
            handleSizeChange(pageSize) {
                this.searchModel.pageSize = pageSize;
                this.getLostList();
            },
            // 处理分页器中当前页码的变化
            handleCurrentChange(pageNo) {
                this.searchModel.pageNo = pageNo;
                this.getLostList();
            
            },
            // 重置
            resetSearch() {
                this.searchModel.username = '';
                this.searchModel.goodsname = '';
                this.getLostList();
            },
            //删除
            deleteLost(lost){
                this.$confirm(`您确认删除这条记录？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
                }).then(() => {
                    lostApi.deletelostById(lost.lostId).then(response => {
                    this.$message({
                        type: 'success',
                        message: '记录删除成功'
                    });
                    this.getLostList();
                    });
                }).catch(() => {
                    this.$message({
                    type: 'info',
                    message: '已取消删除'
                    });
                });
            },
        },
        created() {
            this.getLostList();
        }
    }
</script>

<style lang="scss" scoped>

</style>