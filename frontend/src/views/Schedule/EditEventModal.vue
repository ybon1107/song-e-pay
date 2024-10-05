<template>
    <div
        v-if="isVisible"
        class="modal fade show"
        tabindex="-1"
        role="dialog"
        aria-labelledby="editEventModalLabel"
        style="display: block"
    >
        <div class="modal-dialog" style="max-width: 500px">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="editEventModalLabel">
                        이벤트 수정
                    </h4>
                    <button type="button" class="close" @click="closeModal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <!-- 이벤트명 입력 -->
                    <div class="row no-gutters mb-3">
                        <label for="title" class="col-3">이벤트명</label>
                        <div class="col-14">
                            <input
                                id="title"
                                v-model="eventDetailsCopy.title"
                                class="form-control"
                                type="text"
                                placeholder="이벤트명을 입력하세요"
                                style="width: 100%"
                            />
                        </div>
                    </div>

                    <!-- 색상 선택 -->
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
                                @click="eventDetailsCopy.color = color"
                                :style="{ backgroundColor: getColor(color) }"
                                :class="{
                                    active: eventDetailsCopy.color === color,
                                }"
                                class="color-picker-btn"
                            ></button>
                        </div>
                    </div>

                    <!-- 이벤트 세부내용 입력 -->
                    <div class="row no-gutters mb-3">
                        <label for="description" class="col-3">세부내용</label>
                        <div class="col-14">
                            <textarea
                                id="description"
                                v-model="eventDetailsCopy.description"
                                class="form-control"
                                rows="3"
                                placeholder="세부내용을 입력하세요"
                                style="width: 100%"
                            ></textarea>
                        </div>
                    </div>

                    <!-- 기간 선택 -->
                    <div class="row no-gutters mb-3">
                        <label class="col-3">기간</label>
                        <div class="col-14">
                            <div class="row no-gutters">
                                <div class="col-6">
                                    <input
                                        id="startDate"
                                        v-model="eventDetailsCopy.startedAt"
                                        type="date"
                                        class="form-control"
                                        style="width: 100%"
                                    />
                                </div>
                                <div class="col-6">
                                    <input
                                        id="endDate"
                                        v-model="eventDetailsCopy.endedAt"
                                        type="date"
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
                        class="btn btn-default"
                        @click="closeModal"
                    >
                        닫기
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
import { ref, watch, defineProps, defineEmits, nextTick } from 'vue';

const props = defineProps({
    isVisible: Boolean,
    eventDetails: Object,
});

const emit = defineEmits(['saveEvent', 'closeModal']);
const eventDetailsCopy = ref({ ...props.eventDetails });

watch(
    () => props.isVisible,
    (newVisible) => {
        if (newVisible) {
            eventDetailsCopy.value = { ...props.eventDetails };
            nextTick(() => {
                document.getElementById('title')?.focus(); // 모달이 열리면 title 필드에 포커스
            });
        }
    }
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
    emit('saveEvent', eventDetailsCopy.value);
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
