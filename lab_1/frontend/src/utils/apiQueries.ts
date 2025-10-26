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

export const extra1 = async (id: string) => {
  const response = await axios.delete(API_URL+"/operations/extra/deleteAllLabsByAuthor/" + id);
  return response.data;
};

export const extra2 = async () => {
  const response = await axios.get(API_URL+"/operations/extra/maximumPointSum/");
  return response.data;
};

export const extra3 = async () => {
  const response = await axios.get(API_URL+"/operations/extra/minimalPointUnique/");
  return response.data;
};

export const extra4 = async (id: string, steps: string) => {
  const response = await axios.post(API_URL+"/operations/extra/increaseDifficulty/" + id + "/on/" + steps);
  return response.data;
};

export const extra5 = async (id: string) => {
  const response = await axios.delete(API_URL+"/operations/extra/deleteLabWorkFromDiscipline/" + id);
  return response.data;
};



