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

  const isLogin = computed(() => !!state.value.user.userNo);

  const username = computed(() => state.value.user.userId);
  const email = computed(() => state.value.user.userId);

  const load = () => {
    const auth = localStorage.getItem('auth');
    console.log();
    if (auth != null) {
      state.value = JSON.parse(auth);
    }
  };

  const login = async (member) => {
    // state.value.token = 'test token';
    // state.value.user = { username: member.username, email: member.username + '@test.com' }   ;
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

// 토큰을 가져오고, 사용자의 이메일을 업데이트하며, 초기 상태를 불러오는 기능을 수행
// getToken(): 현재 상태(state.value)에서 token 값을 반환합니다.
// changeProfile(member): 사용자의 이메일을 주어진 member.email로 변경하고, 변경된 상태를 localStorage에 저장합니다.
// load(): 페이지가 로드될 때 localStorage에서 저장된 인증 정보를 불러와 state에 설정

//   return { state, username, email, isLogin, changeProfile, login, logout, getToken };
    return { state, username, email, isLogin, changeProfile, login, logout };
});