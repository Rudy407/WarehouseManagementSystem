import vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";
import createPersistedState from'vuex-persistedstate'

vue.use(Vuex)

function addNewRoute(menuList) {
    let routes=router.options.routes;
    console.log(menuList);
    console.log(routes);
    routes.forEach((routeItem)=>{
        if(routeItem.path=='/index'){
            menuList.forEach(menu=>{
                let newRoute = {
                    path:menu.menuclick,
                    name:menu.menuname,
                    meta:{
                        title:menu.menuname
                    },
                    component:()=>import('../components/'+menu.menucomponent)
                }
                routeItem.children.push(newRoute);
            })
        }
    })
    resetRouter();
    router.addRoutes(routes);
}

export default new Vuex.Store({
    state: {
        menu: []
    },
    mutations: {
        setMenu(state,menuList) {
            state.menu = menuList;
            addNewRoute(menuList);
        }
    },
    getters: {
        getMenu(state) {
            return state.menu
        }
    },
    plugins:[createPersistedState()]
})