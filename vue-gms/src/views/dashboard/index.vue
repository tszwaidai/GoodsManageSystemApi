<template>
  <div class="app"> 
    <h1 style="text-align: center;">欢迎您：{{ name }}</h1>
    <div style="width: 100%;height: 200px;font-size: 20px;font-weight: bold;margin-left: 5px;margin-top: 10px;">
      审核情况
      <div class="container-with-border">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-statistic title="待审核">
            <template slot="formatter">
              {{ statusCounts.pending }}
            </template>
          </el-statistic>
      </el-col>
      <el-col :span="6">
        <div>
          <el-statistic title="已通过">
            <template slot="formatter">
              {{ statusCounts.approved }}
            </template>
          </el-statistic>
        </div>
      </el-col>
      <el-col :span="6">
        <div>
          <el-statistic title="未通过">
            <template slot="formatter">
              {{ statusCounts.rejected }}
            </template>
          </el-statistic>
        </div>
      </el-col>
      <el-col :span="6">
        <div>
          <el-statistic title="待归还">
            <template slot="formatter">
              {{ statusCounts.using }}
            </template>
          </el-statistic>
        </div>
      </el-col>
    </el-row>
  </div>
    </div>
    <div id="main" style="width: 100%;height: 270px;" />
    
  </div>
  
</template>

<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts' // 确保正确引入 echarts
import goodsTypeApi from '@/api/goodsType'
import borrowApi from '@/api/borrow';

export default {
  data(){
    return{
      statusCounts: {
        pending: 0,
        approved: 0,
        rejected: 0,
        using: 0,
        returned: 0
      }
    }
  },
  computed: {
    ...mapGetters({
      name: 'name' // 将 Vuex 的 getter 'name' 映射到组件的计算属性 'username'
    })
  }, 
  mounted() {
    // 获取分类数量并生成ECharts图表
    this.fetchTypeCounts();

    // 获取borrow状态数量
    this.fetchStatusCounts();
  },
  methods: {
    fetchTypeCounts() {
      goodsTypeApi.getTypeCounts().then(response => {
        const typeCounts = response.data;
        const typeNames = typeCounts.map(item => item.typeName);
        const counts = typeCounts.map(item => item.count);
        
        // 初始化ECharts实例
        const myChart = echarts.init(document.getElementById('main'));
        
        // 配置参数
        const option = {
          title: {
            text: '类别数量',
          },
          tooltip: {},
          legend: {
            data: ['数量'],
            left: 'center'
          },
          xAxis: {
            data: typeNames
          },
          yAxis: {},
          series: [{
            name: '数量',
            type: 'bar',
            data: counts
          }]
        };
        
        myChart.setOption(option);
      }).catch(error => {
        console.error("获取错误！", error);
      });
    },
    fetchStatusCounts() {
      borrowApi.getStatusCount().then(response => {
        this.statusCounts = response.data;
      }).catch(error => {
        console.error("获取错误！", error);
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.app {
  background-image: url('/src/assets/404_images/login.jpg');
  background-size: 100%;

}
.like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
  }
  .container-with-border {
    width: 1400px;
    height: 100px;
    margin-top: 15px;
    margin-left: 2px;
    padding: 30px;
    border: 1px solid #dcdfe6;
    // border-radius: 4px;
    background: #fff;
}
.el-icon {
  font-size: 14px;
  vertical-align: middle;
}

.blue-dot {
  color: #409eff; /* 蓝色 */
}

</style>
