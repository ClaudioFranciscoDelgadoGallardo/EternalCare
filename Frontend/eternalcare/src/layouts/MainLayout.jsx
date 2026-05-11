import { Outlet } from 'react-router-dom';
import AppFooter from '../components/AppFooter';
import AppNavbar from '../components/AppNavbar';

const MainLayout = () => {
  return (
    <div className="app-shell d-flex flex-column min-vh-100">
      <AppNavbar />
      <main className="flex-grow-1 py-4 py-lg-5">
        <div className="container">
          <Outlet />
        </div>
      </main>
      <AppFooter />
    </div>
  );
};

export default MainLayout;