<script setup lang="ts">
import { ref } from 'vue';
import FullCalendar from '@fullcalendar/vue3';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import { createEventId } from './event-utils';
import moment from 'moment';
// maintenance 상태를 ref로 정의
const maintenance = ref({
  title: '',
  description: '',
  color: '',
  startedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
  endedAt: moment().format('YYYY-MM-DD HH:mm:ss'),
});
const calendarRef = ref(null);
// 상태 관리
const currentEvents = ref([]);
// 모달 상태와 일정 제목, 선택된 정보 관리

const selectedInfo = ref(null);
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

// 이벤트 클릭 핸들러
const handleEventClick = (clickInfo) => {
  if (confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {
    clickInfo.event.remove();
  }
};
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

// 이벤트 상태 업데이트 핸들러
const handleEvents = (events) => {
  currentEvents.value = events;
};

// 캘린더 옵션
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
  // initialEvents: INITIAL_EVENTS,
  // alternatively, use the `events` setting to fetch from a feed
  editable: true,
  selectable: true,
  selectMirror: true,
  dayMaxEvents: true,
  weekends: true,
  select: handleDateSelect,
  eventClick: handleEventClick,
  eventsSet: handleEvents,
});
</script>

<template>
  <div class="container-fluid" style="width: 85%" id="responsive-container">
    <div class="card card-body demo-app d-flex flex-column flex-md-row">
      <div class="demo-app-main col-md-9">
        <FullCalendar ref="calendarRef" class="demo-app-calendar" :options="calendarOptions">
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
  <!-- Modal for event creation -->
  <div id="maintenance" class="modal fade" tabindex="-1" aria-labelledby="maintenanceLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h4 class="modal-title" id="maintenanceLabel">이벤트 등록</h4>
          <button type="button" class="close" @click="closeModal">
            <span aria-hidden="true">&times;</span>
          </button>
        </div>
        <div class="modal-body">
          <div class="row">
            <label for="title" class="col-3">이벤트명</label>
            <div class="col-9">
              <input id="title" v-model="maintenance.title" class="form-control w-100" type="text" />
            </div>
          </div>
          <div class="row">
            <label for="color" class="col-3">색상선택</label>
            <div class="col-9 btn-group w-100 my-20">
              <!-- 색상 선택을 위한 버튼을 추가 -->
              <button
                v-for="color in ['primary', 'warning', 'success', 'danger', 'muted']"
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
            <label for="description" class="col-3">이벤트 세부내용</label>
            <div class="col-9">
              <textarea id="description" v-model="maintenance.description" class="form-control" rows="3"></textarea>
            </div>
          </div>
          <div class="row">
            <label class="col-3">기간</label>
            <div class="col-9">
              <div class="row">
                <div class="col-6">
                  <input id="startDate" v-model="maintenance.startedAt" type="date" class="form-control" />
                </div>
                <div class="col-6">
                  <input id="endDate" v-model="maintenance.endedAt" type="date" class="form-control" />
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="modal-footer justify-content-between">
          <button type="button" class="btn btn-default" @click="closeModal">Close</button>
          <button type="button" class="btn btn-primary" @click="save">Save changes</button>
        </div>
      </div>
    </div>
  </div>
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

/* li {
  margin: 1.5em 0;
  padding: 0;
} */

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
  border-right: 2px solid #ffeb69 !important ;
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

/* FullCalendar toolbar 버튼 스타일 커스터마이징 */
.fc-toolbar .fc-button {
  background-color: #ffeb69 !important; /* 버튼의 배경색 */
  color: white; /* 버튼 텍스트 색상 */
  border: 0px !important;
}

.fc-toolbar .fc-button:hover {
  background-color: #ffeb69 !important; /* 마우스 오버 시 버튼 색상 */
  color: black !important;
}

.fc-day-sun a {
  /* 일요일 컬러 */
  color: rgb(253, 150, 150) !important;
}

.fc-day-sat a {
  /* 토요일 컬러 */
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
