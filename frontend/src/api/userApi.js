import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const api = axios.create({
  baseURL: '/api/users',
  headers: { 'Content-Type': 'application/json' },
});

export default {
  async getUserImg(userId) {
    const img = (await api.get(`/profile-img/${userId}`)).data;
    return img;
  },
};
