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
          <div class="text-center mb-3">
            <img :src="qrCodeUrl" alt="QR Code">
          </div>
          <div class="btn btn-sm btn-warning mb-0 mx-4 d-flex justify-content-between">
            <p class="mb-0">보유잔액</p>
            <p class="mb-0">10,000원</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'

export default {
  setup() {
    const qrCodeUrl = ref('')

    const generateQRCode = () => {
      const url = encodeURIComponent('https://www.google.com/?hl=ko')
      qrCodeUrl.value = `http://localhost:8888/api/payment/qr?url=${url}`
    }

    onMounted(() => {
      generateQRCode()
    })

    return {
      qrCodeUrl
    }
  }
}
</script>