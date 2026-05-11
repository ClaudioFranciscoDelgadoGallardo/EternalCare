import { Link } from 'react-router-dom';

const NotFound = () => {
  return (
    <div className="page-surface text-center py-5">
      <h1 className="display-5 fw-bold mb-3">404</h1>
      <p className="text-secondary mb-4">La ruta solicitada no existe.</p>
      <Link className="btn btn-primary" to="/">
        Volver al inicio
      </Link>
    </div>
  );
};

export default NotFound;