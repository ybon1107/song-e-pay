<script setup>
import { ref } from "vue";
import ArgonInput from "@/components/templates/ArgonInput.vue";

const phoneNumber = ref("");
const countryCallingCode = ref("+1");

// Props 정의
const props = defineProps({
  phoneNumber: {
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
});
</script>

<template>
  <div class="phone-input-group row g-3">
    <div class="col-xl-4 col-md-3 col-sm-3">
      <select
        id="countryCallingCode"
        class="form-select"
        aria-label="Country"
        v-model="countryCallingCode"
        :disabled="disabled"
      >
        <option value="+1">&#43;1 USA</option>
        <option value="+62">&#43;62 IDN</option>
        <option value="+81">&#43;84 VNM</option>
      </select>
    </div>
    <div class="col-xl col-md col-sm">
      <argon-input
        id="phoneNumber"
        type="tel"
        placeholder="Phone number"
        aria-label="Phone number"
        v-model="phoneNumber"
        :disabled="disabled"
      />
    </div>
  </div>
</template>
