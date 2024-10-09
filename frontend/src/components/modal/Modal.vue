<template>
  <teleport to="#modal-container">
    <div v-if="isVisible" class="modal-backdrop fade show"></div>
    <div
      v-if="isVisible"
      class="modal fade show"
      style="display: block"
      tabindex="-1"
      role="dialog"
      aria-labelledby="exampleModalLabel"
      aria-hidden="true"
      @click.self="close"
    >
      <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
          <div class="modal-header border-0">
            <h2 class="modal-title fs-5" id="exampleModalLabel">{{ $t(title) }}</h2>
            <button
              type="button"
              class="btn-close"
              @click="close"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <slot></slot>
          </div>
          <div v-if="showFooter" class="modal-footer border-0">
            <button type="button" class="btn btn-secondary" @click="close">
              {{ $t(cancelText) }}
            </button>
            <button type="button" class="btn btn-primary" @click="confirm">
              {{ $t(confirmText) }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script setup>
const props = defineProps({
  title: {
    type: String,
    default: "모달 제목",
  },
  cancelText: {
    type: String,
    default: "닫기",
  },
  confirmText: {
    type: String,
    default: "확인",
  },
  isVisible: {
    type: Boolean,
    required: true,
  },
  showFooter: {
    type: Boolean,
    default: true,
  },
});

const emit = defineEmits(["close", "confirm"]);

const close = () => {
  emit("close");
};

const confirm = () => {
  emit("confirm");
};
</script>

<style scoped>
.btn-close {
  opacity: 1;
  visibility: visible;
  color: #000;
  background-color: transparent;
  border: 0;
  font-size: 1.5rem;
}
</style>
