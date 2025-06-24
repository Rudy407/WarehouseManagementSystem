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
        component:()=>import('../components/IndexPage')
    }
]

const router = new VueRouter({
    mode:'history',
    routes
})

export  default router;