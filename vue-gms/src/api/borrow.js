import request from '@/utils/request'

export default {
    getBorrowList(searchModel) {
        return request({
            url: '/borrow/list',
            method: 'get',
            params: {
                pageNo: searchModel.pageNo,
                pageSize: searchModel.pageSize,
                goodsname: searchModel.goodsname,
                username: searchModel.username
            }
        });
    },
    addBorrow(borrow) {
        return request({
            url: '/borrow/add',
            method: 'post',
            data: borrow
        })
    },
    updateBorrow(borrow) {
        return request({
            url: '/borrow/update',
            method: 'put',
            data: borrow
        })
    },
    saveBorrow(borrow) {
        if (borrow.borrowId == null && borrow.borrowId == undefined) {
            return this.addBorrow(borrow);
        }
        return this.updateBorrow(borrow);
    },
    getBorrowById(id) {
        return request({
            url: `/borrow/${id}`,
            method: 'get',
        })
    },
    deleteBorrowById(id) {
        return request({
            url: `/borrow/${id}`,
            method: 'delete',
        })
    },
    approveBorrow(id) {
        return request({
            url: `/borrow/approve/${id}`,
            method: 'put',
        })
    },
    rejectBorrow(id) {
        return request({
            url: `/borrow/reject/${id}`,
            method: 'put',
        })
    },
    complete(id) {
        return request({
            url: `/borrow/completeBorrow/${id}`,
            method: 'post',
        });
    },
    returnGoods(id) {
        return request({
            url: `/borrow/return/${id}`,
            method: 'put',
        })
    },
    lostGoods(goodsId, userId) {
        return request({
            url: '/borrow/lost',
            method: 'post',
            data: {
                goodsId: goodsId,
                userId: userId
            }
        });
    },
    getStatusCount() {
        return request({
            url: '/borrow/status-count',
            method: 'get'
        });
    }


}
