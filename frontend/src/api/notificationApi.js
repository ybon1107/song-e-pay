import api from '@/api';

const BASE_URL = "/api/notification";

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
  async getNotification(userId) {
    console.log("userId: ", userId);
    const notification = (await api.get(`${BASE_URL}/${userId}`)).data;
    console.log("Api noti: ", notification);
    return notification;
  },

  async deleteNotification(notiNo) {
    console.log("notiNo: ", notiNo);
<<<<<<< HEAD
    const response = (await api.delete(`/${notiNo}`)).data;
=======
    const response = (await api.delete(`${BASE_URL}/${notiNo}`)).data;
>>>>>>> a3b5ae426b7f1c856bee370fcd645a3b8079e823
    console.log("response: ", response);
    return response;
  },

  async readNotification(notiNo) {
    console.log("notiNo: ", notiNo);
<<<<<<< HEAD
    const response = (await api.patch(`/${notiNo}`)).data;
=======
    const response = (await api.patch(`${BASE_URL}/${notiNo}`)).data;
>>>>>>> a3b5ae426b7f1c856bee370fcd645a3b8079e823
    console.log("response: ", response);
    return response;
  },
}