<template>
    <div
        v-if="isVisible"
        class="modal fade show"
        tabindex="-1"
        :aria-hidden="!isVisible"
        style="display: block"
    >
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="eventListLabel">이벤트 목록</h4>
                    <button
                        type="button"
                        class="close"
                        @click="handleCloseModal"
                    >
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <ul class="event-list">
                        <li
                            v-for="event in displayedEvents"
                            :key="event.id"
                            class="event-item"
                        >
                            <div class="event-checkbox">
                                <input
                                    type="checkbox"
                                    :value="event.id"
                                    v-model="selectedEvents"
                                    class="form-check-input"
                                />
                            </div>
                            <div class="event-details">
                                <div class="event-title">
                                    <b>{{ event.title }}</b>
                                </div>
                                <div class="event-dates">
                                    {{ event.description }} ~
                                    {{
                                        event.endStr
                                            ? event.endStr
                                            : event.startStr
                                    }}
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="modal-footer justify-content-between">
                    <button
                        type="button"
                        class="btn btn-default"
                        @click="handleCloseModal"
                    >
                        Close
                    </button>
                    <button
                        type="button"
                        class="btn btn-danger"
                        @click="deleteSelectedEvents"
                    >
                        선택한 이벤트 삭제
                    </button>
                    <button
                        type="button"
                        class="btn btn-primary"
                        @click="openAddEvent"
                    >
                        이벤트 추가
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, watch } from 'vue';

// props로 받은 값을 정의합니다.
const props = defineProps({
    isVisible: Boolean,
    events: Array,
});

const emit = defineEmits([
    'closeModal',
    'openMaintenanceModal',
    'deleteEvents',
]);

const selectedEvents = ref([]);
const displayedEvents = ref([...props.events]);

// props.events가 변경되면 이를 반영합니다.
watch(
    () => props.events,
    (newEvents) => {
        displayedEvents.value = [...newEvents];
    },
    { immediate: true } // 컴포넌트가 마운트되었을 때 바로 적용
);

// 모달 닫기
const handleCloseModal = () => {
    emit('closeModal');
};

// 이벤트 추가 버튼 클릭 시 MaintenanceModal을 여는 함수
const openAddEvent = () => {
    emit('openMaintenanceModal');
};

// 선택한 이벤트 삭제
const deleteSelectedEvents = () => {
    selectedEvents.value.forEach((id) => {
        const index = displayedEvents.value.findIndex(
            (event) => event.id === id
        );
        if (index !== -1) {
            displayedEvents.value.splice(index, 1);
        }
    });
    emit('deleteEvents', selectedEvents.value);
    selectedEvents.value = []; // 선택된 이벤트 초기화
};
</script>

<style scoped>
.modal {
    background-color: rgba(0, 0, 0, 0.5);
}

.event-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.event-item {
    display: flex;
    align-items: center;
    padding: 10px;
    margin-bottom: 10px;
    border: 1px solid #e0e0e0;
    border-radius: 8px;
    background-color: #f9f9f9;
    transition: background-color 0.2s ease;
}

.event-item:hover {
    background-color: #f1f1f1;
}

.event-checkbox {
    margin-right: 10px;
}

.event-details {
    flex-grow: 1;
}

.event-title {
    font-weight: bold;
    font-size: 16px;
    color: #333;
}

.event-dates {
    font-size: 14px;
    color: #666;
}
</style>
