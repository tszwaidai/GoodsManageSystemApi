import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard', breadcrumb: true }
    }]
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

]

//异步挂载的路由
//动态需要根据权限加载的路由表
//每个页面给哪些角色放行，在roles数组提前写好即可
export const asyncRoutes = [
  {
    path: '/manage',
    component: Layout,
    redirect: '/manage/info',
    name: 'Manage',
    meta: { title: '物品管理', icon: 'el-icon-s-help', roles: ['admin', 'student'], breadcrumb: true },
    children: [
      {
        path: 'info',
        name: 'Info',
        component: () => import('@/views/info/index'),
        meta: { title: '物品信息管理', icon: 'table', roles: ['admin', 'student'], breadcrumb: true }
      },
      {
        path: 'type',
        name: 'Type',
        component: () => import('@/views/type/index'),
        meta: { title: '物品分类管理', icon: 'tree', roles: ['admin', 'student'], breadcrumb: true }
      }
    ]
  },

  {
    path: '/borrow',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Borrow',
        component: () => import('@/views/borrow/index'),
        meta: { title: '物品借用管理', icon: 'form', roles: ['admin', 'student'], breadcrumb: true }
      }
    ]
  },
  {
    path: '/lost',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Lost',
        component: () => import('@/views/lost/index'),
        meta: { title: '物品丢失处理', icon: 'link', roles: ['admin'], breadcrumb: true }
      }
    ]
  },

  {
    path: '/users',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Users',
        component: () => import('@/views/users/index'),
        meta: { title: '用户管理', icon: 'nested', roles: ['admin'], breadcrumb: true }
      }
    ]
  },
  {
    path: '/center',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Center',
        component: () => import('@/views/center/index'),
        meta: { title: '个人中心', icon: 'nested', roles: ['admin', 'student'], breadcrumb: true }
      }
    ]
  },


  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
