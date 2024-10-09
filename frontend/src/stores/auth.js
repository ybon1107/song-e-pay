import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";

const initState = {
  token: "",
  user: {
    userId: "",
    countryCode: "",
    roles: [],
  },
};

export const useAuthStore = defineStore("auth", () => {
  const state = ref({ ...initState });

  const isLogin = computed(() => !!state.value.user.username);


  const userId = computed(() => state.value.user.userId);


  const user = computed(() => state.value.user);

  const load = () => {
    const auth = localStorage.getItem("auth");
    if (auth != null) {
      state.value = JSON.parse(auth);
    }
  };

  const login = async (member) => {
    console.log("login: ", member);
    console.log("username: ", member.value.username);
    console.log("password: ", member.value.password);

    try {

      const response = await axios.post("/api/auth/login", member.value);
      console.log("response : ", response);
      console.log("response.data : ", response.data);
      state.value = { ...response.data };
      switch (state.value.user.countryCode) {
        case 0:
          state.value.user.country = "한국";
          state.value.user.language = "ko";
          break;
        case 1:
          state.value.user.country = "미국";
          state.value.user.language = "en";
          break;
        case 2:
          state.value.user.country = "인도네시아";
          state.value.user.language = "id";
          break;
        case 3:
          state.value.user.country = "베트남";
          state.value.user.language = "vi";
          break;
      }
      localStorage.setItem("auth", JSON.stringify(state.value));
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  // 프로필 변경 시 state 업데이트 및 localStorage 저장
  const updateProfileState = (updatedData) => {
    Object.assign(state.value.user, updatedData);
    console.log("updateProfileState : ", updatedData);

    // countryCode에 따른 국가와 언어 설정
    switch (state.value.user.countryCode) {
      case 0:
        state.value.user.country = "한국";
        state.value.user.language = "ko";
        break;
      case 1:
        state.value.user.country = "미국";
        state.value.user.language = "en";
        break;
      case 2:
        state.value.user.country = "인도네시아";
        state.value.user.language = "id";
        break;
      case 3:
        state.value.user.country = "베트남";
        state.value.user.language = "vi";
        break;
    }

    // localStorage에 업데이트된 사용자 정보 저장
    localStorage.setItem("auth", JSON.stringify(state.value));
  };

  const logout = () => {
    localStorage.clear();
    state.value = { ...initState };
  };

  //const getToken = () => state.value.token;

  const changeProfile = (member) => {
    state.value.user.userId = member.email;
    localStorage.setItem("auth", JSON.stringify(state.value));
  };

  load();

  // 토큰을 가져오고, 사용자의 이메일을 업데이트하며, 초기 상태를 불러오는 기능을 수행
  // getToken(): 현재 상태(state.value)에서 token 값을 반환합니다.
  // changeProfile(member): 사용자의 이메일을 주어진 member.email로 변경하고, 변경된 상태를 localStorage에 저장합니다.
  // load(): 페이지가 로드될 때 localStorage에서 저장된 인증 정보를 불러와 state에 설정

  //   return { state, username, email, isLogin, changeProfile, login, logout, getToken };

  return {
    state,
    userId,
    user,
    isLogin,
    changeProfile,
    login,
    logout,
    updateProfileState,
  };
});
