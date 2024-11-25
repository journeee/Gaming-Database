import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import '../Styles/Register.css';
import axios from 'axios';

const Register = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        role: 'RegularUser', // Default role
    });
    const [error, setError] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const navigate = useNavigate();

    // Handle form input changes
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccessMessage('');

        if (!formData.password) {
            setError('Password is required.');
            return;
        }

        try {
            console.log('Submitting Registration Data:', formData);

            await axios.post('http://localhost:8080/api/auth/register', formData);

            setSuccessMessage('Registration successful! Redirecting to login...');
            setTimeout(() => navigate('/'), 3000);
        } catch (err) {
            console.error('Registration error:', err.response || err);
            setError(err.response?.data?.message || 'Registration failed. Please try again.');
        }
    };


    return (
        <div className="register-container">
            <h1>Register</h1>
            {error && <p className="error-message">{error}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            <form onSubmit={handleSubmit} className="register-form">
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                />
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    required
                />
                <select
                    name="role"
                    value={formData.role}
                    onChange={handleChange}
                    className="role-select"
                >
                    <option value="RegularUser">Regular User</option>
                    <option value="ContentCreator">Content Creator</option>
                    <option value="Moderator">Moderator</option>
                </select>
                <button type="submit" className="register-button">
                    Register
                </button>
            </form>
            <p>
                Already have an account? <Link to="/">Login here</Link>
            </p>
        </div>
    );
};

export default Register;
