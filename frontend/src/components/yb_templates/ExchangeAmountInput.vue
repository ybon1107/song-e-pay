<template>
  <div class="form-group mb-0">
    <div :class="['input-group', hasIcon(icon)]">
      <span v-if="iconDir === 'left'" class="input-group-text">
        <i :class="getIcon(icon)"></i>
      </span>
      <input :id="id" :type="type" class="form-control" :class="{ 'is-invalid': errorAmountMessage }" :name="name"
        :value="formattedValue" :placeholder="placeholder" :unit="unit" :required="isRequired" @input="onInput"
        @focus="$emit('focus', $event)" @blur="$emit('blur', $event)" :disabled="disabled" />
      <span v-if="iconDir === 'right'" class="input-group-text">
        <i :class="getIcon(icon)"></i>
      </span>
      <span class="input-group-text">{{ unit }}</span>
      <div class="invalid-feedback text-xs mb-1">
        <!-- errorAmountMessage가 빈 문자열일 때, 공백을 출력 -->
        <span v-if="errorAmountMessage">{{ errorAmountMessage }}</span>
        <span v-else>test</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { defineProps, defineEmits } from 'vue';
import { useI18n } from 'vue-i18n';
const { t } = useI18n();
// Props와 Emit 정의
const props = defineProps({
  size: {
    type: String,
    default: 'default',
  },
  success: {
    type: Boolean,
    default: false,
  },
  error: {
    type: Boolean,
    default: false,
  },
  icon: {
    type: String,
    default: '',
  },
  iconDir: {
    type: String,
    default: '',
  },
  name: {
    type: String,
    default: '',
  },
  id: {
    type: String,
    default: '',
  },
  modelValue: {
    type: String,
    default: '',
  },
  placeholder: {
    type: String,
    default: '',
  },
  unit: {
    type: String,
    default: '',
  },
  type: {
    type: String,
    default: 'text',
  },
  isRequired: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  selectedAsset: {
    type: String,
    default: 'Won-E Money', // Won-E Money or Song-E Pay
  },
  wonEMoneyBalance: {
    type: Number,
    default: 0,
  },
  songEMoneyBalance: {
    type: Number,
    default: 0,
  },
  activeTab: {
    type: Number,
    default: 0,
  },
  errorAmountMessage: {
    // props로 errorAmountMessage를 정의
    type: String,
    default: '',
  },
});
const isVIfVisible = computed(() => {
  return props.errorAmountMessage !== '';
});
const emit = defineEmits(['update:modelValue', 'focus', 'blur', 'update:errorAmountMessage']);
// Helper function to get the current balance based on the selected asset
const getBalance = () => {
  return props.selectedAsset === 'won-e' ? props.wonEMoneyBalance : props.songEMoneyBalance;
};
const formatNumber = (num) => {
  return num.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const formattedValue = computed({
  get() {
    let val = props.modelValue.replace(/[^\d.]/g, ''); // 숫자만 추출
    return val ? formatNumber(val) : ''; // 천 단위 콤마와 "원" 추가
  },
  set(newValue) {
    const rawValue = newValue.replace(/[^\d.]/g, ''); // 숫자만 추출
    emit('update:modelValue', rawValue); // 부모 컴포넌트로 숫자만 전송
  },
});
// input 이벤트 핸들러
const onInput = (event) => {
  event.target.value = event.target.value.replace(/[^0-9]/g, '');
  let rawValue = event.target.value
  if (rawValue.startsWith('0')) {
    emit('update:errorAmountMessage', t('myAccount--error-notZero')); // Emit error message
    rawValue = ''; // input 필드 값을 빈 문자열로 초기화
    event.target.value = ''; // 실제 입력 필드의 값도 초기화
  } else if (
    (Number(rawValue) > getBalance() && props.activeTab == 2) ||
    (Number(rawValue) > getBalance() && props.activeTab == 4) ||
    (Number(rawValue) > getBalance() && props.activeTab == 5) ||
    (Number(rawValue) > getBalance() && props.activeTab == 6)
  ) {
    // If the input value exceeds the balance, show error and prevent further input
    emit('update:errorAmountMessage', t('myAccount--error-overAmount')); // Emit error message
    rawValue = rawValue.slice(0, -1); // Reset the input field value
    event.target.value = event.target.value.slice(0, -1); // Clear the input field
  } else {
    emit('update:errorAmountMessage', ''); // Emit empty error message if valid
  }

  emit('update:modelValue', rawValue); // 부모 컴포넌트로 숫자만 전송
};

const getIcon = (icon) => (icon ? icon : null);
const hasIcon = (icon) => (icon ? 'input-group' : null);
</script>

<style scoped>
input {
  padding: 8px;
  font-size: 16px;
  width: calc(100% - 50px);
  /* "원"을 포함한 레이아웃 고려 */
  box-sizing: border-box;
}

.input-group-text {
  padding: 0.375rem 0.75rem;
  background-color: #e9ecef;
  border: 1px solid #ced4da;
  border-radius: 0.375rem;
}
</style>
