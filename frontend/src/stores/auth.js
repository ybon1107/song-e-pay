import { ref, computed } from 'vue';
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
    country: '',
    language: '',
    createAt: '',
    updateAt: '',
  },
};

export const useAuthStore = defineStore('auth', () => {
  const state = ref({ ...initState });

  const isLogin = computed(() => !!state.value.user.userNo);

  const userNo = computed(() => state.value.user.userNo);

  const user = computed(() => state.value.user);

  // 이메일 가져오기 (여기서는 userId가 이메일 역할)
  const email = computed(() => state.value.user.userId);

  const load = () => {
    const auth = localStorage.getItem('auth');
    if (auth != null) {
      // Object.assign을 사용해 로컬 스토리지의 데이터를 state.value에 병합
      Object.assign(state.value, JSON.parse(auth)); // 전체 객체를 reactive에 적용할 때는 Object.assign이 필요
    }
  };

  const login = async (member) => {
    // state.value.token = 'test token';
    // state.value.user = { username: member.username, email: member.username + '@test.com' }   ;
    const formData = new FormData();
    formData.append('userId', member.user.email); // 이메일을 userId로 전송
    formData.append('password', member.user.password); // 비밀번호 추가

    try {
      const response = await axios.post('/api/users/login', formData);
      Object.assign(state.value.user, { ...response.data });
      switch (state.value.user.countryCode) {
        case 0:
          state.value.user.country = '한국';
          state.value.user.language = 'ko';
          break;
        case 1:
          state.value.user.country = '미국';
          state.value.user.language = 'en';
          break;
        case 2:
          state.value.user.country = '인도네시아';
          state.value.user.language = 'id';
          break;
        case 3:
          state.value.user.country = '베트남';
          state.value.user.language = 'vi';
          break;
      }
      localStorage.setItem('auth', JSON.stringify(state.value));
    } catch (error) {
      console.error(error);
      throw error
    }

    // api 호출
    // const { data } = await axios.post('/api/users/login', formData);
    // Object.assign(state.value, data);
    // localStorage.setItem('auth', JSON.stringify(state.value));
  };

  const logout = () => {
    localStorage.clear();
    Object.assign(state.value, initState);
  };

  //const getToken = () => state.value.token;

  const changeProfile = (member) => {
    state.value.user.userId = member.email;
    localStorage.setItem('auth', JSON.stringify(state.value));
  };

  load();

  // 토큰을 가져오고, 사용자의 이메일을 업데이트하며, 초기 상태를 불러오는 기능을 수행
  // getToken(): 현재 상태(state.value)에서 token 값을 반환합니다.
  // changeProfile(member): 사용자의 이메일을 주어진 member.email로 변경하고, 변경된 상태를 localStorage에 저장합니다.
  // load(): 페이지가 로드될 때 localStorage에서 저장된 인증 정보를 불러와 state에 설정

  //   return { state, username, email, isLogin, changeProfile, login, logout, getToken };
  return { state, userNo, user, email, isLogin, changeProfile, login, logout };
});