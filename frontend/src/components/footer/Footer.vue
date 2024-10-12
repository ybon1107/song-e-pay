<template>
  <footer class="py-3 footer">
    <div class="container-fluid">
      <div class="row align-items-center justify-content-center">
        <!-- 가운데 정렬 -->
        <div class="mb-4 col-lg-6">
          <div
            class="text-sm text-center text-lg-center copyright text-muted mt-8"
          >
            ©
            {{ new Date().getFullYear() }}, made with by
            <a
              href="https://github.com/ybon1107/song-e-pay.git"
              class="font-weight-bold"
              target="_blank"
              >Beoseot Dojeokdan</a
            >
            for a better web.
          </div>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { useStore } from "vuex";

const store = useStore();
</script>
