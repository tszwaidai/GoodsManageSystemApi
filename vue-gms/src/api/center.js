import request from '@/utils/request'

export default {
    //    根据id获取当前用户信息
    getUserById(id) {
        return request({
            url: `/user/${id}`,
            method: 'get',
        })
    },
    addUser(user) {
        return request({
            url: '/user/add',
            method: 'post',
            data: user
        })
    },
    updateUser(user) {
        return request({
            url: '/user/update',
            method: 'put',
            data: user
        })
    },
    saveUser(user) {
        if (user.userId == null && user.userId == undefined) {
            return this.addUser(user);
        }
        return this.updateUser(user);
    },

}
