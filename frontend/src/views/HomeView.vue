<template>
  <div class="home-container">
    <!-- 메인 캘린더 영역 (좌측 70%) -->
    <div class="calendar-section">
      <div class="calendar-header">
        <button @click="prevMonth">&lt;</button>
        <h2>{{ currentYear }}.{{ currentMonth + 1 }}</h2>
        <button @click="nextMonth">&gt;</button>
        <button class="today-btn" @click="goToday">Today</button>
      </div>

      <div class="calendar-grid">
        <!-- 요일 헤더 -->
        <div class="weekday" v-for="day in weekdays" :key="day">{{ day }}</div>
        
        <!-- 날짜 칸 -->
        <div 
            v-for="(date, index) in calendarDays" 
            :key="index" 
            class="day-cell" 
            :class="{ 'other-month': !date.isCurrentMonth, 'today': isToday(date.dateObj) }"
            @click="openDateView(date)"
        >
            <div class="date-num">{{ date.day }}</div>
            <div class="day-content">
                <div v-for="log in getLogsForDate(date.dateStr)" :key="log.id" class="mini-log">
                    • {{ log.title }}
                </div>
                <div v-if="getLogsForDate(date.dateStr).length > 3" class="more-logs">...</div>
            </div>
        </div>
      </div>
    </div>

    <!-- 사이드바 영역 (우측 30%) -->
    <div class="sidebar-section">
        <div class="add-button-container">
            <button class="add-btn" @click="openAddModal">+ 새 내역 추가</button>
        </div>
        <RecentLogs :logs="recentLogs" @view="openDetailModal" />
    </div>

    <!-- 모달 -->
    <TransactionModal ref="modal" @saved="refreshData" />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import RecentLogs from '../components/RecentLogs.vue'
import TransactionModal from '../components/TransactionModal.vue'

// --- State ---
const currentDate = ref(new Date())
const monthlyLogs = ref([])
const recentLogs = ref([])
const modal = ref(null)

const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']
const MEMBER_ID = 1;

// --- Computed ---
const currentYear = computed(() => currentDate.value.getFullYear())
const currentMonth = computed(() => currentDate.value.getMonth())

const calendarDays = computed(() => {
    const year = currentYear.value
    const month = currentMonth.value
    const firstDayOfMonth = new Date(year, month, 1).getDay()
    const lastDateOfMonth = new Date(year, month + 1, 0).getDate()
    const lastDateOfPrevMonth = new Date(year, month, 0).getDate()

    const days = []

    // Prev Month days
    for (let i = firstDayOfMonth - 1; i >= 0; i--) {
        const d = lastDateOfPrevMonth - i
        days.push({ 
            day: d, 
            isCurrentMonth: false, 
            dateObj: new Date(year, month - 1, d),
            dateStr: formatDateStr(new Date(year, month - 1, d))
        })
    }

    // Current Month days
    for (let i = 1; i <= lastDateOfMonth; i++) {
        days.push({ 
            day: i, 
            isCurrentMonth: true, 
            dateObj: new Date(year, month, i),
            dateStr: formatDateStr(new Date(year, month, i))
        })
    }

    // Next Month days
    const remainingCells = 42 - days.length // 6 rows * 7 cols
    for (let i = 1; i <= remainingCells; i++) {
        days.push({ 
            day: i, 
            isCurrentMonth: false, 
            dateObj: new Date(year, month + 1, i),
            dateStr: formatDateStr(new Date(year, month + 1, i))
        })
    }

    return days
})

// --- Methods ---
const formatDateStr = (date) => {
    return date.toISOString().slice(0, 10)
}

const isToday = (date) => {
    const today = new Date()
    return date.getDate() === today.getDate() &&
           date.getMonth() === today.getMonth() &&
           date.getFullYear() === today.getFullYear()
}

const getLogsForDate = (dateStr) => {
    return monthlyLogs.value.filter(log => log.transactionTime.startsWith(dateStr))
}

const prevMonth = () => {
    currentDate.value = new Date(currentYear.value, currentMonth.value - 1, 1)
    fetchMonthlyLogs()
}

const nextMonth = () => {
    currentDate.value = new Date(currentYear.value, currentMonth.value + 1, 1)
    fetchMonthlyLogs()
}

const goToday = () => {
    currentDate.value = new Date()
    fetchMonthlyLogs()
}

// API Calls
const fetchMonthlyLogs = async () => {
    const yearMonth = `${currentYear.value}-${String(currentMonth.value + 1).padStart(2, '0')}`
    try {
        const res = await axios.get(`/api/money-log/monthly?memberId=${MEMBER_ID}&yearMonth=${yearMonth}`)
        monthlyLogs.value = res.data.data
    } catch (e) {
        console.error(e)
    }
}

const fetchRecentLogs = async () => {
    try {
        const res = await axios.get(`/api/money-log/recent?memberId=${MEMBER_ID}`)
        recentLogs.value = res.data.data
    } catch (e) {
        console.error(e)
    }
}

const refreshData = () => {
    fetchMonthlyLogs()
    fetchRecentLogs()
}

// Modal Actions
const openAddModal = () => {
    modal.value.open(null, new Date()) // Add new
}

const openDateView = (dateBlock) => {
    // When clicking a day, maybe show all logs?
    // For now, let's open "Add" modal pre-filled with that date if empty, or "View" if click specific log?
    // Requirement says "date click -> view modal". 
    // Let's simplified: Click date -> Open Add Modal for that date (UX choice for now as View Modal logic is complex for list)
    // Or better: pass the date to modal
    modal.value.open(null, dateBlock.dateObj) 
}

const openDetailModal = (log) => {
    modal.value.open(log) 
}

onMounted(() => {
    refreshData()
})
</script>

<style scoped>
.home-container {
    display: flex; height: 100vh;
    background: white; /* [디자인 수정] 전체 배경 화이트 */
    color: #333;
}

/* 캘린더 영역 */
.calendar-section { flex: 7; padding: 20px; display: flex; flex-direction: column; }
.calendar-header { display: flex; justify-content: center; align-items: center; margin-bottom: 20px; gap: 20px; }
.calendar-header button { background: none; border: 1px solid #ddd; padding: 5px 10px; cursor: pointer; border-radius: 4px; }
.calendar-header h2 { margin: 0; font-size: 1.5em; }

.calendar-grid { 
    display: grid; grid-template-columns: repeat(7, 1fr); 
    flex-grow: 1; border-top: 1px solid #eee; border-left: 1px solid #eee; 
}
.weekday { text-align: center; padding: 10px; background: #f9f9f9; font-weight: bold; border-right: 1px solid #eee; border-bottom: 1px solid #eee; }
.day-cell { 
    border-right: 1px solid #eee; border-bottom: 1px solid #eee; 
    padding: 5px; min-height: 80px; position: relative; cursor: pointer; flex-direction: column; display: flex;
}
.day-cell:hover { background: #f5f5f5; }
.day-cell.other-month { background: #fafafa; color: #ccc; }
.day-cell.today { background: #e8f5e9; } /* [디자인 수정] 오늘 날짜 강조색 */

.date-num { font-weight: bold; margin-bottom: 5px; font-size: 0.9em; }
.mini-log { font-size: 0.75em; color: #555; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 2px; }
.more-logs { font-size: 0.7em; color: #aaa; text-align: center; }

/* 사이드바 영역 */
.sidebar-section { flex: 3; padding: 20px; border-left: 1px solid #eee; background: white; display: flex; flex-direction: column; }
.add-button-container { text-align: right; margin-bottom: 20px; }
.add-btn { 
    background: #42b983; color: white; border: none; padding: 10px 20px; 
    border-radius: 20px; cursor: pointer; font-weight: bold; box-shadow: 0 2px 5px rgba(0,0,0,0.1); 
    /* [디자인 수정] + 버튼 스타일 */
}
</style>
