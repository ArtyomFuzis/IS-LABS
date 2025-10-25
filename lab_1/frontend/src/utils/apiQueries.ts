import axios from 'axios';

const API_URL = '/api';

export const fetchData = async (query: string) => {
  const response = await axios.get(API_URL+query);
  return response.data;
};

export const createObject = async (query: string, dataForm: URLSearchParams) => {
  const response = await axios.post(API_URL+query, dataForm,{
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'
    }
  });
  return response.data;
};

export const removeObject = async (query: string) => {
  const response = await axios.delete(API_URL+query);
  return response.data;
};


