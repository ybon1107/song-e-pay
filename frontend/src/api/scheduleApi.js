import axios from "axios";
const api = axios.create({
  baseURL: "/api/schedule", // 기본 경로 설정
  headers: { "Content-Type": "application/json" }, // 기본 헤더
});
export default {

  async getEvents(userId, year, month) {
    try {
      const response = await api.get(`/list/${userId}`, {
        params: { year, month }
      });
      return response.data;
    } catch (error) {
      console.error('이벤트 로딩 중 오류 발생:', error);
      throw error;
    }
  },

  async save(submitData, calendarRef, closeModal, getColor, userId) {
    try {
      // FullCalendar API 객체를 가져오기
      const calendarApi = calendarRef.value.getApi();
      // submitData를 사용하여 백엔드로 전송할 데이터를 준비
      const scheduleData = {
        userId: userId, // 로그인된 사용자의 ID
        title: submitData.title,
        description: submitData.description,
        color: submitData.color,
        start: submitData.start,
        end: submitData.end,
        allDay: submitData.allDay, // 하루 종일 이벤트 여부
      };
      console.log("전송할 데이터:", scheduleData); // 전송할 데이터 로그 확인
      // 백엔드에 일정 저장 API 호출
      const response = await api.post("/add", scheduleData);
      console.log("백엔드 응답:", response.data); // 백엔드 응답 확인
      // 일정이 성공적으로 저장된 경우 FullCalendar에 이벤트 추가
      if (response.status === 200) {
        calendarApi.addEvent({
          id: response.data.id, // 백엔드에서 반환된 ID
          title: submitData.title, // 이벤트 제목
          start: submitData.start, // 시작 시간
          end: submitData.end, // 종료 시간
          backgroundColor: getColor(submitData.color), // 배경 색상
          borderColor: getColor(submitData.color), // 테두리 색상
          allDay: submitData.allDay, // 하루 종일 이벤트 여부
        });
        // 모달 닫기
        closeModal();
      } else {
        console.error("일정 저장 실패:", response.status);
      }
    } catch (error) {
      console.error("일정 저장 중 오류 발생:", error);
    }
  },
};
