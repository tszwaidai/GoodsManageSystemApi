<template>
  <div style="width: 400px;">
  <h1>用户注册</h1>
  <el-form ref="registerForm" :model="registerForm" label-width="80px" >
      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="registerForm.userName" />
      </el-form-item>
      <el-form-item label="用户密码" prop="userPassword">
        <el-input type="password" v-model="registerForm.userPassword" />
      </el-form-item>
      <el-form-item label="联系电话" prop="tele">
        <el-input v-model="registerForm.tele" />
      </el-form-item>
      <el-form-item label="所属社团" prop="club">
        <el-input v-model="registerForm.club" />
      </el-form-item>
      <el-form-item label="身份" prop="isAdmin">
        <el-radio-group v-model="registerForm.isAdmin">
          <el-radio label="1">管理员</el-radio>
          <el-radio label="0">学生</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit" size="large">注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import registerApi from '@/api/register';

  export default {
    data(){
      return {
          registerForm: {
          userName: '',
          userPassword: '',
          tele: '',
          club: '',
          isAdmin: '0' // 默认设置为学生
        }
      }
    },
    methods: {
      async submit() {
      try {
        // 调用后端注册接口
        await registerApi.register(this.registerForm);
        this.$message.success('注册成功');
        // 注册成功后的处理逻辑，比如跳转到登录页面
        this.$router.push('/login');
      } catch (error) {
        console.error('注册失败:', error);
        this.$message.error('注册失败，请稍后重试');
      }
      }
    }
  }
</script>

<style lang="scss" scoped>

</style>