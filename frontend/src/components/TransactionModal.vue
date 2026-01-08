<template>
  <div v-if="isOpen" class="modal-overlay" @click.self="close">
    <div class="modal-content">
      <h2>{{ isEdit ? '지출/수입 상세' : '새 내역 추가' }}</h2>
      
      <div class="form-group">
        <label>타이틀</label>
        <input v-model="form.title" placeholder="예: 점심 식사" :disabled="isEdit" />
      </div>

      <div class="form-group">
        <label>금액</label>
        <input type="number" v-model.number="form.amount" placeholder="0" :disabled="isEdit" />
      </div>

      <div class="form-group">
        <label>카테고리</label>
        <div v-if="!isEdit" class="category-input-group">
            <select v-model="form.categorySelect" @change="handleCategoryChange">
                <option value="FOOD">식비</option>
                <option value="TRANSPORT">교통비</option>
                <option value="SHOPPING">쇼핑</option>
                <option value="ETC">기타</option>
                <option value="CUSTOM">직접 입력...</option>
            </select>
            <input v-if="form.categorySelect === 'CUSTOM'" v-model="form.customCategory" placeholder="카테고리 입력" />
        </div>
        <input v-else v-model="form.category" disabled />
      </div>

      <div class="form-group">
        <label>지출 내역 (상세)</label>
        <textarea v-model="form.description" placeholder="상세 내용을 입력하세요" :disabled="isEdit"></textarea>
      </div>

      <div class="form-group">
        <label>날짜</label>
        <input type="datetime-local" v-model="form.transactionTime" :disabled="isEdit" />
      </div>

      <div class="actions">
        <button v-if="!isEdit" @click="save" class="btn-primary">저장</button>
        <button @click="close" class="btn-secondary">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineExpose, defineEmits } from 'vue'
import axios from 'axios'

const emit = defineEmits(['saved', 'close'])

const isOpen = ref(false)
const isEdit = ref(false)
const form = ref({
    title: '',
    amount: 0,
    categorySelect: 'FOOD',
    customCategory: '',
    category: '', // Final value
    description: '',
    transactionTime: ''
})

const MEMBER_ID = 1;

const open = (data = null, defaultDate = null) => {
    isOpen.value = true
    if (data) {
        isEdit.value = true
        form.value = { ...data, categorySelect: 'CUSTOM' } // Simplified
    } else {
        isEdit.value = false
        const now = defaultDate ? new Date(defaultDate) : new Date()
        now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
        form.value = {
            title: '',
            amount: 0,
            categorySelect: 'FOOD',
            customCategory: '',
            category: 'FOOD',
            description: '',
            transactionTime: now.toISOString().slice(0, 16)
        }
    }
}

const close = () => {
    isOpen.value = false
    emit('close')
}

const handleCategoryChange = () => {
    if (form.value.categorySelect !== 'CUSTOM') {
        form.value.category = form.value.categorySelect
    }
}

const save = async () => {
    const finalCategory = form.value.categorySelect === 'CUSTOM' ? form.value.customCategory : form.value.categorySelect
    
    try {
        await axios.post(`/api/money-log?memberId=${MEMBER_ID}`, {
            title: form.value.title,
            amount: form.value.amount,
            category: finalCategory,
            description: form.value.description,
            transactionTime: form.value.transactionTime
        })
        alert('저장되었습니다.')
        emit('saved')
        close()
    } catch (e) {
        console.error(e)
        alert('저장 실패')
    }
}

defineExpose({ open })
</script>

<style scoped>
/* [디자인 수정] 모달 배경 및 박스 스타일 */
.modal-overlay {
    position: fixed; top: 0; left: 0; width: 100%; height: 100%;
    background: rgba(0, 0, 0, 0.5); display: flex; justify-content: center; align-items: center; z-index: 1000;
}
.modal-content {
    background: white; padding: 25px; border-radius: 12px;
    width: 400px; max-width: 90%;
    box-shadow: 0 4px 15px rgba(0,0,0,0.2);
    color: #333; /* [디자인 수정] 텍스트 색상 */
}

.form-group { margin-bottom: 15px; text-align: left; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; color: #555; }
input, select, textarea {
    width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; box-sizing: border-box;
    font-size: 14px;
}
.category-input-group { display: flex; gap: 5px; }

.actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
.btn-primary { background: #42b983; color: white; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer; }
.btn-secondary { background: #ccc; color: #333; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer; }
button:hover { opacity: 0.9; }
</style>
