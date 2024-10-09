<script setup>
import { defineProps } from "vue";

const props = defineProps({
  items: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <div class="accordion">
    <div v-for="(item, index) in items" :key="index" class="accordion-item">
      <h6 class="accordion-header" :id="'heading' + index">
        <button
          class="accordion-button"
          :class="{
            collapsed: !item.expanded,
          }"
          type="button"
          data-bs-toggle="collapse"
          :data-bs-target="'#collapse' + index"
          :aria-expanded="item.expanded"
          :aria-controls="'collapse' + index"
        >
          {{ item.title }}
        </button>
      </h6>
      <div
        :id="'collapse' + index"
        class="accordion-collapse collapse"
        :class="{ show: item.expanded }"
        :aria-labelledby="'heading' + index"
        data-bs-parent="#accordionExample"
      >
        <div class="accordion-body">
          <p>{{ item.content }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style>
.font-weight-bold {
  font-weight: bold;
}
</style>
