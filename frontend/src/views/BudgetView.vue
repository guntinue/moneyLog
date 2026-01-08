<template>
  <div class="budget-view">
    <h1>예산 목표 및 달성 현황</h1>

    <div class="budget-form">
      <h3>목표 설정</h3>
      <div class="form-group">
        <label>카테고리</label>
        <select v-model="form.category">
          <option value="FOOD">식비</option>
          <option value="TRANSPORT">교통비</option>
          <option value="SHOPPING">쇼핑</option>
          <option value="ETC">기타</option>
        </select>
      </div>
      <div class="form-group">
        <label>목표 금액</label>
        <input type="number" v-model.number="form.targetAmount" placeholder="금액 입력" />
      </div>
      <button @click="setBudget">설정하기</button>
    </div>

    <div class="dashboard">
      <h3>이번 달 현황 ({{ currentYearMonth }})</h3>
      <div class="budget-cards">
        <div v-for="stat in budgets" :key="stat.budgetId" class="budget-card">
          <div class="card-header">
            <span class="category">{{ getCategoryLabel(stat.category) }}</span>
            <span class="status-badge" :class="getStatusClass(stat.ratio)">{{ getStatusLabel(stat.ratio) }}</span>
          </div>
          <div class="card-body">
            <div class="amounts">
              <span>{{ formatMoney(stat.currentAmount) }}</span>
              <span class="divider">/</span>
              <span class="target">{{ formatMoney(stat.targetAmount) }}</span>
            </div>
            <div class="progress-bar-container">
              <div class="progress-bar" :style="{ width: Math.min(stat.ratio, 100) + '%', backgroundColor: getStatusColor(stat.ratio) }"></div>
            </div>
            <div class="ratio-text">{{ stat.ratio.toFixed(1) }}% 사용</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const form = ref({
  category: 'FOOD',
  targetAmount: 0
})

const budgets = ref([])
const currentYearMonth = ref(new Date().toISOString().slice(0, 7))

const MEMBER_ID = 1; 

const setBudget = async () => {
    try {
        await axios.post(`/api/budget?memberId=${MEMBER_ID}`, {
            category: form.value.category,
            targetAmount: form.value.targetAmount,
            yearMonth: currentYearMonth.value
        })
        alert('목표가 설정되었습니다.')
        fetchStatus()
    } catch (e) {
        alert('설정 실패')
        console.error(e)
    }
}

const fetchStatus = async () => {
    try {
        const response = await axios.get(`/api/budget/status?memberId=${MEMBER_ID}&yearMonth=${currentYearMonth.value}`)
        budgets.value = response.data.data
    } catch (e) {
        console.error(e)
    }
}

const getCategoryLabel = (code) => {
    const map = { 'FOOD': '식비', 'TRANSPORT': '교통비', 'SHOPPING': '쇼핑', 'ETC': '기타' }
    return map[code] || code
}

const getStatusColor = (ratio) => {
    if (ratio < 50) return '#4caf50' 
    if (ratio < 80) return '#ffeb3b' 
    return '#f44336' 
}

const getStatusClass = (ratio) => {
    if (ratio < 50) return 'safe'
    if (ratio < 80) return 'warning'
    return 'danger'
}

const getStatusLabel = (ratio) => {
    if (ratio < 50) return '여유'
    if (ratio < 80) return '주의'
    if (ratio <= 100) return '위험'
    return '초과'
}

const formatMoney = (val) => val.toLocaleString() + '원'

onMounted(() => {
    fetchStatus()
})
</script>

<style scoped>
.budget-view { padding: 20px; max-width: 800px; margin: 0 auto; }
.budget-form { background: #333; padding: 20px; border-radius: 8px; margin-bottom: 30px; }
.form-group { margin-bottom: 10px; text-align: left; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
input, select { padding: 8px; width: 100%; box-sizing: border-box; }
button { padding: 10px 20px; background: #42b983; color: white; border: none; cursor: pointer; border-radius: 4px; margin-top: 10px; }
.budget-cards { display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px; }
.budget-card { background: #444; border-radius: 8px; padding: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; }
.category { font-size: 1.2em; font-weight: bold; }
.status-badge { padding: 4px 8px; border-radius: 12px; font-size: 0.8em; color: #000; font-weight: bold; }
.status-badge.safe { background: #a5d6a7; }
.status-badge.warning { background: #fff59d; }
.status-badge.danger { background: #ef9a9a; }
.amounts { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 10px; }
.divider { margin: 0 5px; color: #888; }
.target { color: #aaa; font-size: 0.9em; }
.progress-bar-container { background: #222; height: 10px; border-radius: 5px; overflow: hidden; margin-bottom: 5px; }
.progress-bar { height: 100%; transition: width 0.3s ease; }
.ratio-text { text-align: right; font-size: 0.8em; color: #ddd; }
</style>
