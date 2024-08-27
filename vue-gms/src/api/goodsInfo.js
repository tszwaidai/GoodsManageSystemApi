import request from '@/utils/request'

export default {
    getInfoList(searchModel) {
        return request({
            url: '/goodsInfo/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                typename: searchModel.typename,
                goodsname: searchModel.goodsname
            }
        });
    },
    addInfo(goodsInfo) {
        return request({
            url: '/goodsInfo/add',
            method: 'post',
            data: goodsInfo
        })
    },
    updateInfo(goodsInfo) {
        return request({
            url: '/goodsInfo/update',
            method: 'put',
            data: goodsInfo
        })
    },
    saveInfo(goodsInfo) {
        if (goodsInfo.goodsId == null && goodsInfo.goodsId == undefined) {
            return this.addInfo(goodsInfo);
        }
        return this.updateInfo(goodsInfo);
    },
    getInfoById(id) {
        return request({
            url: `/goodsInfo/${id}`,
            method: 'get',
        })
    },
    deleteInfoById(id) {
        return request({
            url: `/goodsInfo/${id}`,
            method: 'delete',
        })
    },
    updateStatus(goodsInfo) {
        return request({
            url: '/goodsInfo/updateStatus',
            method: 'post',
            data: goodsInfo
        });
    },
    apply(goodsId, userId) {
        return request({
            url: '/goodsInfo/apply',
            method: 'post',
            data: {
                goodsId: goodsId,
                userId: userId
            }
        });
    },
    complete(goodsId, userId) {
        return request({
            url: '/goodsInfo/completeBorrow',
            method: 'post',
            data: {
                goodsId: goodsId,
                userId: userId
            }
        });
    },

}