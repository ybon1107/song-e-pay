import axios from "axios";
import { useAuthStore } from "@/stores/auth";

const api = axios.create({
  baseURL: "/api/users",
  headers: { "Content-Type": "application/json" },
});

export default {
  async getUser(userId) {
    console.log("userId: ", userId);
    const user = (await api.get(`/${userId}`)).data;
    console.log("Api user: ", user);
    return user;
  },

  async getUserImg(userId) {
    console.log("userId: ", userId);
    const img = (await api.get(`/profile-img/${userId}`)).data;
    return img;
  },
};
