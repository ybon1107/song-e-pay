<template>
  <!-- Calendar Section -->
  <div class="row mt-3">
    <div class="col-lg-12">
      <div class="calendar-container">
        <calendar></calendar>
      </div>
    </div>
  </div>
</template>

<script setup>
import Calendar from "../Templates/Calendar.vue";
</script>

<style>
/* Responsiveness */
@media (max-width: 768px) {
  .calendar-container {
    height: auto;
  }
}
</style>
