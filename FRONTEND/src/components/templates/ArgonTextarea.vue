<script setup>
import { readonly } from "vue";
defineProps({
  id: {
    type: String,
    required: true,
  },
  placeholder: {
    type: String,
    default: "",
  },
  value: {
    type: String,
    default: "",
  },
  rows: {
    type: String,
    default: "3",
  },
  readonly: {
    type: Boolean,
    default: false,
  },
  disabled: {
    type: Boolean,
    default: false,
  },
});
</script>
<template>
  <div class="form-group">
    <label :for="id">
      <slot />
    </label>
    <textarea
      :id="id"
      class="form-control bg-white"
      :rows="rows"
      :placeholder="placeholder"
      :value="value"
      :readonly="readonly"
      :disabled="disabled"
    ></textarea>
  </div>
</template>
