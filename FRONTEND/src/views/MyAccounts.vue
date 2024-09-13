<script setup>
import DefaultInfoCard from '@/views/Cards/DefaultInfoCard.vue';
</script>
<template>
  <div class="row">
    <div class="col-md-4">
      <default-info-card title="Paypal" description="Freelance Payment" value="$455.00">
        <!-- icon slot에 <flag iso="us" /> 컴포넌트 전달 -->
        <template #icon>
          <flag iso="us" />
        </template>
      </default-info-card>
    </div>
    <!-- 원화 머니 사용 -->
    <div class="col-md-4">
      <default-info-card title="Paypal" description="Freelance Payment" value="$455.00">
        <!-- icon slot에 <flag iso="us" /> 컴포넌트 전달 -->
        <template #icon>
          <flag iso="kr" />
        </template>
      </default-info-card>
    </div>
  </div>
</template>
