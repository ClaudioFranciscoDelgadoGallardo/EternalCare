import axiosConfig from '../api/axiosConfig';

const getContratos = async () => {
  const { data } = await axiosConfig.get('/contratos');
  return data;
};

export { getContratos };