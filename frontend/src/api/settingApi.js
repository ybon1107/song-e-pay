import axios from 'axios';
import { useAuthStore } from '@/stores/auth';

const api = axios.create({
  baseURL: '/api/setting',
  headers: { 'Content-Type': 'application/json' }
});

export default {

  async updateProfile(userInfo) {
    console.log('userInfo: ', userInfo);
    let headers = { 'Content-Type': 'multipart/form-data' };

    const formData = new FormData();

    formData.append('userId', userInfo.userId);
    formData.append('address', userInfo. address);
    formData.append('postCode', userInfo.postCode);
    formData.append('countryCode', userInfo.countryCode);
    
    if (userInfo.profilePic) {
      formData.append('profilePicFile', userInfo.profilePic);
      console.log('profilePic: ', userInfo.profilePic);
    }

    const { data } = await api.post('/', formData, { headers });
    console.log('updateProfile: ', data);
    
    
    const auth = useAuthStore();
    auth.updateProfileState(data);
    await auth.fetchUser(data.userId);
    return data;
  },

  async changePassword(formData) {
    console.log('formData: ', formData);
    const { data } = await api.patch('/change-password', formData);
    console.log('AUTH PUT: ', data);
    return data;
  },

  async delete(userId) {
    console.log('userId: ', userId);
    const { data } = await api.delete(`/bye/${userId}`);
    console.log('delete: ', data);
    window.location.href = '/';
  },

}