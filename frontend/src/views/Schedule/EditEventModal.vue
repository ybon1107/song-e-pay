<template>
    <div
        v-if="isVisible"
        class="modal fade show"
        tabindex="-1"
        style="display: block"
        :inert="!isVisible"
    >
        <div class="modal-dialog" style="max-width: 500px">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title">이벤트 수정</h4>
                    <button type="button" class="close" @click="closeModal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row no-gutters mb-3">
                        <label for="title" class="col-3">이벤트명</label>
                        <div class="col-14">
                            <input
                                id="title"
                                v-model="maintenanceCopy.title"
                                class="form-control"
                                type="text"
                                placeholder="이벤트명을 입력하세요"
                                style="width: 100%"
                            />
                        </div>
                    </div>
                    <div class="row no-gutters mb-3">
                        <label for="color" class="col-3">색상 선택</label>
                        <div class="col-14 btn-group">
                            <button
                                v-for="color in [
                                    'primary',
                                    'warning',
                                    'success',
                                    'danger',
                                    'muted',
                                ]"
                                :key="color"
                                @click="maintenanceCopy.color = getColor(color)"
                                :style="{ backgroundColor: getColor(color) }"
                                :class="{
                                    active:
                                        maintenanceCopy.color ===
                                        getColor(color),
                                }"
                                class="color-picker-btn"
                            ></button>
                        </div>
                    </div>
                    <div class="row no-gutters mb-3">
                        <label for="description" class="col-3">세부내용</label>
                        <div class="col-14">
                            <textarea
                                id="description"
                                v-model="maintenanceCopy.description"
                                class="form-control"
                                rows="3"
                                placeholder="세부내용을 입력하세요"
                                style="width: 100%"
                            ></textarea>
                        </div>
                    </div>
                    <div class="row no-gutters mb-3">
                        <label class="col-3">기간</label>
                        <div class="col-14">
                            <div class="row no-gutters">
                                <div class="col-6">
                                    <input
                                        id="startDate"
                                        v-model="maintenanceCopy.startedAt"
                                        type="datetime-local"
                                        class="form-control"
                                        style="width: 100%"
                                    />
                                </div>
                                <div class="col-6">
                                    <input
                                        id="endDate"
                                        v-model="maintenanceCopy.endedAt"
                                        type="datetime-local"
                                        class="form-control"
                                        style="width: 100%"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer justify-content-between">
                    <button
                        type="button"
                        class="btn btn-danger"
                        @click="emit('deleteEvent', maintenanceCopy.id)"
                    >
                        삭제
                    </button>
                    <button
                        type="button"
                        class="btn btn-primary"
                        @click="saveEvent"
                    >
                        저장
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, nextTick, defineProps, defineEmits } from 'vue';

const props = defineProps({
    isVisible: Boolean,
    maintenance: {
        type: Object,
        default: () => ({
            id: '',
            title: '',
            description: '',
            color: '',
        }),
    },
});

const emit = defineEmits(['saveEvent', 'closeModal', 'deleteEvent']);
const maintenanceCopy = ref({ ...props.maintenance });

watch(
    () => props.isVisible,
    (newVisible) => {
        if (newVisible) {
            maintenanceCopy.value = { ...props.maintenance };
            nextTick(() => {
                document.getElementById('title')?.focus();
            });
        }
    },
    { immediate: true }
);

const getColor = (colorName) => {
    switch (colorName) {
        case 'primary':
            return '#8EEFEF';
        case 'warning':
            return '#FFD347';
        case 'success':
            return '#4DD36A';
        case 'danger':
            return '#FFA8E1';
        case 'muted':
            return '#A1A5AB';
        default:
            return '#8EEFEF';
    }
};

const closeModal = () => {
    emit('closeModal');
};

const saveEvent = () => {
    emit('saveEvent', maintenanceCopy.value);
    closeModal();
};
</script>

<style scoped>
.modal {
    background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
}

.color-picker-btn {
    border: none;
    cursor: pointer;
    margin-right: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    transition: box-shadow 0.3s ease;
}

.color-picker-btn.active {
    box-shadow: 0 0 0 4px rgba(0, 0, 0, 0.3);
}
</style>
