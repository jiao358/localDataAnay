import Vue from 'vue'
import Router from 'vue-router'
import Layout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }]
  },
  {
    path: '/system',
    component: Layout,
    name: 'System',
    meta: { title: '系统管理', icon: 'setting' },
    children: [
      {
        path: 'user',
        component: () => import('@/views/system/user/index'),
        name: 'User',
        meta: { title: '用户管理' }
      },
      {
        path: 'role',
        component: () => import('@/views/system/role/index'),
        name: 'Role',
        meta: { title: '角色管理' }
      },
      {
        path: 'permission',
        component: () => import('@/views/system/permission/index'),
        name: 'Permission',
        meta: { title: '权限管理' }
      },
      {
        path: 'order',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/order/index'),
            name: 'Order',
            meta: { title: '订单管理', icon: 'shopping-cart' }
          }
        ]
      },
      {
        path: '/system/customer',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/customer/index'),
            name: 'Customer',
            meta: { title: '客户管理', icon: 'user' }
          }
        ]
      },
      {
        path: '/system/platform',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/platform/index'),
            name: 'Platform',
            meta: { title: '平台账号', icon: 'link' }
          }
        ]
      },
      {
        path: '/system/sales',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/sales/index'),
            name: 'Sales',
            meta: { title: '销售订单', icon: 'shopping-bag-1' }
          }
        ]
      },
      {
        path: '/system/dict',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/dict/index'),
            name: 'Dict',
            meta: { title: '字典管理', icon: 'list' }
          }
        ]
      },
      {
        path: '/system/knowledge',
        component: Layout,
        children: [
          {
            path: '',
            component: () => import('@/views/system/knowledge/index'),
            name: 'Knowledge',
            meta: { title: '知识模型', icon: 'education' }
          }
        ]
      }
    ]
  }
]

// 解决路由重复点击报错的问题
const originalPush = Router.prototype.push
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => {
    if (err.name !== 'NavigationDuplicated') {
      throw err
    }
  })
}

const createRouter = () => new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router 