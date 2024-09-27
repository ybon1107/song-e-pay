<template>
  <footer class="py-3 footer">
    <div class="container-fluid">
      <div class="row align-items-center justify-content-lg-between">
        <div class="mb-4 col-lg-6 mb-lg-0">
          <div
            class="text-sm text-center copyright text-muted"
            :class="store.state.isRTL ? 'text-lg-end' : 'text-lg-start'"
          >
            ©
            {{ new Date().getFullYear() }}, made with
            <i class="fa fa-heart"></i> by
            <a
              href="https://www.creative-tim.com"
              class="font-weight-bold"
              target="_blank"
              >Creative Tim</a
            >
            for a better web.
          </div>
        </div>
        <div class="col-lg-6">
          <ul
            class="nav nav-footer justify-content-center justify-content-lg-end"
          >
            <li class="nav-item">
              <a
                href="https://www.creative-tim.com"
                class="nav-link text-muted"
                target="_blank"
                >Creative Tim</a
              >
            </li>
            <li class="nav-item">
              <a
                href="https://www.creative-tim.com/presentation"
                class="nav-link text-muted"
                target="_blank"
                >About Us</a
              >
            </li>
            <li class="nav-item">
              <a
                href="https://www.creative-tim.com/blog"
                class="nav-link text-muted"
                target="_blank"
                >Blog</a
              >
            </li>
            <li class="nav-item">
              <a
                href="https://www.creative-tim.com/license"
                class="nav-link pe-0 text-muted"
                target="_blank"
                >License</a
              >
            </li>
          </ul>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { onMounted } from "vue";
import { useExchangeStore } from "@/stores/exchangeStore";
import { useStore } from "vuex";

const store = useStore();
const exchangeStore = useExchangeStore();

const usdToKrwUrl =
  "https://v6.exchangerate-api.com/v6/6bbbf78cc42a296d533a9e6b/pair/USD/KRW";
const krwToUsdUrl =
  "https://v6.exchangerate-api.com/v6/6bbbf78cc42a296d533a9e6b/pair/KRW/USD";
const usdToKrwUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/USD/KRW`;
const krwToUsdUrl = `https://v6.exchangerate-api.com/v6/${import.meta.env.VITE_EXCHANGE_RATE_API_KEY}/pair/KRW/USD`;

const fetchExchangeRates = async () => {
  try {
    const [usdToKrwResponse, krwToUsdResponse] = await Promise.all([
      fetch(usdToKrwUrl),
      fetch(krwToUsdUrl),
    ]);

    if (!usdToKrwResponse.ok || !krwToUsdResponse.ok) {
      throw new Error("Network response was not ok");
    }

    const [usdToKrwData, krwToUsdData] = await Promise.all([
      usdToKrwResponse.json(),
      krwToUsdResponse.json(),
    ]);

    exchangeStore.setCurrentToKrw(usdToKrwData.conversion_rate);
    exchangeStore.setCurrentFromKrw(krwToUsdData.conversion_rate);

    console.log("환율 데이터가 성공적으로 로드되었습니다.");
    return {
      currentToKrw: usdToKrwData.conversion_rate,
      currentFromKrw: krwToUsdData.conversion_rate,
    };
  } catch (error) {
    console.error("Error fetching exchange rate data", error);
  }
};

onMounted(() => {
  fetchExchangeRates();
});
</script>
