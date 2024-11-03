import { ref, computed } from 'vue';
import { defineStore } from 'pinia';
import axios from 'axios';
import userApi from '@/api/userApi';

const initState = {
    token: '',
    user: {
        userId: '',
        countryCode: '',
        roles: [],
    },
};

export const useAuthStore = defineStore('auth', () => {
    const state = ref({ ...initState });
    const user = ref(null);

    const isLogin = computed(() => !!state.value.user.userId);
    const userId = computed(() => state.value.user.userId);

    const fetchUser = async (userId) => {
        try {
            const userData = await userApi.getUser(userId);
            user.value = userData;
            console.log(
                '================================fetch user : ',
                user.value
            );
        } catch (error) {
            console.error('사용자 정보를 가져오는 데 실패했습니다:', error);
        }
    };

    const load = async () => {
        const auth = localStorage.getItem('auth');
        if (auth != null) {
            state.value = JSON.parse(auth);
            console.log('load auth : ', state.value);
            if (state.value.token && state.value.user.userId) {
                await fetchUser(state.value.user.userId);
                console.log(
                    '================================load user : ',
                    user.value
                );
            }
        }
    };

    const login = async (member) => {
        try {
            // const response = await axios.post("/api/auth/login", member.value);
            // console.log("response : ", response);
            // console.log("response.data : ", response.data);

            // state.value = { ...response.data };

            const { data } = await axios.post('/api/auth/login', member.value);
            console.log('data : ', data);

            state.value = { ...data };

            await fetchUser(state.value.user.userId);

            switch (state.value.user.countryCode) {
                case 0:
                    localStorage.setItem('Language', 'ko');
                    break;
                case 1:
                    localStorage.setItem('Language', 'en');
                    break;
                case 2:
                    localStorage.setItem('Language', 'id');
                    break;
                case 3:
                    localStorage.setItem('Language', 'vi');
                    break;
            }
            localStorage.setItem('auth', JSON.stringify(state.value));
            // await fetchUser(state.value.user.userId);
        } catch (error) {
            console.error(error);
            throw error;
        }
    };

    // 프로필 변경 시 state 업데이트 및 localStorage 저장
    const updateProfileState = (updatedData) => {
        Object.assign(state.value.user, updatedData);
        console.log('updateProfileState : ', updatedData);

        // localStorage에 업데이트된 사용자 정보 저장
        localStorage.setItem('auth', JSON.stringify(state.value));
    };

    const logout = () => {
        localStorage.removeItem('auth');
        state.value = { ...initState };
        user.value = null;
    };

    const getToken = () => {
        const auth = JSON.parse(localStorage.getItem('auth'));
        const token = auth ? auth.token : null;
        console.log('getToken : ', token);
        return token;
    };

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

    return {
        state,
        userId,
        user,
        isLogin,
        changeProfile,
        login,
        logout,
        updateProfileState,
        fetchUser,
        getToken,
    };
});
