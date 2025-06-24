import VueRouter from 'vue-router';

const routes =[
    {
        path:'/',
        name:'login',
        component:()=>import('../components/LoginOption')
    },
    {
        path:'/index',
        name:'index',
        component:()=>import('../components/IndexPage'),
        children:[
            {
                path:'home',
                name:'home',
                meta:{
                    title:"首页"
                },
                component:()=>import('../components/HomeOption')
            },
            {
                path:'admin',
                name:'admin',
                component:()=>import('../components/admin/AdminManage')
            },
            {
                path:'user',
                name:'user',
                component:()=>import('../components/user/UserManage')
            }
        ]
    }
]

const router = new VueRouter({
    mode:'history',
    routes
})

const VueRouterPush = VueRouter.prototype.push
VueRouter.prototype.push = function push (to) {
    return VueRouterPush.call(this, to).catch(err => err)
}

export  default router;