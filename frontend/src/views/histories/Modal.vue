<template>
  <div class="modal fade show d-flex justify-content-center align-items-center" style="display: block;" tabindex="-1" role="dialog" v-if="isVisible" @click.self="closeModal">
    <div class="modal-dialog modal-lg modal-centered" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ transaction.typeCode }} 내역 상세</h5>
        </div>
        <div class="modal-body">
          <h2 class="text-start">{{ transaction.historyContent }}</h2>
          <h4 class="text-end font-weight-bold">{{ transaction.amount }}</h4>
          <h4 class="text-end font-weight-bold" v-if="['환전', '환급'].includes(transaction.typeCode)"><strong>환율:</strong> {{ transaction.exchangeRate }}</h4>
          <p><hr><strong>결제 일시:</strong> {{ transaction.historyDate }}</p>
          <p><hr><strong>거래 상태:</strong> {{ transaction.stateCode }}</p>
          <div class="form-group">
            <hr>
            <label for="memo">메모</label>
            <textarea v-model="localMemo" class="form-control" id="memo" rows="3" placeholder="메모를 입력하세요"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="closeModal">닫기</button>
          <button type="button" class="btn btn-primary" @click="updateMemo">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';

const props = defineProps({
  transaction: Object,
  isVisible: Boolean
});

const emit = defineEmits(['close', 'updateMemo']);
const localMemo = ref(props.transaction ? props.transaction.memo : '');

watch(() => props.transaction, (newTransaction) => {
  localMemo.value = newTransaction ? newTransaction.memo : '';
});

const updateMemo = () => {
  if (props.transaction) {
    axios
      .post('/api/histories/updateMemo', {
        historyNo: props.transaction.historyNo,
        memo: localMemo.value
      })
      .then(response => {
        emit('updateMemo', { historyNo: props.transaction.historyNo, memo: localMemo.value });
        alert('메모가 성공적으로 저장되었습니다.');
        emit('close');
      })
      .catch(error => {
        console.error('메모 저장 중 오류 발생:', error);
        alert('메모 저장에 실패했습니다.');
      });
  }
};

const closeModal = () => {
  emit('close');
};
</script>

<style scoped>
.modal-content {
  border-radius: 10px;
  border: none;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
  max-width: 600px; /* 모달의 최대 너비 설정 */
  width: 100%; /* 화면에 맞게 너비를 100% 설정 */
}

.modal-header {
  background-color: #f9f9f9;
  border-bottom: none;
}

.modal-title {
  font-weight: bold;
}

.text-center {
  text-align: center;
}

.modal-footer .btn-primary {
  background-color: #f9d71c;
  border-color: #f9d71c;
}

.modal-footer .btn-primary:hover {
  background-color: #e0be14;
  border-color: #e0be14;
}

.modal-footer .btn-secondary {
  background-color: #6c757d;
}

.textarea {
  border-radius: 5px;
}

/* 모달을 화면 중앙에 위치시키기 위한 스타일 */
.modal.fade.show {
  display: flex !important;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 모달이 화면 중앙에 고정되도록 설정 */
}

.modal-dialog {
  max-width: 600px; /* 모달의 가로 너비를 제한 */
  width: 100%;
}

.modal-body {
  text-align: left;
  max-height: 70vh; /* 모달의 세로 높이를 제한 */
  overflow-y: auto; /* 내용이 길 경우 스크롤 표시 */
}
</style>
