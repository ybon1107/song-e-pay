<template>
    <Modal :title="title" :confirm-text="confirmText" @confirm="handleConfirm">
        <p>사칭사기에 주의하세요.</p>
        <h2>결제 비밀번호</h2>

        <!-- 비밀번호 도트 -->
        <!-- <div class="password-dots">
            <span v-for="(digit, index) in 6" :key="index"
                :class="{ dot: true, filled: password.length >= index + 1 }"></span>
        </div> -->

        <!-- 숫자 키패드 -->
        <!-- <div class="keypad">
            <button v-for="num in 9" :key="num" @click="enterDigit(num)">{{ num }}</button>
            <button @click="clearAll">C</button>
            <button @click="enterDigit(0)">0</button>
            <button @click="clearLast"><i class="fas fa-backspace"></i></button>
        </div> -->
    </Modal>
</template>

<script setup>
import Modal from '@/components/modal/Modal.vue';
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
    title: {
        type: String,
        default: '⚠️ 잠깐 !!!!'
    },
    confirmText: {
        type: String,
        default: '저장'
    }
});

const emit = defineEmits(['confirm']);

const handleConfirm = () => {
    emit('confirm');
};
</script>