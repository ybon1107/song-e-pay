import api from '@/api';
import mulApi from '@/api/multipartApi';
import { useAuthStore } from '@/stores/auth';

const BASE_URL = '/api/setting';

export default {
  async updateProfile(userInfo) {
    console.log('userInfo: ', userInfo);

    const formData = new FormData();

    formData.append('userId', userInfo.userId);
    formData.append('address', userInfo.address);
    formData.append('postCode', userInfo.postCode);
    formData.append('countryCode', userInfo.countryCode);

    if (userInfo.profilePic) {
      formData.append('profilePicFile', userInfo.profilePic);
      console.log('profilePic: ', userInfo.profilePic);
    }

    const { data } = await mulApi.post(`${BASE_URL}/`, formData);
    console.log('updateProfile: ', data);

    const auth = useAuthStore();
    auth.updateProfileState(data);
    await auth.fetchUser(data.userId);
    return data;
  },

  async changePassword(formData) {
    console.log('formData: ', formData);
    const { data } = await api.patch(
      `${BASE_URL}/change-password`,
      formData
    );
    console.log('AUTH PUT: ', data);
    return data;
  },

  async delete(userId) {
    console.log('userId: ', userId);
    const { data } = await api.delete(`${BASE_URL}/bye/${userId}`);
    console.log('delete: ', data);
    window.location.href = '/';
  },

  async submitSecPwd(formData) {
    try {
      const response = await api.post(`${BASE_URL}/check-secpwd`, formData);
      return response;
    } catch (error) {
      console.error('2차 비밀번호 제출 오류:', error.response.data);
      throw error;
    }
  },
  async changeSecPwd(formData) {
    try {
      console.log(formData);
      const response = await api.patch(`${BASE_URL}/change-secpwd`, formData);
      return response;
    } catch (error) {
      console.error('2차 비밀번호 제출 오류:', error.response.data);
      throw error;
    }
  },
  async submitAccount(formData) {
    try {
      const response = await api.patch(`${BASE_URL}/register-account`, formData);
      return response;
    } catch (error) {
      console.error('계좌 제출 오류:', error.response.data);
      throw error;
    }
  },
};
