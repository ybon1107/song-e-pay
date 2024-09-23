import { defineStore } from "pinia";
import { ref } from "vue";

export const useExchangeStore = defineStore("exchange", () => {
  const currentToKrw = ref(0);

  // Function to update the value
  const setCurrentToKrw = (newRate) => {
    currentToKrw.value = newRate;
  };

  return {
    currentToKrw,
    setCurrentToKrw,
  };
});
