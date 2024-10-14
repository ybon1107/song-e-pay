<template>
  <div class="form-group mb-0">
    <div :class="['input-group', hasIcon(icon)]">
      <span v-if="iconDir === 'left'" class="input-group-text">
        <i :class="getIcon(icon)"></i>
      </span>
      <input :id="id" :type="type" class="form-control" :class="{ 'is-invalid': errorMessage }" :name="name"
        :value="formattedValue" :placeholder="placeholder" :unit="unit" :required="isRequired" @input="onInput" />
      <span v-if="iconDir === 'right'" class="input-group-text">
        <i :class="getIcon(icon)"></i>
      </span>
      <span class="input-group-text">{{ unit }}</span>
      <div class="invalid-feedback text-xs mb-1">
        <!-- errorAmountMessage가 빈 문자열일 때, 공백을 출력 -->
        <span v-if="errorMessage">{{ errorMessage }}</span>
        <span v-else>test</span>
      </div>
    </div>

    <!-- 에러 메시지 표시 -->
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
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
});

const emit = defineEmits(['update:modelValue']);
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

const errorMessage = ref('');

// input 이벤트 핸들러
const onInput = (event) => {
  event.target.value = event.target.value.replace(/[^0-9]/g, '');
  let rawValue = event.target.value

  if (rawValue.startsWith('0')) {
    errorMessage.value = t('myAccount--error-notZero');
    rawValue = ''; // input 필드 값을 빈 문자열로 초기화
    event.target.value = ''; // 실제 입력 필드의 값도 초기화
  } else if (
    (Number(rawValue) > getBalance() && props.activeTab == 2) ||
    (Number(rawValue) > getBalance() && props.activeTab == 4) ||
    (Number(rawValue) > getBalance() && props.activeTab == 5) ||
    (Number(rawValue) > getBalance() && props.activeTab == 6)
  ) {
    // If the input value exceeds the balance, show error and prevent further input
    errorMessage.value = t('myAccount--error-overAmount');
    rawValue = rawValue.slice(0, -1); // Reset the input field value
    event.target.value = event.target.value.slice(0, -1); // Clear the input field
  } else {
    errorMessage.value = ''; // 유효한 값일 때 에러 메시지 지움
  }

  emit('update:modelValue', rawValue); // 부모 컴포넌트로 숫자만 전송
};

const getClasses = (size, success, error) => {
  let sizeValue, isValidValue;

  sizeValue = size ? `form-control-${size}` : null;

  if (error) {
    isValidValue = 'is-invalid';
  } else if (success) {
    isValidValue = 'is-valid';
  } else {
    isValidValue = '';
  }

  return `${sizeValue} ${isValidValue}`;
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
  background-color: #665A4A;
  border: 1px solid #ced4da;
  border-radius: 0 0.375rem 0.375rem 0 !important;
  color: white;
}
</style>
