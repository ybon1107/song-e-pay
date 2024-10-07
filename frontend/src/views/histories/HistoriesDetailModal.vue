<template>
    <Modal title="histories--modal-title" :isVisible="isVisible" :showFooter="false" @close="closeModal">
        <h4>
            <span :class="getBadgeClass(transaction?.typeCode)">{{
                $t(transaction?.i18nType)
                }}</span>
        </h4>
        <p class="font-weight-bold mb-0 mt-3">
            {{ transaction?.historyContent }}
        </p>
        <p>
            <span class="title me-1">{{ transaction?.amount }}</span
            >
            <span
                v-if="[TRANSACTION_TYPES.DEPOSIT, TRANSACTION_TYPES.EXCHANGE, TRANSACTION_TYPES.REFUND].includes(transaction.typeCode)">
                {{ songCurrencyUnit }}
            </span>
            <span
                v-else-if="[TRANSACTION_TYPES.RE_EXCHANGE, TRANSACTION_TYPES.TRANSFER, TRANSACTION_TYPES.PAYMENT].includes(transaction.typeCode)">
                KRW
            </span>
        </p>
        <hr />

        <div class="d-flex justify-content-between align-items-center mb-3">
            <strong>{{$t('histories--modal-memo')}}</strong>
            <button @click="updateMemo" class="btn btn-secondary btn-sm mb-0">
                {{ $t('btn-save') }}
            </button>
        </div>
        <argon-alert v-if="showAlert" class="mt-3" :color="alertColor">
            {{ alertMessage }}
        </argon-alert>
        <textarea v-model="localMemo" class="form-control fixed-textarea" id="memo" rows="3"
        :placeholder="$t('histories--modal-enterMemo')"></textarea>
        <hr />

        <div class="flex-justify-between" v-if="[TRANSACTION_TYPES.EXCHANGE, TRANSACTION_TYPES.RE_EXCHANGE].includes(transaction?.typeCode)">
            <strong>환율</strong>
            <p>{{ transaction?.exchangeRate }}</p>
        </div>
        <!-- <div class="flex-justify-between" v-if="transaction?.typeCode === '송금'">
            <strong>계좌</strong> 
            <p>{{ transaction?.exchangeRate }}송금 보낸 이메일 보여주기 </p>
        </div> -->
        <div class="flex-justify-between">
            <strong>{{$t('histories--modal-historyDate')}}</strong>
            <p>{{ transaction?.historyDate }}</p>
        </div>
        <div class="flex-justify-between">
            <strong>{{$t('histories--modal-stateCode')}}</strong>
            <p>{{ transaction?.i18nState }}</p>
        </div>
    </Modal>
</template>

<script setup>
import { ref, watch,computed } from 'vue';
import axios from 'axios';
import Modal from '@/components/modal/Modal.vue';
import {
    TRANSACTION_TYPES,
    TRANSACTION_TYPES_STRING_KO,
} from '@/constants/transactionType';
import { CURRENCY_NAME } from '@/constants/countryCode';

import ArgonAlert from '@/components/templates/ArgonAlert.vue';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

//user
import { useAuthStore } from '@/stores/auth';
const auth = useAuthStore();
const user = computed(() => auth.user);

const songCurrencyUnit = CURRENCY_NAME[user.value.countryCode];

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
            TRANSACTION_TYPES.DEPOSIT,
            TRANSACTION_TYPES.EXCHANGE,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-primary`;
    } else if (
        [
            TRANSACTION_TYPES.REFUND,
            TRANSACTION_TYPES.RE_EXCHANGE,
        ].includes(typeCode)
    ) {
        return `${baseClasses} bg-danger`;
    } else if (
        [
            TRANSACTION_TYPES.PAYMENT,
            TRANSACTION_TYPES.TRANSFER,
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
