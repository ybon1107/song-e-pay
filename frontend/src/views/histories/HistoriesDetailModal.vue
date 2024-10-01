<template>
    <Modal
        title="내역 상세"
        :isVisible="isVisible"
        :showFooter="false"
        @close="closeModal"
    >
        <h4>
            <span :class="getBadgeClass(transaction?.typeCode)">{{
                transaction?.typeCode
            }}</span>
        </h4>
        <p class="font-weight-bold mb-0 mt-3">
            {{ transaction?.historyContent }}
        </p>
        <p>
            <span class="title me-1">{{ transaction?.amount }}</span
            >won
        </p>
        <hr />

        <div class="d-flex justify-content-between align-items-center mb-3">
            <strong>메모남기기</strong>
            <button @click="updateMemo" class="btn btn-secondary btn-sm mb-0">
                저장
            </button>
        </div>
        <argon-alert v-if="showAlert" class="mt-3" :color="alertColor">
            {{ alertMessage }}
        </argon-alert>
        <textarea
            v-model="localMemo"
            class="form-control fixed-textarea"
            id="memo"
            rows="3"
            placeholder="메모를 입력하세요"
        ></textarea>
        <hr />

        <div
            class="flex-justify-between"
            v-if="['환전', '환급'].includes(transaction?.typeCode)"
        >
            <strong>환율</strong>
            <p>{{ transaction?.exchangeRate }}</p>
        </div>
        <div
            class="flex-justify-between"
            v-if="transaction?.typeCode === '송금'"
        >
            <strong>계좌</strong>
            <p>{{ transaction?.exchangeRate }}</p>
        </div>
        <div class="flex-justify-between">
            <strong>결제 일시</strong>
            <p>{{ transaction?.historyDate }}</p>
        </div>
        <div class="flex-justify-between">
            <strong>거래 상태</strong>
            <p>{{ transaction?.stateCode }}</p>
        </div>
    </Modal>
</template>

<script setup>
import { ref, watch } from 'vue';
import axios from 'axios';
import Modal from '@/components/modal/Modal.vue';
import {
    TRANSACTION_TYPES,
    TRANSACTION_TYPES_STRING,
} from '@/constants/transactionType';
import ArgonAlert from '@/components/templates/ArgonAlert.vue';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

const props = defineProps({
    transaction: {
        type: Object,
        default: () => ({}),
    },
    isVisible: Boolean,
});

const emit = defineEmits(['close', 'updateMemo']);
const localMemo = ref(props.transaction ? props.transaction.memo : '');
const showAlert = ref(false);
const alertMessage = ref('');
const alertColor = ref('success');

watch(
    () => props.transaction,
    (newTransaction) => {
        localMemo.value = newTransaction?.memo || '';
    }
);

const getBadgeClass = (typeCode) => {
    const baseClasses = 'badge rounded-pill';
    if (
        [
            TRANSACTION_TYPES_STRING.DEPOSIT,
            TRANSACTION_TYPES_STRING.EXCHANGE,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-primary`;
    } else if (
        [
            TRANSACTION_TYPES_STRING.REFUND,
            TRANSACTION_TYPES_STRING.RE_EXCHANGE,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-danger`;
    } else if (
        [
            TRANSACTION_TYPES_STRING.PAYMENT,
            TRANSACTION_TYPES_STRING.TRANSFER,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-success`;
    }
    return `${baseClasses} bg-secondary`;
};

const updateMemo = () => {
    if (props.transaction) {
        axios
            .post('/api/histories/updateMemo', {
                historyNo: props.transaction.historyNo,
                memo: localMemo.value,
            })
            .then(() => {
                // emit 이벤트로 부모에게 업데이트된 메모를 전송
                emit('updateMemo', {
                    historyNo: props.transaction.historyNo,
                    memo: localMemo.value,
                });
                showAlert.value = true;
                alertMessage.value = '메모가 성공적으로 저장되었습니다.';
                alertColor.value = 'success';
                setTimeout(() => {
                    showAlert.value = false;
                }, 3000); // 3초 후 알림 숨김
            })
            .catch((error) => {
                console.error('메모 저장 중 오류 발생:', error);
                Swal.fire({
                    title: '실패',
                    text: '메모 저장에 실패했습니다.',
                    icon: 'error',
                });
            });
    }
};

const closeModal = () => {
    emit('close');
};
</script>

<style scoped>
.title {
    font-size: 2.25rem;
    line-height: 1.3;
    font-weight: 700;
    color: #344767;
}

.flex-justify-between {
    display: flex;
    justify-content: space-between;
}

.fixed-textarea {
    resize: none;
}
</style>
