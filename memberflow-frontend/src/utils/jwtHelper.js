import { jwtDecode } from 'jwt-decode';

export const getRoleFromToken = () => {
  const token = localStorage.getItem('token');
  if (!token) return null;

  try {
    const decoded = jwtDecode(token);
    console.log("Token decodificado:", decoded);
    return decoded?.role || null; 
  } catch (error) {
    console.error('Error decoding token:', error);
    return null;
  }
};
