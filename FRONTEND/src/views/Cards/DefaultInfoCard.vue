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
});
</script>
<template>
  <div class="card">
    <div class="position-absolute top-0 start-0 p-3">
      <h6 class="mb-0">{{ title }}</h6>
    </div>
    <div class="p-3 mx-4 text-center card-header d-flex justify-content-center">
      <slot name="icon"></slot>
    </div>
    <div class="p-3 pt-0 text-center card-body">
      <span class="text-xs">{{ description }}</span>
      <h5 class="mb-0">{{ value }}</h5>
      <hr class="my-3 horizontal dark" />
    </div>
  </div>
</template>
