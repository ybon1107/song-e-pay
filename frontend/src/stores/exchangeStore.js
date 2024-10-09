import { defineStore } from "pinia";
import { ref } from "vue";

export const useExchangeStore = defineStore("exchange", () => {
  const currentToKrw = ref(0);
  const currentFromKrw = ref(0);

  // 값을 업데이트하는 함수
  const setCurrentToKrw = (newRate) => {
    currentToKrw.value = newRate;
  };

  const setCurrentFromKrw = (newRate) => {
    currentFromKrw.value = newRate;
  };

  // 환율 데이터 초기화 함수 추가
  const resetExchangeRates = () => {
    currentToKrw.value = 0;
    currentFromKrw.value = 0;
  };

  return {
    currentToKrw,
    currentFromKrw,
    setCurrentToKrw,
    setCurrentFromKrw,
    resetExchangeRates,
  };
});
