<template>
  <div class="stats-view">
    <h1>시간대별 지출 통계</h1>
    <div class="chart-container" v-if="loaded">
       <Doughnut :data="chartData" :options="chartOptions" />
    </div>
    <div v-else>Loading...</div>
    <div class="debug-info">
        <p>Backend API: /api/stats/hourly</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js'
import { Doughnut } from 'vue-chartjs'
import axios from 'axios'

ChartJS.register(ArcElement, Tooltip, Legend)

const loaded = ref(false)
const chartData = ref({ labels: [], datasets: [] })
const chartOptions = { responsive: true, maintainAspectRatio: false }

onMounted(async () => {
    try {
        // Attempt to fetch from API
        // const response = await axios.get('/api/stats/hourly', { params: { start: new Date().toISOString(), end: new Date().toISOString() }})
        // const data = response.data.data
        // Process data...
        
        // For demonstration purposes (Restoration), setting mock data initially
        // to ensure the chart renders immediately.
        chartData.value = {
            labels: ['08:00', '12:00', '18:00', '20:00', '23:00'],
            datasets: [{
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF'],
                data: [15000, 25000, 40000, 10000, 30000]
            }]
        }
        loaded.value = true
    } catch (e) {
        console.error(e)
    }
})
</script>

<style scoped>
.stats-view {
    padding: 20px;
}
.chart-container {
    height: 400px;
    width: 400px;
    margin: 0 auto;
    position: relative;
}
</style>
