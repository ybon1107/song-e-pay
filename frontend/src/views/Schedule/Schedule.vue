<script setup lang="ts">
import { ref } from 'vue';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { INITIAL_EVENTS, createEventId } from './event-utils';
import moment from 'moment';

// maintenance 상태를 ref로 정의
const maintenance = ref({
    title: '',
    description: '',
    color: '',
    startedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
    endedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
});

// FullCalendar에 접근할 수 있도록 ref 정의
const calendarRef = ref(null);

// 캘린더 옵션 및 이벤트 상태 정의
const currentEvents = ref([]);
const calendarOptions = ref({
    plugins: [
        dayGridPlugin,
        timeGridPlugin,
        interactionPlugin, // needed for dateClick
    ],
    headerToolbar: {
        left: 'prev,next today',
        center: 'title',
        right: 'dayGridMonth,timeGridWeek,timeGridDay',
    },
    initialView: 'dayGridMonth',
    initialEvents: INITIAL_EVENTS, // alternatively, use the `events` setting to fetch from a feed
    editable: true,
    selectable: true,
    selectMirror: true,
    dayMaxEvents: true,
    weekends: true,
    select: handleDateSelect, // 드래그 선택 이벤트 핸들러
    eventClick: handleEventClick, // 이벤트 클릭 핸들러
    eventsSet: handleEvents, // 이벤트 리스트 설정 핸들러
});

function handleDateSelect(selectInfo: any) {
    // 드래그된 날짜 범위의 시작 날짜와 끝 날짜를 가져옴
    const startDate = selectInfo.startStr; // 드래그한 시작 날짜
    const endDate = selectInfo.endStr ? selectInfo.endStr : startDate; // 드래그한 끝 날짜 (없으면 시작 날짜와 같음)

    const calendarApi = selectInfo.view.calendar;
    showModal();

    maintenance.value.title = ''; // 새 이벤트를 위한 초기화
    maintenance.value.startedAt = startDate;
    maintenance.value.endedAt = endDate;

    // 선택 완료 후 선택 상태를 해제
    calendarApi.unselect();
}

function save() {
    // FullCalendar API 객체를 가져오기
    const calendarApi = calendarRef.value.getApi();

    // 선택한 이벤트를 추가할 때 색상과 기타 정보 반영
    calendarApi.addEvent({
        id: createEventId(),
        title: maintenance.value.title,
        start: maintenance.value.startedAt,
        end: maintenance.value.endedAt,
        backgroundColor: getColor(maintenance.value.color), // 선택한 색상 적용
        borderColor: getColor(maintenance.value.color),
        allDay: true,
    });

    // 모달 닫기
    closeModal();
}

// 선택한 색상에 따른 배경색 설정
function getColor(colorName: string) {
    switch (colorName) {
        case 'primary':
            return '#007bff';
        case 'warning':
            return '#ffc107';
        case 'success':
            return '#28a745';
        case 'danger':
            return '#dc3545';
        case 'muted':
            return '#6c757d';
        default:
            return '#007bff'; // 기본 색상
    }
}

// 주말 토글 핸들러
function handleWeekendsToggle() {
    calendarOptions.value.weekends = !calendarOptions.value.weekends; // 주말 표시 여부 토글
}

// 이벤트 클릭 핸들러
function handleEventClick(clickInfo: any) {
    if (
        confirm(
            `Are you sure you want to delete the event '${clickInfo.event.title}'?`
        )
    ) {
        clickInfo.event.remove();
    }
}

// 이벤트 리스트 설정 핸들러
function handleEvents(events: any) {
    currentEvents.value = events;
}

// 모달 닫기 함수
function closeModal() {
    const modal = document.getElementById('maintenance');
    if (modal) {
        modal.style.display = 'none';
        modal.classList.remove('show');
        modal.setAttribute('aria-hidden', 'true');
    }
}

// 모달 열기 함수
function showModal() {
    const modal = document.getElementById('maintenance');
    if (modal) {
        modal.style.display = 'block';
        modal.classList.add('show'); // 모달이 보여지도록 Bootstrap의 show 클래스 추가
        modal.setAttribute('aria-hidden', 'false');
        modal.style.background = 'rgba(0, 0, 0, 0.5)'; // 배경 색상 추가
    }
}
</script>

<template>
    <div class="demo-app">
        <div class="demo-app-sidebar">
            <div class="demo-app-sidebar-section">
                <h2>Instructions</h2>
                <ul>
                    <li>
                        Select dates and you will be prompted to create a new
                        event
                    </li>
                    <li>Drag, drop, and resize events</li>
                    <li>Click an event to delete it</li>
                </ul>
            </div>
            <div class="demo-app-sidebar-section">
                <label>
                    <input
                        type="checkbox"
                        :checked="calendarOptions.weekends"
                        @change="handleWeekendsToggle"
                    />
                    toggle weekends
                </label>
            </div>
            <div class="demo-app-sidebar-section">
                <h2>All Events ({{ currentEvents.length }})</h2>
                <ul>
                    <li v-for="event in currentEvents" :key="event.id">
                        <b>{{ event.startStr }}</b>
                        <i>{{ event.title }}</i>
                    </li>
                </ul>
            </div>
        </div>
        <div class="demo-app-main">
            <!-- FullCalendar에 ref 추가 -->
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
    </div>

    <!-- Modal for event creation -->
    <div
        id="maintenance"
        class="modal fade"
        tabindex="-1"
        aria-labelledby="maintenanceLabel"
        aria-hidden="true"
    >
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="maintenanceLabel">
                        이벤트 등록
                    </h4>
                    <button type="button" class="close" @click="closeModal">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <label for="title" class="col-3">이벤트명</label>
                        <div class="col-9">
                            <input
                                id="title"
                                v-model="maintenance.title"
                                class="form-control w-100"
                                type="text"
                            />
                        </div>
                    </div>
                    <div class="row">
                        <label for="color" class="col-3">색상선택</label>
                        <div class="col-9 btn-group w-100 my-20">
                            <!-- 색상 선택을 위한 버튼을 추가 -->
                            <button
                                v-for="color in [
                                    'primary',
                                    'warning',
                                    'success',
                                    'danger',
                                    'muted',
                                ]"
                                :key="color"
                                @click="maintenance.color = color"
                                :style="{
                                    backgroundColor: getColor(color),
                                    width: '30px',
                                    height: '30px',
                                    marginRight: '5px',
                                }"
                                class="color-picker-btn"
                            ></button>
                        </div>
                    </div>
                    <div class="row">
                        <label for="description" class="col-3"
                            >이벤트 세부내용</label
                        >
                        <div class="col-9">
                            <textarea
                                id="description"
                                v-model="maintenance.description"
                                class="form-control"
                                rows="3"
                            ></textarea>
                        </div>
                    </div>
                    <div class="row">
                        <span class="col-3">기간</span>
                        <div class="form-group col-9">
                            <div class="input-group w-100">
                                <div class="input-group-prepend">
                                    <span class="input-group-text"
                                        ><i class="far fa-clock"></i
                                    ></span>
                                </div>
                                <input
                                    id="reservationtime"
                                    v-model="maintenance.startedAt"
                                    type="text"
                                    class="form-control float-right"
                                />
                                <input
                                    id="reservationtime"
                                    v-model="maintenance.endedAt"
                                    type="text"
                                    class="form-control float-right"
                                />
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
                        Close
                    </button>
                    <button type="button" class="btn btn-primary" @click="save">
                        Save changes
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style>
.color-picker-btn {
    border: none;
    cursor: pointer;
}

.color-picker-btn:focus {
    outline: none;
}
</style>
