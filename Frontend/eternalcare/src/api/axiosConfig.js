import axios from 'axios';

const axiosConfig = axios.create({
  baseURL: 'http://localhost:8080/api/bff',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosConfig.interceptors.response.use(
  (response) => response,
  (error) => Promise.reject(error)
);

export default axiosConfig;