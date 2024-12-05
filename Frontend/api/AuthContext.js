import React, { createContext, useState, useEffect } from 'react';
import axios from '../utils/axiosConfig';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchUser = async () => {
            setLoading(true);
            const token = localStorage.getItem('authToken');
            if (token) {
                try {
                    const response = await axios.get('/auth/me');
                    setUser(response.data);
                } catch (error) {
                    console.error('Failed to fetch user:', error);
                    localStorage.removeItem('authToken');
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
        } catch (error) {
            throw new Error('Failed to login.');
        }
    };

    const logout = () => {
        localStorage.removeItem('authToken');
        setUser(null);
    };

    return (
        <AuthContext.Provider value={{ user, login, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};
