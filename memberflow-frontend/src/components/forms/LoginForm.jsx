import React, { useState } from "react";
import api from "../../api/axiosConfig";
import "../styles/Login.css"

const LoginForm = ({ onLoginSuccess }) => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        try {
            const res = await api.post('/auth/login', { email, password });
            localStorage.setItem('token', res.data.token);
            console.log("Token guardado:", res.data.token);
            onLoginSuccess();
        } catch (err) {
            console.error("Error al iniciar sesión:", err);
            setError('Email o contraseña incorrectos');
        }
    };

    return (
        <div className="login-wrapper">
            <div className="login-card">
                <h1>MemberFlow</h1>
                <form onSubmit={handleLogin}>
                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
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