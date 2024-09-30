import axios from 'axios';

const api = axios.create({
  baseURL: '/api/setting',
  headers: { 'Content-Type': 'application/json' }
});

export default {
    async changePassword(formData) {
        console.log('formData: ', formData);
        const { data } = await api.patch('/change-password',formData);
        console.log('AUTH PUT: ', data);
        return data;
      },
    
    async delete(userNo) {
      console.log('userNo: ', userNo);
      const { data } = await api.delete(`/bye/${userNo}`);
      console.log('delete: ', data);
      window.location.href = '/';
    },


}