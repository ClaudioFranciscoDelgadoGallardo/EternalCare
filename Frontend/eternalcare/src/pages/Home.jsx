import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import SectionHeader from '../components/SectionHeader';
import StatCard from '../components/StatCard';
import { getEspacios } from '../services/inventarioService';

const Home = () => {
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
        eyebrow="Panel principal"
        title="EternalCare"
        description="Sistema de gestión de cementerios con frontend React 18+, Bootstrap y comunicación centralizada al BFF."
      />

      <div className="hero-card card border-0 shadow-sm mb-4">
        <div className="card-body p-4 p-lg-5">
          <div className="row align-items-center g-4">
            <div className="col-lg-7">
              <h2 className="fw-bold mb-3">Control operativo desde un solo lugar</h2>
              <p className="text-secondary mb-4">
                Administra inventario, contratos y programación desde una interfaz clara,
                responsiva y lista para crecer por módulos.
              </p>
              <div className="d-flex flex-wrap gap-2">
                <Link className="btn btn-light btn-lg" to="/inventario">
                  Ver inventario
                </Link>
                <Link className="btn btn-outline-light btn-lg" to="/contratos">
                  Revisar contratos
                </Link>
              </div>
            </div>
            <div className="col-lg-5">
              <div className="hero-badge p-4 rounded-4 text-center">
                <p className="mb-1 text-uppercase small fw-semibold">BFF</p>
                <h3 className="fw-bold mb-0">localhost:8080</h3>
                <p className="mb-0 text-secondary">Axios configurado para esta única entrada</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="row g-3 mb-4">
        <div className="col-md-4">
          <StatCard
            label="Espacios"
            value={loading ? '...' : espacios.length}
            tone="primary"
            detail="Datos obtenidos desde el BFF"
          />
        </div>
        <div className="col-md-4">
          <StatCard label="Inventario" value="Activo" tone="success" detail="Módulo preparado" />
        </div>
        <div className="col-md-4">
          <StatCard
            label="Arquitectura"
            value="Limpia"
            tone="warning"
            detail="api, services, pages, layouts y routes"
          />
        </div>
      </div>

      <div className="card border-0 shadow-sm">
        <div className="card-body p-4">
          <h2 className="h4 fw-bold mb-3">Ruta de trabajo</h2>
          <div className="row g-3">
            <div className="col-lg-4">
              <div className="feature-box h-100 p-3 rounded-4">
                <h3 className="h6 fw-bold mb-2">Inventario</h3>
                <p className="text-secondary mb-0">Consume GET /espacios desde el BFF.</p>
              </div>
            </div>
            <div className="col-lg-4">
              <div className="feature-box h-100 p-3 rounded-4">
                <h3 className="h6 fw-bold mb-2">Contratos</h3>
                <p className="text-secondary mb-0">Base para formularios, tablas y filtros.</p>
              </div>
            </div>
            <div className="col-lg-4">
              <div className="feature-box h-100 p-3 rounded-4">
                <h3 className="h6 fw-bold mb-2">Programación</h3>
                <p className="text-secondary mb-0">Listo para conectarse cuando abras el módulo.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;