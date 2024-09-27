<template>
  <Modal :title="`${transaction?.typeCode || '거래'} 내역 상세`" :isVisible="isVisible" @close="closeModal"
    @confirm="updateMemo" confirmText="저장">

    <h2 class="text-start">{{ transaction?.historyContent }}</h2>
    <h4 class="text-end font-weight-bold">{{ transaction?.amount }}</h4>
    <h4 class="text-end font-weight-bold" v-if="['환전', '환급'].includes(transaction?.typeCode)">
      <strong>환율:</strong> {{ transaction?.exchangeRate }}
    </h4>
    <p v-if="transaction?.typeCode === '송금'">
      <hr> <strong>계좌:</strong> {{ transaction?.historyContent }}
    </p>
    <p>
      <hr><strong>결제 일시:</strong> {{ transaction?.historyDate }}
    </p>
    <p>
      <hr><strong>거래 상태:</strong> {{ transaction?.stateCode }}
    </p>
    <div class="form-group">
      <hr>
      <label for="memo">메모</label>
      <textarea v-model="localMemo" class="form-control" id="memo" rows="3" placeholder="메모를 입력하세요"></textarea>
    </div>
  </Modal>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import Modal from '@/components/modal/Modal.vue';

const props = defineProps({
  transaction: {
    type: Object,
    default: () => ({})
  },
  isVisible: Boolean
});

const emit = defineEmits(['close', 'updateMemo']);
const localMemo = ref(props.transaction ? props.transaction.memo : '');

watch(() => props.transaction, (newTransaction) => {
  localMemo.value = newTransaction?.memo || '';
});

const updateMemo = () => {
  if (props.transaction) {
    axios
      .post('/api/histories/updateMemo', {
        historyNo: props.transaction.historyNo,
        memo: localMemo.value
      })
      .then(response => {
        // emit 이벤트로 부모에게 업데이트된 메모를 전송
        emit('updateMemo', { historyNo: props.transaction.historyNo, memo: localMemo.value });
        alert('메모가 성공적으로 저장되었습니다.');
        emit('close'); // 모달을 닫음
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

<style scoped></style>