import { ref } from 'vue';
import { defineStore } from 'pinia';

export const useSignupStore = defineStore('signup', () => {
  const email = ref(sessionStorage.getItem('email') || '');
  const countryCallingCode = ref(sessionStorage.getItem('countryCallingCode') || '');
  const phoneNumber = ref(sessionStorage.getItem('phoneNumber') || '');

  const setEmail = (newEmail) => {
    email.value = newEmail;
    sessionStorage.setItem('email', newEmail);
  };

  const setCountryCallingCode = (newCountryCallingCode) => {
    countryCallingCode.value = newCountryCallingCode;
    sessionStorage.setItem('countryCallingCode', newCountryCallingCode);
  };

  const setPhoneNumber = (newPhoneNumber) => {
    phoneNumber.value = newPhoneNumber;
    sessionStorage.setItem('phoneNumber', newPhoneNumber);
  };

  const clearSessionStorage = () => {
    sessionStorage.removeItem('email');
    sessionStorage.removeItem('countryCallingCode');
    sessionStorage.removeItem('phoneNumber');
  };

  return {
    email,
    countryCallingCode,
    phoneNumber,
    setEmail,
    setCountryCallingCode,
    setPhoneNumber,
    clearSessionStorage,
  };
});
