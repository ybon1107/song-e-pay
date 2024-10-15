<script setup>
import Alert from './Alert.vue';
// import ExchangeRate from './ExchangeRate.vue';
import MyAccount from './MyAccount.vue';
</script>

<template>
    <div class="container-fluid">
        <div id="my-account" class="d-grid gap-5">
            <MyAccount />
        </div>
        <div class="custom-spacer"></div>

        <!-- Graph and Conversion Section -->
        <!-- <div id="exchange-rate" class="d-grid gap-5">
      <ExchangeRate />
    </div>
    <div class="custom-spacer"></div> -->

        <div id="set-alert" class="d-grid gap-5">
            <Alert />
        </div>

        <div class="custom-spacer"></div>
    </div>
</template>
