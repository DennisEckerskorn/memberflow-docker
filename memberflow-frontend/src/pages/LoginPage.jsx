import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../components/forms/LoginForm';

const LoginPage = () => {
  const navigate = useNavigate();

  return (
    <LoginForm onLoginSuccess={() => {
      console.log("Login exitoso - navegando a /redirect");
      navigate('/redirect');
    }} />
  );
};

export default LoginPage;
