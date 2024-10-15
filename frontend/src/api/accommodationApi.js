import api from '@/api';

const BASE_URL = "/api/accommodation";

export default {

  async getAll() {
    const accommodation = (await api.get(`${BASE_URL}`)).data;
    console.log("Api accommodationList: ", accommodation);
    return accommodation;
  },

  async getAccommodation(accommodationNo) {
    console.log("Api accommodationNo: ", accommodationNo);
    const accommodation = (await api.get(`${BASE_URL}/${accommodationNo}`)).data;
    console.log("Api accommodation: ", accommodation);
    return accommodation;
  },
};