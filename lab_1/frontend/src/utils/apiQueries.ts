import axios from 'axios';

const API_URL = '/api';

export const fetchData = async (query: string) => {
  const response = await axios.get(API_URL+query);
  return response.data;
};

