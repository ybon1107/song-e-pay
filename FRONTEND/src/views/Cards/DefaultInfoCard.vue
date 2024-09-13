<script setup>
defineProps({
  icon: {
    type: Object,
    required: false, // 필수에서 선택으로 변경
    default: () => ({
      background: 'bg-gradient-success', // 기본값 추가
    }),
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: '',
  },
  value: {
    type: [String, Number],
    default: '',
  },
  imgSrc: {
    // 이미지 소스를 받는 prop 추가
    type: String,
    required: true,
  },
  img: {
    type: String,
    required: false,
  },
});
</script>

<template>
  <div class="card position-relative" :style="{ backgroundImage: `url(${imgSrc})`, backgroundSize: 'cover', backgroundPosition: 'center' }">
    <!-- 카드 제목 -->
    <div class="position-relative p-3 text-white">
      <h6 class="mb-0">{{ title }}</h6>
    </div>

    <!-- 카드 안에 표시할 이미지 -->
    <div v-if="img" class="position-relative p-3 img-container">
      <img :src="img" alt="Card Image" class="img-fluid rounded" />
    </div>

    <!-- 아이콘과 내용 -->
    <div class="p-3 d-flex justify-content-start align-items-center" style="margin-top: 20px">
      <slot name="icon"></slot>
    </div>

    <!-- 하단 우측 내용 -->
    <div class="position-absolute bottom-0 end-0 p-3 text-center text-white" style="margin-bottom: -20px; margin-right: 10px">
      <hr class="my-1 horizontal dark" />
      <h6 class="text-s">{{ value }}</h6>
    </div>
  </div>
</template>

<style scoped>
.img-container {
  position: absolute;
  top: -25px; /* 제목 아래에 위치하도록 조절 */
  left: 0px; /* 카드의 왼쪽 여백 조절 */
  width: 10vw; /* 뷰포트 너비의 20%로 조절 */
  max-width: 70px; /* 최대 너비 설정 */
  height: auto; /* 높이는 비율에 맞춰 조절 */
}

.img-container img {
  width: 100%; /* 컨테이너에 맞게 이미지 크기 조절 */
  height: auto; /* 비율 유지 */
}

/* 미디어 쿼리: 화면이 더 작을 때 이미지 크기 조절 */
@media (max-width: 768px) {
  .img-container {
    width: 30vw; /* 화면이 작은 경우 이미지 크기를 더 크게 설정 */
  }
}

@media (max-width: 576px) {
  .img-container {
    width: 40vw; /* 모바일 화면에서는 이미지가 더 크도록 조정 */
  }
}
</style>
