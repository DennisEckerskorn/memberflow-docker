import React, { useState, useEffect } from 'react';
import SidebarAdmin from './SidebarAdmin';
import SidebarTeacher from './SidebarTeacher';
import SidebarStudent from './SidebarStudent';

const Sidebar = () => {
  const [role, setRole] = useState(null);

  useEffect(() => {
    const interval = setInterval(() => {
      const storedRole = localStorage.getItem('roleName');
      if (storedRole) {
        setRole(storedRole);
        clearInterval(interval);
      }
    }, 100); // intenta cada 100ms

    return () => clearInterval(interval);
  }, []);

  if (!role) {
    return (
      <div className="sidebar">
        <p>Cargando menú...</p>
      </div>
    );
  }

  switch (role) {
    case 'ROLE_ADMIN':
      return <SidebarAdmin />;
    case 'ROLE_TEACHER':
      return <SidebarTeacher />;
    case 'ROLE_STUDENT':
      return <SidebarStudent />;
    default:
      return (
        <div className="sidebar">
          <p>Sin acceso</p>
        </div>
      );
  }
};

export default Sidebar;
