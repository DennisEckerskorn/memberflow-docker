import React, { useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/Login.css";

const LoginForm = ({ onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const res = await api.post('/auth/login', { email, password });
      const token = res.data.token;
      localStorage.setItem('token', token);
      console.log("Token guardado:", token);

      const profileRes = await api.get('/users/me');
      const roleName = profileRes.data.roleName;
      console.log("Rol del usuario:", roleName);
      localStorage.setItem('roleName', roleName);

      onLoginSuccess();
    } catch (err) {
      console.error("Error al iniciar sesión:", err);
      setError('Correo electrónico o Contraseña son incorrectos');
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-card">
        <h1>MemberFlow</h1>
        <h2>Accede con tus credenciales</h2>
        <form onSubmit={handleLogin}>
          <input
            type="email"
            placeholder="Correo electrónico"
            value={email}
            autoComplete="email"
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            autoComplete="current-password"
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <button type="submit">Entrar</button>
          {error && <p className="error">{error}</p>}
        </form>
      </div>
    </div>
  );
};

export default LoginForm;
