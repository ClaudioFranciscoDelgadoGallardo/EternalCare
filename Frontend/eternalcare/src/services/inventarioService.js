import axiosConfig from '../api/axiosConfig';

const getEspacios = async () => {
  const { data } = await axiosConfig.get('/espacios');
  return data;
};

export { getEspacios };