<template>
  <div class="card widget-calendar">
    <div class="p-3 pb-0 card-header">
      <h6 class="mb-0">{{ props.title }}</h6>
      <div class="d-flex">
        <div class="mb-0 text-sm p font-weight-bold widget-calendar-day">
          {{ props.day }}
        </div>
        <div class="mb-1 text-sm p font-weight-bold widget-calendar-year">
          {{ props.year }}
        </div>
      </div>
    </div>
    <div class="p-3 card-body">
      <div :id="props.id" data-toggle="widget-calendar"></div>
    </div>
  </div>
</template>

<script setup>
import { onBeforeUnmount, onMounted, ref } from "vue";
import { Calendar } from "@fullcalendar/core";
import dayGridPlugin from "@fullcalendar/daygrid";

let calendar;

const props = defineProps({
  id: {
    type: String,
    default: "widget-calendar",
  },
  title: {
    type: String,
    default: "",
  },
  day: {
    type: String,
    default: "",
  },
  year: {
    type: String,
    default: "",
  },
  initialView: {
    type: String,
    default: "dayGridMonth",
  },
  initialDate: {
    type: String,
    default: () => new Date().toISOString().split("T")[0], // Use a function for default
  },
  events: {
    type: Array,
    default: () => [],
  },
  selectable: {
    type: Boolean,
    default: true,
  },
  editable: {
    type: Boolean,
    default: true,
  },
});

onMounted(() => {
  calendar = new Calendar(document.getElementById(props.id), {
    contentHeight: "auto",
    plugins: [dayGridPlugin],
    initialView: props.initialView,
    selectable: props.selectable,
    editable: props.editable,
    events: props.events,
    initialDate: props.initialDate,
    headerToolbar: {
      start: "title",
      center: "",
      end: "today prev,next",
    },
    views: {
      month: {
        titleFormat: {
          month: "long",
          year: "numeric",
        },
      },
      agendaWeek: {
        titleFormat: {
          month: "long",
          year: "numeric",
          day: "numeric",
        },
      },
      agendaDay: {
        titleFormat: {
          month: "short",
          year: "numeric",
          day: "numeric",
        },
      },
    },
  });

  calendar.render();
});

onBeforeUnmount(() => {
  if (calendar) {
    calendar.destroy();
  }
});
</script>
