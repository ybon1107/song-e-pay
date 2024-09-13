<script setup>
import AuthorsTable from "@/components/viewsComponents/AuthorsTable.vue";
import ProjectsTable from "@/components/viewsComponents/ProjectsTable.vue";
</script>
<template>
  <div class="py-4 container-fluid">
    <div class="row">
      <div class="col-12">
        <authors-table />
      </div>
    </div>
    <div class="mt-4 row">
      <div class="col-12">
        <projects-table />
      </div>
    </div>
  </div>
</template>
