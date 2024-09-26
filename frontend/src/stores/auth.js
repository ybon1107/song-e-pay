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
  const state = ref({ ...initState });

  const user = JSON.parse(localStorage.getItem('auth'));
  const userNo = computed(() => {
    return user && user.userNo ? user.userNo : null
  });

  const isLogin = computed(() => {
    return user && user.userNo ? true : false;
  });


  console.log("user : ", user);
  console.log("userNo : ", userNo);

  const load = () => {
    const auth = localStorage.getItem('auth');
    console.log();
    if (auth != null) {
      state.value = JSON.parse(auth);
    }
  };

  const login = async (member) => {
    console.log("auth email : " + member.email);
    console.log("auth password : " + member.password);

    const formData = new FormData();
    formData.append('userId', member.email);
    formData.append('password', member.password);

    // api 호출
      const { data } = await axios.post('/api/users/login', formData);
      state.value = { ...data };
      console.log("data : " +data);
      localStorage.setItem('auth', JSON.stringify(state.value));
    
  };

  const logout = () => {
    localStorage.clear();
    state.value = { ...initState };
  };

//const getToken = () => state.value.token;

  const changeProfile = (member) => {
    state.value.user.email = member.email;
    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  load();

    return { state, user, userNo, isLogin, changeProfile, login, logout };
});