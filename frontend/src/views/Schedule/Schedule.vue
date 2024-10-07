<script setup lang="ts">
import { ref, onMounted } from 'vue';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { createEventId } from './event-utils';
import moment from 'moment';
import axios from 'axios';
import EditEventModal from './EditEventModal.vue';
import MaintenanceModal from './MaintenanceModal.vue';

const isMaintenanceModalVisible = ref(false);
const isEditEventModalVisible = ref(false);

const maintenance = ref({
    id: '',
    title: '',
    description: '',
    color: '',
    startedAt: moment().format('YYYY-MM-DD'),
    endedAt: moment().format('YYYY-MM-DD'),
});

const calendarRef = ref(null);
const currentEvents = ref([]);

// Maintenance modal 열기
function openMaintenanceModal(startDate, endDate) {
    maintenance.value = {
        id: '',
        title: '',
        description: '',
        color: '',
        startedAt: startDate,
        endedAt: endDate,
    };
    isMaintenanceModalVisible.value = true;
}

// 새 이벤트 추가
function addEvent(eventData) {
    const scheduleData = {
        userNo: 1,
        beginDate: eventData.startedAt,
        endDate: eventData.endedAt,
        title: eventData.title,
        todo: eventData.description,
        color: eventData.color || '000000',
    };

    axios
        .post('/api/schedule/add', scheduleData)
        .then((response) => {
            calendarRef.value.getApi().addEvent({
                title: scheduleData.title,
                start: scheduleData.beginDate,
                end: scheduleData.endDate,
                backgroundColor: scheduleData.color,
                borderColor: scheduleData.color,
                allDay: true,
                extendedProps: {
                    description: scheduleData.todo,
                },
            });
            closeMaintenanceModal();
        })
        .catch((error) => {
            console.error('Error adding event:', error);
        });
}

function handleEventClick(clickInfo) {
    const event = clickInfo.event;

    maintenance.value = {
        id: event.id || '', // id가 null 또는 undefined일 경우 빈 문자열을 사용
        title: event.title || '',
        description: event.extendedProps.description || '',
        color: event.backgroundColor || '000000',
        startedAt: event.start
            ? moment(event.start).format('YYYY-MM-DD HH:mm:ss')
            : '',
        endedAt: event.end
            ? moment(event.end).format('YYYY-MM-DD HH:mm:ss')
            : '',
    };

    console.log(
        'Clicked Event Details:',
        JSON.stringify(maintenance.value, null, 2)
    );
    // 데이터 확인
    isEditEventModalVisible.value = true; // 모달 표시
}

// 기존 이벤트 업데이트
function updateEvent(updatedEvent) {
    console.log('Updated Event Data:', updatedEvent); // updatedEvent 객체 확인

    const scheduleData = {
        id: event.eventNo,
        title: updatedEvent.title,
        todo: updatedEvent.description || '',
        beginDate: updatedEvent.startedAt || moment().format('YYYY-MM-DD'),
        endDate: updatedEvent.endedAt || moment().format('YYYY-MM-DD'),
        color: updatedEvent.color,
    };

    axios
        .post(`/api/schedule/update/${updatedEvent.id}`, scheduleData)
        .then((response) => {
            console.log('Event updated successfully:', response.data);
        })
        .catch((error) => {
            console.error('Error updating event:', error);
        });
}
// 모달 닫기 함수
function closeMaintenanceModal() {
    isMaintenanceModalVisible.value = false;
}
function closeEditEventModal() {
    isEditEventModalVisible.value = false;
}

// Date selection handler
function handleDateSelect(selectInfo) {
    const startDate = selectInfo.startStr;
    const endDate = selectInfo.endStr || startDate;
    openMaintenanceModal(startDate, endDate);
}

// Load events from the backend
function loadEvents() {
    axios
        .get('/api/schedule/list')
        .then((response) => {
            const events = response.data;
            events.forEach((event) => {
                calendarRef.value.getApi().addEvent({
                    id: event.eventNo,
                    title: event.title,
                    start: event.beginDate,
                    end: event.endDate,
                    backgroundColor: event.color,
                    borderColor: event.color,
                    allDay: true,
                    description: event.todo,
                });
            });
        })
        .catch((error) => {
            console.error('Error loading events:', error);
        });
}

onMounted(() => {
    loadEvents();
});

const calendarOptions = ref({
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin],
    headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    initialView: 'dayGridMonth',
    editable: true,
    selectable: true,
    dayMaxEvents: true,
    weekends: true,
    select: handleDateSelect,
    eventClick: handleEventClick,
    eventsSet: (events) => (currentEvents.value = events),
});
</script>

<template>
    <div class="container-fluid" style="width: 85%" id="responsive-container">
        <div class="card card-body demo-app d-flex flex-column flex-md-row">
            <div class="demo-app-main col-md-9">
                <FullCalendar
                    ref="calendarRef"
                    class="demo-app-calendar"
                    :options="calendarOptions"
                >
                    <template v-slot:eventContent="arg">
                        <b>{{ arg.timeText }}</b>
                        <i>{{ arg.event.title }}</i>
                    </template>
                </FullCalendar>
            </div>
            <div class="demo-app-sidebar demo-app-sidebar-section col-md-3">
                <h5>All Events ({{ currentEvents.length }})</h5>
                <ul>
                    <li v-for="event in currentEvents" :key="event.id">
                        <b>{{ event.startStr }}</b>
                        <i>{{ event.title }}</i>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <MaintenanceModal
        v-if="isMaintenanceModalVisible"
        :isVisible="isMaintenanceModalVisible"
        :maintenance="maintenance"
        @closeModal="closeMaintenanceModal"
        @saveEvent="addEvent"
    />
    <EditEventModal
        v-if="isEditEventModalVisible"
        :isVisible="isEditEventModalVisible"
        :maintenance="maintenance"
        @closeModal="closeEditEventModal"
        @saveEvent="updateEvent"
    />
</template>

<style lang="css">
.modal {
    background-color: rgba(0, 0, 0, 0.5);
}
.modal-footer {
    display: flex;
    justify-content: space-between;
}
h2 {
    margin: 0;
    font-size: 16px;
}
ul {
    margin: 0;
    padding-left: 1rem !important;
    padding: 0 0 0 1.5em;
}
b {
    margin-right: 3px;
}
.demo-app {
    display: flex;
    min-height: 100%;
    font-family:
        Arial,
        Helvetica Neue,
        Helvetica,
        sans-serif;
    font-size: 14px;
    background: #fff6ef !important;
}
.demo-app-sidebar {
    line-height: 1.5;
    background: #fdf4bb !important;
    border-right: 2px solid #ffeb69 !important;
    border-radius: 1rem;
}
.demo-app-sidebar-section {
    padding: 1em;
    margin-top: 1rem;
}
.demo-app-main {
    flex-grow: 1;
    padding: 1em;
}
.fc {
    max-width: 1100px;
    margin: 0 auto;
}
.fc-toolbar .fc-button {
    background-color: #ffeb69 !important;
    color: white;
    border: 0px !important;
}
.fc-toolbar .fc-button:hover {
    background-color: #ffeb69 !important;
    color: black !important;
}
.fc-day-sun a,
.fc-day-sat a {
    color: rgb(253, 150, 150) !important;
}
.fc .fc-toolbar.fc-header-toolbar {
    margin-bottom: 1.5em !important;
    background: #fdf4bb !important;
    padding: 1rem !important;
    border-radius: 1rem !important;
}
.color-picker-btn {
    border: none;
    cursor: pointer;
}
.color-picker-btn:focus {
    outline: none;
}
</style>
