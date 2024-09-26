<template>
    <Modal :title="title" :confirm-text="confirmText" @confirm="handleConfirm">
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