<template>
  <div class="modal fade show" style="display: block;" tabindex="-1" role="dialog" v-if="isVisible" @click.self="closeModal">
    <div class="modal-dialog modal-lg" role="document">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">{{ transaction.typeCode }} 내역 상세</h5>
        </div>
        <div class="modal-body">
          <h2 class="text-start">{{ transaction.historyContent }}</h2>
          <h4 class="text-end font-weight-bold">{{ transaction.amount }}</h4>
          <h4 class="text-end font-weight-bold" v-if="['환전', '환급'].includes(transaction.typeCode)"><strong>환율:</strong> {{ transaction.exchangeRate }}</h4>
          <!-- <p v-if="transaction.typeCode === '충전'"><hr> <strong>출금 계좌:</strong> {{ transaction.원래 외국 계좌 }}</p> -->
          <!-- <p v-if="transaction.typeCode === '송금'"><hr> <strong>계좌:</strong> {{ transaction.송금 받을 krwNo}}</p> -->
          <p><hr><strong>결제 일시:</strong> {{ transaction.historyDate }}</p>
          <!-- <p v-if="['송금', '결제', '환불', '환급', '환전'].includes(transaction.typeCode)"><hr><strong>거래 후 원화:</strong> {{ transaction.한국 계좌 잔액 }}</p> -->
          <!-- <p v-if="['환전', '환급', '충전', '환불'].includes(transaction.typeCode)"><hr><strong>거래 후 송이 계좌 잔액:</strong> {{ transaction.송이 계좌 잔액 }}</p>>  -->
          <!-- <p v-if="transaction.typeCode === '결제'"><hr><strong>승인 번호:</strong> {{ transaction.결제 승인 번호 ?  }}</p> -->
          <p><hr><strong>거래 상태:</strong> {{  transaction.stateCode }}</p>
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
