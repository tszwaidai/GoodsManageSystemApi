import request from '@/utils/request'

export default {
    getLostList(searchModel) {
        return request({
            url: '/lost/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                goodsname: searchModel.goodsname,
                username: searchModel.username
            }
        });
    },
    solveLost(id) {
        return request({
            url: `/lost/solve/${id}`,
            method: 'put',
        })
    },
    deletelostById(id) {
        return request({
            url: `/lost/${id}`,
            method: 'delete',
        })
    }


}
