import { ref, computed, reactive } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';


const initState = {
  token: '',
  user: {
    userNo: '',
    accountNo: '',
    songNo: '',
    krwNo: '',
    countryCode: '',
    userId: '',
    password: '',
    firstName: '',
    lastName: '',
    birthday: '',
    gender: '',
    phoneNo: '',
    secondPwd: '',
    profilePic: '',
    address: '',
    createAt: '',
    updateAt: '',
  },
};


export const useAuthStore = defineStore('auth', () => {
  const state = reactive({ ...initState });

  const isLogin = computed(() => !!state.user.userNo);

  const username = computed(() => state.user.userId);
  const email = computed(() => state.user.userId);

  const load = () => {
    const auth = localStorage.getItem('auth');
    if (auth != null) {
      Object.assign(state, JSON.parse(auth)); // 전체 객체를 reactive에 적용할 때는 Object.assign이 필요
    }
  };

  const login = async (member) => {
    const formData = new FormData();
    formData.append('userId', member.email);
    formData.append('password', member.password);

    const { data } = await axios.post('/api/users/login', formData);
    Object.assign(state, data); // reactive는 전체 객체를 바로 재할당하는 것이 어려움
    localStorage.setItem('auth', JSON.stringify(state));
  };

  const logout = () => {
    localStorage.clear();
    Object.assign(state, initState); // reactive는 전체 재할당 대신 Object.assign 사용
  };

  const changeProfile = (member) => {
    state.user.email = member.email;
    localStorage.setItem('auth', JSON.stringify(state));
  };

  load();

  return { state, username, email, isLogin, changeProfile, login, logout };
});
