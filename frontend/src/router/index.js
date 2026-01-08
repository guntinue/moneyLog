import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import TimeStatisticsView from '../views/TimeStatisticsView.vue'
import BudgetView from '../views/BudgetView.vue'

const routes = [
    { path: '/', component: HomeView },
    { path: '/stats', component: TimeStatisticsView },
    { path: '/budget', component: BudgetView }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
