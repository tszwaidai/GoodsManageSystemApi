import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    userId: null,
    userName: '',
    avatar: '',
    roles: [] // 添加 roles 字段
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    console.log(token, 'token22222222222222222222')
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_USER_ID: (state, userId) => {  // 添加 SET_USER_ID
    state.userId = userId
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles // 设置 roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: password }).then(response => {
        const { data } = response
        commit('SET_TOKEN', data.token)
        // commit('SET_USER_ID', data.userId)  // 在登录成功时设置 userId
        setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    console.log(commit, state, 'ssssssssssssssss')
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const { data } = response

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const { userName, avatar, userId, isAdmin } = data

        // if (!roles || roles.length <= 0) {
        // reject('getInfo: roles must be a non-null array!')
        // }

        commit('SET_NAME', userName)
        commit('SET_AVATAR', avatar);
        commit('SET_USER_ID', userId);
        // commit('SET_ROLES', roles);
        commit('SET_ROLES', isAdmin === 1 ? ['admin'] : ['student'])
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE', '')
        commit('SET_ROLES', []) // add
        removeToken() //add
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

