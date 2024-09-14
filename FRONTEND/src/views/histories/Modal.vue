<template>
  <div class="modal fade show" style="display: block;" tabindex="-1" role="dialog" v-if="isVisible" @click.self="closeModal">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ transaction.type }} 내역 상세</h5>

        </div>
        <div class="modal-body">
          <h2 class="text-start">{{ transaction.detail }}</h2>
          <h4 class="text-end font-weight-bold">{{ transaction.amount }}</h4>
          <h4 class="text-end font-wight-bold" v-if="['환전', '환급'].includes(transaction.type)"><strong>환율:</strong> {{ transaction.exchangeRate }}</h4>
          <p v-if="transaction.type === '충전'"><hr><strong>금액:</strong> {{ transaction.amount }}</p>
          <p v-if="transaction.type === '충전'"><hr><strong>계좌:</strong> {{ transaction.account }}</p>
          <p><hr><strong>결제 일시:</strong> {{ transaction.date }}</p>
          <p v-if="['송금', '결제', '환불', '환급', '환전'].includes(transaction.type)"><hr><strong>거래 후 잔액:</strong> {{ transaction.balance }}</p>
          <p v-if="['환전', '환급'].includes(transaction.type)"><hr><strong>환전 후 잔액:</strong> {{ transaction.balanceAfterExchange }}</p>
          <p v-if="transaction.type === '결제'"><hr><strong>승인 번호:</strong> {{ transaction.approvalNumber }}</p>
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
  emit('updateMemo', localMemo.value);
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

textarea {
  border-radius: 5px;
}

</style>