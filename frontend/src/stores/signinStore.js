import { defineStore } from 'pinia'

export const useSigninStore = defineStore('signin', {
  state: () => ({
    email: ''
  }),
  actions: {
    setEmail(newEmail) {
      this.email = newEmail;
    }
  }
});