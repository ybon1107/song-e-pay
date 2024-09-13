<script setup>
import { computed } from 'vue'; // Import computed from Vue

const emit = defineEmits(['update:modelValue']);

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
  type: {
    type: String,
    default: 'text',
  },
  isRequired: {
    type: Boolean,
    default: false,
  },
});

const getClasses = (size, success, error) => {
  let sizeValue, isValidValue;

  sizeValue = size ? `form-control-${size}` : '';

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

const hasIcon = (icon) => (icon ? 'input-group' : '');

const formatNumber = (value) => {
  if (!value) return '';
  return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};

const formattedValue = computed({
  get() {
    let val = props.modelValue.replace(/[^\d]/g, '');
    if (val.length > 1 && val[0] === '0') {
      val = val.slice(1);
    }
    return val ? `${formatNumber(val)} 원` : '';
  },
  set(newValue) {
    const rawValue = newValue.replace(/[^\d]/g, '');
    emit('update:modelValue', rawValue);
  },
});

const handleInput = (event) => {
  const value = event.target.value.replace(/[^\d]/g, '');
  emit('update:modelValue', value);
};
</script>

<template>
  <div class="form-group">
    <div :class="hasIcon(props.icon)">
      <span v-if="props.iconDir === 'left'" class="input-group-text">
        <i :class="getIcon(props.icon)"></i>
      </span>
      <input
        :id="props.id"
        :type="props.type"
        class="form-control"
        :class="getClasses(props.size, props.success, props.error)"
        :name="props.name"
        :placeholder="props.placeholder"
        :value="formattedValue"
        :required="props.isRequired"
        @input="handleInput"
      />
      <span v-if="props.iconDir === 'right'" class="input-group-text">
        <i :class="getIcon(props.icon)"></i>
      </span>
    </div>
  </div>
</template>
