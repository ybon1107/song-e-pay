import axios from "axios";
import { useAuthStore } from "@/stores/auth";

const api = axios.create({
  baseURL: "/api/notification",
  headers: { "Content-Type": "application/json" },
});

export default {
  async getNotification(userId) {
    console.log("userId: ", userId);
    const notification = (await api.get(`/${userId}`)).data;
    console.log("Api noti: ", notification);
    return notification;
  },
  async deleteNotification(notificationId) {
    console.log("notificationId: ", notificationId);
    // const response = (await api.get(`/${userId}`)).data;
    // console.log("response: ", response);
    return notificationId;
  },
}