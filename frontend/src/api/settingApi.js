import api from '@/api';
import { useAuthStore } from "@/stores/auth";

const BASE_URL = "/api/setting";
const headers = { 'Content-Type': 'multipart/form-data' };

api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
}, (error) => {
  return Promise.reject(error);
});

export default {
  async updateProfile(userInfo) {
    console.log("userInfo: ", userInfo);

    const formData = new FormData();

    formData.append("userId", userInfo.userId);
    formData.append("address", userInfo.address);
    formData.append("postCode", userInfo.postCode);
    formData.append("countryCode", userInfo.countryCode);

    if (userInfo.profilePic) {
      formData.append("profilePicFile", userInfo.profilePic);
      console.log("profilePic: ", userInfo.profilePic);
    }

    const { data } = await api.post(`${BASE_URL}/`, formData, headers);
    console.log("updateProfile: ", data);

    const auth = useAuthStore();
    auth.updateProfileState(data);
    await auth.fetchUser(data.userId);
    return data;
  },

  async changePassword(formData) {
    console.log("formData: ", formData);
    const { data } = await api.patch(`${BASE_URL}/change-password`, formData);
    console.log("AUTH PUT: ", data);
    return data;
  },

  async delete(userId) {
    console.log("userId: ", userId);
    const { data } = await api.delete(`${BASE_URL}/bye/${userId}`);
    console.log("delete: ", data);
    window.location.href = "/";
  },


  async submitSecPwd(formData) {
    try {
<<<<<<< HEAD
      const response = await api.post('/check-secpwd', formData);
=======
      const response = await api.post(`${BASE_URL}/check-secpwd`, formData);
>>>>>>> a3b5ae426b7f1c856bee370fcd645a3b8079e823
      return response;
    } catch (error) {
      console.error('비밀번호 제출 오류:', error.response.data);
      throw error;
    }
  },
};
