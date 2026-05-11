import { useEffect, useState } from 'react';
import SectionHeader from '../components/SectionHeader';
import { getEspacios } from '../services/inventarioService';

const Inventario = () => {
  const [espacios, setEspacios] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadEspacios = async () => {
      try {
        const data = await getEspacios();
        setEspacios(Array.isArray(data) ? data : []);
      } catch {
        setEspacios([]);
      } finally {
        setLoading(false);
      }
    };

    loadEspacios();
  }, []);

  return (
    <div className="page-surface">
      <SectionHeader
        eyebrow="Módulo de inventario"
        title="Espacios"
        description="Listado inicial de espacios consumidos directamente desde el BFF."
      />

      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <div className="d-flex flex-column flex-md-row justify-content-between gap-3 mb-4">
            <div>
              <h2 className="h4 fw-bold mb-1">Resumen de espacios</h2>
              <p className="text-secondary mb-0">Estructura lista para búsqueda, filtros y acciones.</p>
            </div>
            <button type="button" className="btn btn-primary">
              Nuevo espacio
            </button>
          </div>

          {loading ? (
            <div className="text-center py-5">
              <div className="spinner-border text-primary" role="status" aria-label="Cargando" />
            </div>
          ) : (
            <div className="table-responsive">
              <table className="table align-middle mb-0">
                <thead>
                  <tr>
                    <th>Nombre</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  {espacios.length > 0 ? (
                    espacios.map((espacio, index) => (
                      <tr key={espacio.id ?? `${espacio.nombre ?? 'espacio'}-${index}`}>
                        <td>{espacio.nombre ?? espacio.descripcion ?? `Espacio ${index + 1}`}</td>
                        <td>
                          <span className="badge text-bg-success">Disponible</span>
                        </td>
                        <td className="d-flex gap-2">
                          <button type="button" className="btn btn-sm btn-outline-primary">
                            Editar
                          </button>
                          <button type="button" className="btn btn-sm btn-outline-danger">
                            Eliminar
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="3" className="text-center text-secondary py-4">
                        No hay espacios cargados todavía.
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Inventario;