import { useEffect, useMemo, useState } from 'react';
import { getContratos } from '../services/contratosService';

const initialForm = {
  rutCliente: '',
  idEspacio: '',
  tipoPlan: '',
};

const useContratos = () => {
  const [contratos, setContratos] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState(initialForm);

  useEffect(() => {
    const loadContratos = async () => {
      try {
        setLoading(true);
        const data = await getContratos();
        setContratos(Array.isArray(data) ? data : []);
        setError('');
      } catch (requestError) {
        setContratos([]);
        setError('No fue posible cargar los contratos desde el BFF.');
        console.error(requestError);
      } finally {
        setLoading(false);
      }
    };

    loadContratos();
  }, []);

  const totalContratos = useMemo(() => contratos.length, [contratos]);

  const handleOpenModal = () => {
    setFormData(initialForm);
    setShowModal(true);
  };

  const handleCloseModal = () => setShowModal(false);

  const handleChange = (event) => {
    const { name, value } = event.target;

    setFormData((currentForm) => ({
      ...currentForm,
      [name]: value,
    }));
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    console.log('Nuevo contrato', formData);
    handleCloseModal();
  };

  const handleEditContrato = (contrato) => {
    console.log('Editar contrato', contrato);
  };

  const handleVerDetalle = (contrato) => {
    console.log('Ver detalle contrato', contrato);
  };

  return {
    contratos,
    loading,
    error,
    showModal,
    formData,
    totalContratos,
    handleOpenModal,
    handleCloseModal,
    handleChange,
    handleSubmit,
    handleEditContrato,
    handleVerDetalle,
  };
};

export default useContratos;