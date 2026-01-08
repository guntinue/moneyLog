<template>
  <div class="recent-logs">
    <h3>최근 내역 (5건)</h3>
    <div v-if="logs.length === 0" class="empty-state">내역이 없습니다.</div>
    <ul v-else class="log-list">
      <li v-for="log in logs" :key="log.id" @click="$emit('view', log)" class="log-item">
        <div class="log-header">
            <span class="log-title">{{ log.title || '제목 없음' }}</span>
            <span class="log-date">{{ formatDate(log.transactionTime) }}</span>
        </div>
        <div class="log-body">
            <span class="log-category badge">{{ log.category }}</span>
            <span class="log-amount">{{ log.amount.toLocaleString() }}원</span>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
    logs: Array
})

const formatDate = (dateStr) => {
    const date = new Date(dateStr)
    return `${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`
}
</script>

<style scoped>
/* [디자인 수정] 최근 내역 리스트 스타일 */
.recent-logs {
    background: #f9f9f9; /* [디자인 수정] 배경색 */
    padding: 20px;
    border-radius: 12px;
    border: 1px solid #eee;
    height: 100%;
}
.log-list { list-style: none; padding: 0; margin: 0; }
.log-item {
    background: white; border: 1px solid #eee; border-radius: 8px;
    padding: 12px; marginBottom: 10px; cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;
}
.log-item:hover { transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.05); }

.log-header { display: flex; justify-content: space-between; margin-bottom: 5px; font-size: 0.9em; }
.log-title { font-weight: bold; color: #333; }
.log-date { color: #888; font-size: 0.8em; }

.log-body { display: flex; justify-content: space-between; align-items: center; }
.log-amount { color: #d32f2f; font-weight: bold; } /* 지출 색상 */
.badge { background: #e0f2f1; color: #00695c; padding: 2px 6px; border-radius: 4px; font-size: 0.8em; }
</style>
