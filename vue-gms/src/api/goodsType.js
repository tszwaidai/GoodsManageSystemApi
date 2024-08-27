import request from '@/utils/request'

export default {
    getTypeList(searchModel) {
        return request({
            url: '/goodsType/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                typename: searchModel.typename,
            }
        });
    },
    addType(goodsType) {
        return request({
            url: '/goodsType/add',
            method: 'post',
            data: goodsType
        })
    },
    updateType(goodsType) {
        return request({
            url: '/goodsType/update',
            method: 'put',
            data: goodsType
        })
    },
    saveType(goodsType) {
        if (goodsType.goodsTypeId == null && goodsType.goodsTypeId == undefined) {
            return this.addType(goodsType);
        }
        return this.updateType(goodsType);
    },
    getTypeById(id) {
        return request({
            url: `/goodsType/${id}`,
            method: 'get',
        })
    },
    deleteTypeById(id) {
        return request({
            url: `/goodsType/${id}`,
            method: 'delete',
        })
    },
    getTypeCounts() {
        return request({
            url: '/goodsType/type-count',
            method: 'get',
        })
    }

}
