<script setup>
import { ref, watch } from "vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";

// Props 정의
const props = defineProps({
  modelValue: {
    type: String,
    required: true,
  },
  countryCallingCode: {
    type: String,
    required: true,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
  error: {
    type: Boolean,
    default: false,
  },
});

// Emits 정의
const emit = defineEmits(["update:modelValue", "update:countryCallingCode"]);

// 내부 상태 정의
const internalPhoneNumber = ref(props.modelValue);
const internalCountryCallingCode = ref(props.countryCallingCode);
const internalError = ref(props.error);

// 전화번호 입력 변경 감지
watch(internalPhoneNumber, (newValue) => {
  emit("update:modelValue", newValue);
});

// 국가 코드 변경 감지
watch(internalCountryCallingCode, (newValue) => {
  emit("update:countryCallingCode", newValue);
});

watch(
  () => props.modelValue,
  (newValue) => {
    internalPhoneNumber.value = newValue;
  }
);

watch(
  () => props.countryCallingCode,
  (newValue) => {
    internalCountryCallingCode.value = newValue;
  }
);

watch(
  () => props.error,
  (newValue) => {
    internalError.value = newValue;
  }
);

// 숫자만 입력되도록 설정하는 함수
const handleInput = (event) => {
  event.target.value = event.target.value.replace(/\D/g, "");
  internalPhoneNumber.value = event.target.value;
};

const handleKeyPress = (event) => {
  if (!/[0-9]/.test(event.key)) {
    event.preventDefault();
  }
};
</script>

<template>
  <div class="phone-input-group row g-3">
    <div class="col-xl-4 col-md-4 col-sm-4">
      <select
        id="countryCallingCode"
        class="form-select"
        aria-label="Country"
        v-model="internalCountryCallingCode"
        :disabled="disabled"
      >
        <option value="+1">&#43;1 USA</option>
        <option value="+62">&#43;62 IDN</option>
        <option value="+84">&#43;84 VNM</option>
      </select>
    </div>
    <div class="col-xl col-md col-sm">
      <argon-input
        id="phoneNumber"
        type="tel"
        :placeholder="$t('signUp--phonePlaceholder')"
        aria-label="Phone number"
        v-model="internalPhoneNumber"
        :disabled="disabled"
        :error="internalError"
        :errorText="$t('signUp--errorText-phone')"
        @input="handleInput"
        @keypress="handleKeyPress"
      />
    </div>
  </div>
</template>
