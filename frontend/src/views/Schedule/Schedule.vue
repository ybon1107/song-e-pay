<script setup lang="ts">
import { ref } from 'vue';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { createEventId } from './event-utils';
import EditEventModal from './EditEventModal';
import MaintenanceModal from './MaintenanceModal'; // 모달 컴포넌트로 분리
import moment from 'moment';

const clickedDate = ref('');
const maintenance = ref({
    title: '',
    description: '',
    color: '',
    startedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
    endedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
});
const calendarRef = ref(null);
const currentEvents = ref([]);
const isEditEventModalVisible = ref(false);
const isMaintenanceModalVisible = ref(false); // MaintenanceModal의 가시성 관리

// 이벤트 삭제 함수
const deleteEvent = (selectedEventIds) => {
    // 삭제 확인을 사용자에게 묻기
    const confirmation = window.confirm('정말로 삭제하시겠습니까?');

    if (!confirmation) {
        // 사용자가 취소를 누르면 삭제하지 않음
        return;
    }

    // selectedEventIds가 배열인지 확인하고 배열이 아니면 배열로 변환
    if (!Array.isArray(selectedEventIds)) {
        selectedEventIds = [selectedEventIds]; // 배열로 변환
    }

    const calendarApi = calendarRef.value.getApi();

    selectedEventIds.forEach((eventId) => {
        const event = calendarApi.getEventById(eventId);
        if (event) {
            event.remove(); // 이벤트 삭제
            closeModal();
        }
    });

    // currentEvents 상태 업데이트
    currentEvents.value = calendarApi.getEvents();
};

const handleDateSelect = (selectInfo: any) => {
    const startDate = moment(selectInfo.startStr).format('YYYY-MM-DD');
    const endDate = selectInfo.endStr
        ? moment(selectInfo.endStr).format('YYYY-MM-DD')
        : startDate;
    const calendarApi = selectInfo.view.calendar;

    maintenance.value.title = '';
    maintenance.value.startedAt = startDate;
    maintenance.value.endedAt = endDate;
    isMaintenanceModalVisible.value = true; // 일정 추가 모달 열기

    calendarApi.unselect(); // 선택 상태 해제
};

const handleDateClick = (info: any) => {
    clickedDate.value = moment(info.dateStr).format('YYYY-MM-DD');

    maintenance.value.startedAt = clickedDate.value;
    maintenance.value.endedAt = clickedDate.value;
    isMaintenanceModalVisible.value = true; // MaintenanceModal 열기
};

const handleEventClick = (clickInfo) => {
    maintenance.value.id = clickInfo.event.id;
    maintenance.value.title = clickInfo.event.title;
    maintenance.value.description =
        clickInfo.event.extendedProps.description || '';
    maintenance.value.start = moment(clickInfo.event.start).format(
        'YYYY-MM-DD'
    );
    maintenance.value.end = moment(
        clickInfo.event.end || clickInfo.event.start
    ).format('YYYY-MM-DD');

    isEditEventModalVisible.value = true; // EditEventModal 열기
};

const closeModal = () => {
    isEditEventModalVisible.value = false;
    isMaintenanceModalVisible.value = false;
};
const saveEvent = (updatedEvent) => {
    const calendarApi = calendarRef.value.getApi();

    const existingEvent = calendarApi.getEventById(updatedEvent.id);

    if (existingEvent) {
        // 기존 이벤트 업데이트
        existingEvent.setProp('title', updatedEvent.title);
        existingEvent.setExtendedProp('description', updatedEvent.description);
        existingEvent.setStart(updatedEvent.startedAt);
        existingEvent.setEnd(updatedEvent.endedAt);
        existingEvent.setProp('backgroundColor', getColor(updatedEvent.color));
        existingEvent.setProp('borderColor', getColor(updatedEvent.color));
    } else {
        // 새 이벤트 추가
        calendarApi.addEvent({
            id: updatedEvent.id || createEventId(), // id가 없을 경우 새로 생성
            title: updatedEvent.title,
            start: updatedEvent.startedAt,
            end: updatedEvent.endedAt,
            backgroundColor: getColor(updatedEvent.color),
            borderColor: getColor(updatedEvent.color),
            allDay: true,
            extendedProps: {
                description: updatedEvent.description,
            },
        });
    }

    // currentEvents 상태를 갱신하여 UI 업데이트
    currentEvents.value = calendarApi.getEvents();

    // 모달 닫기
    closeEditEventModal();
};

const getColor = (colorName: string) => {
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
    dateClick: handleDateClick,
    eventClick: handleEventClick,
});

const closeEditEventModal = () => {
    isEditEventModalVisible.value = false;
    maintenance.value.id = ''; // 이벤트 ID를 초기화하여 수정 상태 종료
};
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
                <b style="color: black;">{{ arg.timeText }}</b>
                <span style="color: black;">{{ arg.event.title }}</span>
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

    <!-- 분리된 모달들 -->
    <EditEventModal
        v-if="isEditEventModalVisible"
        :isVisible="isEditEventModalVisible"
        :eventDetails="maintenance"
        @saveEvent="saveEvent"
        @closeModal="closeEditEventModal"
        @deleteEvent="deleteEvent"
    />

    <MaintenanceModal
        v-if="isMaintenanceModalVisible"
        :isVisible="isMaintenanceModalVisible"
        :maintenance="maintenance"
        @saveEvent="saveEvent"
        @closeModal="closeModal"
    />
</template>

<style lang="css">
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
    background: #ffffff !important;
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

.fc-day-sun a {
    color: rgb(253, 150, 150) !important;
}

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
    margin-right: 10px;
    width: 40px;
    height: 40px;
    border-radius: 50%;
    transition: box-shadow 0.3s ease;
}

.color-picker-btn:focus {
    outline: none;
}

.color-picker-btn.active {
    box-shadow: 0 0 0 4px rgba(0, 0, 0, 0.3);
}
</style>
