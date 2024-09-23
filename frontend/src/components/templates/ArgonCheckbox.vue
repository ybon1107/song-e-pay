<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
  name: {
    type: String,
    default: "",
  },
  id: {
    type: String,
    default: "",
  },
  modelValue: {
    type: Boolean,
    default: false,
  },
});

const emit = defineEmits(["update:modelValue"]);

const handleChange = (event) => {
  emit("update:modelValue", event.target.checked);
};
</script>
<template>
  <div class="form-check">
    <input
      :id="id"
      class="form-check-input"
      type="checkbox"
      :name="name"
      :checked="modelValue"
      @change="handleChange"
    />
    <label :for="id" class="form-check-label" :class="$attrs.class">
      <slot />
    </label>
  </div>
</template>
