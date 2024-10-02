<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  icon: {
    type: Object,
    required: false,
    default: () => ({
      background: "bg-gradient-success", // 기본값 추가
    }),
  },
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: "",
  },
  value: {
    type: [String, Number],
    default: "",
  },
  imgSrc: {
    type: String,
    required: true,
  },
  img: {
    type: String,
    required: false,
  },
  isSelected: {
    type: Boolean,
    default: false, // 기본값 설정
  },
});

const emit = defineEmits(["click"]);

function handleClick() {
  emit("click");
}
</script>

<template>
  <div
    class="card position-relative"
    :class="{ selected: isSelected }"
    :style="{
      backgroundImage: `url(${imgSrc})`,
      backgroundSize: 'cover',
      backgroundPosition: 'center',
    }"
    @click="handleClick"
  >
    <!-- 카드 제목 -->
    <div class="position-relative p-3 text-white">
      <h6 class="mb-0">{{ title }}</h6>
    </div>

    <!-- 카드 안에 표시할 이미지
    <div v-if="img" class="position-relative p-3 img-container">
      <img :src="img" alt="Card Image" class="img-fluid rounded" />
    </div> -->

    <!-- 아이콘과 내용
    <div class="w-60 mt-2" style="margin-top: 20px">
      <slot name="icon"></slot>
    </div> -->

    <!-- 하단 우측 내용 -->
    <div
      class="position-absolute bottom-0 end-0 p-3 text-center text-white"
      style="margin-bottom: -15px; margin-right: 10px"
    >
      <hr class="my-1 horizontal dark" />
      <h6 class="text-s">{{ value }}</h6>
    </div>
  </div>
</template>

<style scoped>
.card {
  width: 30vw; /* 뷰포트 너비의 30%로 조절 */
  max-width: 450px; /* 최대 너비 설정 */
  aspect-ratio: 3 / 2; /* 비율 유지 */
  position: relative;
  border-radius: 20px; /* 카드의 모서리를 둥글게 설정 */
  overflow: hidden; /* 카드 내용이 카드 영역을 넘지 않도록 설정 */
  cursor: pointer; /* 카드 클릭 가능하도록 설정 */
  margin-left: 30px;
  margin-right: 30px;
}

.card.selected {
  border: 3px solid #ffd700;
  /* 선택된 카드에 녹색 테두리 추가 */
  box-shadow: 0 0 15px ffd700;
  /* 선택된 카드에 그림자 효과 추가 */
}

.img-container {
  position: absolute;
  top: 10px; /* 제목 아래에 위치하도록 조절 */
  left: 10px; /* 카드의 왼쪽 여백 조절 */
  width: 10vw; /* 뷰포트 너비의 10%로 조절 */
  max-width: 90px; /* 최대 너비 설정 */
  height: auto; /* 높이는 비율에 맞춰 조절 */
}

.img-container img {
  width: 100%; /* 컨테이너에 맞게 이미지 크기 조절 */
  height: auto; /* 비율 유지 */
}

/* 텍스트 스타일 */
h6 {
  font-size: 2.5vw; /* 뷰포트 너비에 따라 크기 조절 */
  margin: 0;
}

/* 하단 우측 내용 글자 크기 조정 */
.text-s {
  font-size: 2vw; /* 뷰포트 너비에 따라 크기 조절 */
}
/* 미디어 쿼리: 텍스트 크기 조정 */
@media (max-width: 768px) {
  h6 {
    font-size: 1rem; /* 작은 화면에서 글자 크기 감소 */
  }
  .text-s {
    font-size: 0.5rem; /* 작은 화면에서 글자 크기 감소 */
  }
}

@media (max-width: 576px) {
  h6 {
    font-size: 0.5rem; /* 모바일 화면에서 글자 크기 감소 */
  }
  .text-s {
    font-size: 0.25rem; /* 모바일 화면에서 글자 크기 감소 */
  }
}
</style>
