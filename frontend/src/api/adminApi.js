import api from "@/api"

const BASE_URL = "/api/admin";

export default {
    async getStatistics() {
      const response = await api.get(`${BASE_URL}/statistics`);
      console.log("Api : ", response);
      return response;
    },
  }