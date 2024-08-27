<template>
    <div style="width: 400px; text-align: center;">
    <h1>修改个人信息</h1>
    <el-form ref="updateForm" :model="updateForm" label-width="80px" >
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="updateForm.userName" />
      </el-form-item>
      <el-form-item label="用户密码" prop="userPassword">
        <el-input type="password" v-model="updateForm.userPassword" />
      </el-form-item>
      <el-form-item label="联系电话" prop="tele">
        <el-input v-model="updateForm.tele" />
      </el-form-item>
      <el-form-item label="所属社团" prop="club">
        <el-input v-model="updateForm.club" />
      </el-form-item>
      <!-- <el-form-item label="身份" prop="isAdmin">
        <el-radio-group v-model="updateForm.isAdmin">
          <el-radio label="1">管理员</el-radio>
          <el-radio label="0">学生</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item>
        <el-button type="primary" @click="submit" size="larger">确定</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import centerApi from '@/api/center';
import { mapState } from 'vuex'

export default {
    data() {
        return{
            updateForm: {
                userName: '',
                userPassword: '',
                tele: '',
                club: '',
                isAdmin: 0
            },
        }
    },
    computed: {
        ...mapState({
        userId: state => state.user.userId // 从 Vuex 获取 userId
        })
    },
    mounted() {
        
        if (this.userId) {
        this.getUserInfo(this.userId);
        }
    },
    methods:{
        submit(){
          centerApi.saveUser(this.updateForm).then(response => {
                //成功提示
                this.$message({
                  message: response.msg,
                  type: 'success'
                });
                
            }).catch(error => {
                console.error("修改失败：", error)
                this.$message.error('保存用户信息失败')
            });
        },
        getUserInfo(id) {
            centerApi.getUserById(id).then(res=> {
                this.updateForm = res.data
            }).catch(error => {
                console.error("获取用户信息失败：", error);
            });
        }
    }
}
</script>

<style lang="scss" scoped>

</style>