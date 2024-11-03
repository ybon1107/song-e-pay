<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { createEventId } from './event-utils';
import moment from 'moment';
import axios from 'axios';
import EditEventModal from './EditEventModal.vue';
import MaintenanceModal from './MaintenanceModal.vue';
import scheduleApi from '@/api/scheduleApi';
import { useAuthStore } from '@/stores/auth';

const isMaintenanceModalVisible = ref(false);
const isEditEventModalVisible = ref(false);

const auth = useAuthStore();
const userId = computed(() => auth.userId);

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
async function addEvent(eventData) {
    const colorWithoutHash = eventData.color.replace('#', '');

    const scheduleData = {
        userId: userId.value,
        beginDate: eventData.startedAt,
        endDate: moment(eventData.endedAt).add(1, 'days').format('YYYY-MM-DD'),
        title: eventData.title,
        todo: eventData.description,
        color: colorWithoutHash || '000000',
    };

    try {
        const response = await scheduleApi.addEvent(scheduleData);
        if (response) {
            // 캘린더 API를 통해 현재 날짜 가져오기
            const calendarApi = calendarRef.value.getApi();

            // 새로 추가된 이벤트를 캘린더에 직접 추가
            calendarApi.addEvent({
                id: response.eventNo, // 서버에서 반환된 이벤트 ID
                title: eventData.title,
                start: eventData.startedAt,
                end: moment(eventData.endedAt)
                    .add(1, 'days')
                    .format('YYYY-MM-DD'),
                backgroundColor: `#${colorWithoutHash}`,
                borderColor: `#${colorWithoutHash}`,
                allDay: true,
                extendedProps: {
                    description: eventData.description,
                },
            });
        }
        closeMaintenanceModal();
    } catch (error) {
        console.error('일정 추가 중 오류 발생:', error);
    }
}

function handleEventClick(clickInfo) {
    const event = clickInfo.event;

    maintenance.value = {
        id: event.id || '', // id가 null 또는 undefined일 경우 빈 문자열을 사용
        title: event.title || '',
        description: event.extendedProps.description || '',
        color: event.backgroundColor || '000000',
        startedAt: event.start
            ? moment(event.start).format('YYYY-MM-DD') // 날짜만 표시
            : '',
        endedAt: event.end ? moment(event.end).format('YYYY-MM-DD') : '', // 날짜만 표시
    };

    console.log(
        'Clicked Event Details:',
        JSON.stringify(maintenance.value, null, 2)
    );
    // 데이터 확인
    isEditEventModalVisible.value = true; // 모달 표시
}

// 기존 이벤트 업데이트
async function updateEvent(updatedEvent) {
    const colorWithoutHash = updatedEvent.color.replace('#', ''); // 색깔 Hex코드 저장
    console.log('Updated Event Data:', updatedEvent); // updatedEvent 객체 확인

    const scheduleData = {
        id: maintenance.value.id,
        title: updatedEvent.title,
        todo: updatedEvent.description || '',
        beginDate: updatedEvent.startedAt || moment().format('YYYY-MM-DD'),
        endDate: updatedEvent.endedAt || moment().format('YYYY-MM-DD'),
        color: colorWithoutHash || '000000',
    };

    try {
        const response = await scheduleApi.updateEvent(
            maintenance.value.id,
            scheduleData
        );
        const calendarApi = calendarRef.value?.getApi();
        if (calendarApi) {
            const event = calendarApi.getEventById(maintenance.value.id);
            if (event) {
                event.setProp('title', updatedEvent.title);
                event.setStart(updatedEvent.startedAt);
                event.setEnd(updatedEvent.endedAt);
                event.setProp('backgroundColor', `#${colorWithoutHash}`);
                event.setProp('borderColor', `#${colorWithoutHash}`);
                event.setExtendedProp('description', updatedEvent.description);
            }
        }
        closeEditEventModal();
    } catch (error) {
        console.error('이벤트 업데이트 중 오류 발생:', error);
    }
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
async function loadEvents(date) {
    try {
        const year = date.getFullYear();
        const month = date.getMonth() + 1; // getMonth()는 0부터 시작하므로 1을 더합니다.
        const events = await scheduleApi.getEvents(userId.value, year, month);

        const calendarApi = calendarRef.value.getApi();

        // 기존 모든 이벤트 제거
        calendarApi.removeAllEvents();

        events.forEach((event) => {
            calendarApi.addEvent({
                id: event.eventNo,
                title: event.title,
                start: event.beginDate,
                end: event.endDate,
                backgroundColor: `#${event.color}`,
                borderColor: `#${event.color}`,
                allDay: true,
                extendedProps: {
                    description: event.todo,
                },
            });
        });
    } catch (error) {
        console.error('이벤트 로딩 중 오류 발생:', error);
    }
}

onMounted(() => {
    const calendarApi = calendarRef.value.getApi();
    loadEvents(calendarApi.getDate());
});

// 삭제
async function deleteEvent(eventId) {
    try {
        const response = await scheduleApi.deleteEvent(eventId);
        console.log('Event deleted successfully:', response);
        // 캘린더에서 삭제된 이벤트 제거
        const calendarApi = calendarRef.value.getApi();
        const event = calendarApi.getEventById(eventId);
        if (event) {
            event.remove();
        }
        closeEditEventModal();
    } catch (error) {
        console.error('Error deleting event:', error);
    }
}

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
    datesSet: (dateInfo) => {
        // 달력의 날짜 범위가 변경될 때마다 이벤트를 다시 로드합니다.
        loadEvents(dateInfo.view.currentStart);
    },
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
                <h5>
                    {{ $t('schedule--text--thisMonth') }} ({{
                        currentEvents.length
                    }})
                </h5>
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
        @deleteEvent="deleteEvent"
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
