import { createRouter, createWebHistory } from 'vue-router'
import TimeStatisticsView from '../views/TimeStatisticsView.vue'

const routes = [
    { path: '/', redirect: '/stats' },
    { path: '/stats', component: TimeStatisticsView }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
