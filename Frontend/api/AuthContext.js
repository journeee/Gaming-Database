import React, { createContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from './axiosConfig';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchUser = async () => {
            const token = localStorage.getItem('authToken');
            if (token) {
                try {
                    const response = await axios.get('/auth/me');
                    setUser(response.data);
                } catch (err) {
                    console.error('Error fetching user data:', err);
                    localStorage.removeItem('authToken');
                    setUser(null);
                }
            }
            setLoading(false);
        };
        fetchUser();
    }, []);

    const login = async (credentials) => {
        try {
            const response = await axios.post('/auth/signin', credentials);
            localStorage.setItem('authToken', response.data.token);
            setUser(response.data.user);
            navigate(`/${response.data.user.role.toLowerCase()}`);
        } catch (err) {
            throw new Error('Login failed');
        }
    };

    const logout = () => {
        localStorage.removeItem('authToken');
        setUser(null);
        navigate('/');
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
