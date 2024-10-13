import axios from "axios";
import { useAuthStore } from "@/stores/auth";

const api = axios.create({
  baseURL: "/api/notification",
  headers: { "Content-Type": "application/json" },
});

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
    const notification = (await api.get(`/${userId}`)).data;
    console.log("Api noti: ", notification);
    return notification;
  },

  async deleteNotification(notiNo) {
    console.log("notiNo: ", notiNo);
    const response = (await api.delete(`/${notiNo}`)).data;
    console.log("response: ", response);
    return response;
  },

  async readNotification(notiNo) {
    console.log("notiNo: ", notiNo);
    const response = (await api.patch(`/${notiNo}`)).data;
    console.log("response: ", response);
    return response;
  },
}