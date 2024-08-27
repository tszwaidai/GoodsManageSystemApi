const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  roles: state => state.user.roles,//拿到用户的角色信息
  permission_routes: state => state.permission.routes//筛选过的路由
}
export default getters
