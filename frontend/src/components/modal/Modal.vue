<template>
    <teleport to="#modal-container">
        <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="exampleModalLabel">{{ title }}</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <slot></slot>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">{{ cancelText
                            }}</button>
                        <button type="button" class="btn btn-primary" @click="$emit('confirm')">{{ confirmText
                            }}</button>
                    </div>
                </div>
            </div>
        </div>
    </teleport>
</template>

<script setup>
defineProps({
    title: {
        type: String,
        default: '모달 제목'
    },
    cancelText: {
        type: String,
        default: '닫기'
    },
    confirmText: {
        type: String,
        default: '확인'
    }
});

defineEmits(['confirm']);
</script>