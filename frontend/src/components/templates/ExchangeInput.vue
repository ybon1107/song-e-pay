<script setup>
import { ref, computed, defineEmits, defineProps, watch } from 'vue';
import currencyFormatter from "../../js/currencyFormatter";
// const { formatNumber, reFormatNumber } = currencyFormatter;

const emit = defineEmits(["update:modelValue"]);

const props = defineProps({
    size: {
        type: String,
        default: "lg",
    },
    success: {
        type: Boolean,
        default: false,
    },
    error: {
        type: Boolean,
        default: false,
    },
    errorText: {
        type: String,
        default: "",
    },
    name: {
        type: String,
        default: "",
    },
    id: {
        type: String,
        default: "",
    },
    modelValue: {
        type: String,
        default: "",
    },
    placeholder: {
        type: String,
        default: "myAccount--input-placeholder",
    },
    type: {
        type: String,
        default: "text",
    },
    isRequired: {
        type: Boolean,
        default: false,
    },
    disabled: {
        type: Boolean,
        default: false,
    },
    currencyUnit: {
        type: String,
        default: "",
    },
    balance: {
        type: Number,
        default: 0,
    }
});

const getClasses = (size, success, error) => {
    let sizeValue, isValidValue;

    sizeValue = size ? `form-control-${size}` : null;

    if (error) {
        isValidValue = "is-invalid";
    } else if (success) {
        isValidValue = "is-valid";
    } else {
        isValidValue = "";
    }

    return `${sizeValue} ${isValidValue}`;
};

const isEditing = ref(false);
const internalValue = ref(props.modelValue);

const displayValue = computed(() => {
    if (isEditing.value) {
        return internalValue.value;
    }
    return formatNumber(internalValue.value);
});

const handleInput = (event) => {
    const value = event.target.value;
    const numericValue = value.replace(/[^0-9]/g, '');
    internalValue.value = numericValue;
    emit('update:modelValue', numericValue);
};

const handleFocus = (event) => {
    isEditing.value = true;
    event.target.value = internalValue.value;
};

const handleBlur = () => {
    isEditing.value = false;
};

const formatNumber = (value) => {
    if (!value) return '';
    return value.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
};
</script>
<template>
    <div class="form-group">
        <div class="input-group">
            <input :id="id" :type="type" class="form-control" :class="getClasses(size, success, error)" :name="name"
                :value="displayValue" :placeholder="placeholder" :required="isRequired" :disabled="disabled"
                :errorText="errorText" @input="handleInput" @focus="handleFocus" @blur="handleBlur" />
            <span class="input-group-text bg-secondary text-white">
                {{ currencyUnit }}
            </span>
            <div v-if="error" class="invalid-feedback text-xs">{{ $t(errorText) }}</div>
        </div>
    </div>
</template>

<style>
/* Chrome, Safari, Edge, Opera */
input::-webkit-outer-spin-button,
input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

/* Firefox */
input[type="number"] {
    -moz-appearance: textfield;
}
</style>
