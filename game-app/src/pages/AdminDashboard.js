import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../Styles/AdminDashboard.css'; // Ensure you have a CSS file for styling

const AdminDashboard = () => {
    const navigate = useNavigate();

    // Navigation handler for dashboard actions
    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="dashboard admin-dashboard">
            <header>
                <h1>Admin Dashboard</h1>
            </header>
            <div className="dashboard-content">
                <button onClick={() => handleNavigation('/api/admin/users')}>
                    Manage Users
                </button>
                <button onClick={() => alert('Generating Reports...')}>
                    Generate Reports
                </button>
                <button onClick={() => alert('Opening System Settings...')}>
                    System Settings
                </button>
            </div>
        </div>
    );
};

export default AdminDashboard;
