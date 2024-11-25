import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../Styles/Login.css';
import axios from 'axios';

const Login = () => {
    const [identifier, setIdentifier] = useState(''); // Identifier (username or email)
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            console.log('Submitting Login Data:', { identifier, password });

            const response = await axios.post('http://localhost:8080/api/auth/signin', {
                identifier,
                password,
            });

            console.log('Login successful:', response.data);
            localStorage.setItem('authToken', response.data.token);
            localStorage.setItem('userRole', response.data.role);
            navigate(getRedirectPath(response.data.role));
        } catch (err) {
            console.error('Login error:', err.response || err);
            setError(
                err.response?.data?.message || 'Login failed. Please check your credentials.'
            );
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className="login-container">
            <h1>Login</h1>
            {error && <p className="error-message">{error}</p>}
            <form onSubmit={handleSubmit} className="login-form">
                <input
                    type="text"
                    placeholder="Username or Email"
                    value={identifier}
                    onChange={(e) => setIdentifier(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
                <button type="submit" className="login-button" disabled={loading}>
                    {loading ? 'Logging in...' : 'Login'}
                </button>
            </form>
            <p>
                Don't have an account? <a href="/register">Register here</a>
            </p>
        </div>
    );
};

export default Login;
