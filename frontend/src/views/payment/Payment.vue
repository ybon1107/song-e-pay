<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="mx-auto col-xl-4 col-lg-5 col-md-7 d-flex flex-column mx-lg-0">
        <div class="d-none d-xl-block">
          <div>
            <img src="../../assets/img/mushroom.png" class="p-3 w-100" alt="mushroom" />
          </div>
          <h4 class="text-center">Our payment system is only available in a mobile environment.</h4>
        </div>
        <div class="d-xl-none">
          <div class="d-flex flex-column justify-content-center align-items-center gap-5 py-5">
            <div class="card card-img-bg bg-info" :style="{ backgroundImage: 'url(/images/won-e-money.png)' }">
            </div>
            <router-link to="/payment/password">
              <button class="btn btn-warning mb-0">
                {{ $t('payment--button-pay') }}
              </button>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
</script>