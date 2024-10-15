import api from '@/api';

const BASE_URL = '/api/notification';

export default {
    async getNotification(userId) {
        console.log('userId: ', userId);
        const notification = (await api.get(`${BASE_URL}/${userId}`)).data;
        console.log('Api noti: ', notification);
        return notification;
    },

    async deleteNotification(notiNo) {
        console.log('notiNo: ', notiNo);
        const response = (await api.delete(`${BASE_URL}/${notiNo}`)).data;
        console.log('response: ', response);
        return response;
    },

    async readNotification(notiNo) {
        console.log('notiNo: ', notiNo);
        const response = (await api.patch(`${BASE_URL}/${notiNo}`)).data;
        console.log('response: ', response);
        return response;
    },
};
