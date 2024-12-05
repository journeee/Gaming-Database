import React, { useContext } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import AdminDashboard from '../../src/pages/AdminDashboard';
import ModeratorDashboard from '../../src/pages/ModeratorDashboard';
import ContentCreatorDashboard from '../../src/pages/ContentCreatorDashboard';
import UserDashboard from '../../src/pages/UserDashboard';
import Login from '../../src/pages/Login';
import Register from '../../src/pages/Register';
import PrivateRoute from './PrivateRoute';
import { AuthContext } from './AuthContext';

const AppRoutes = () => {
    const { user } = useContext(AuthContext);

    const getDashboardForRole = () => {
        if (!user) return <Navigate to="/login" />;
        switch (user.role) {
            case 'Admin':
                return <Navigate to="/admin-dashboard" />;
            case 'Moderator':
                return <Navigate to="/moderator-dashboard" />;
            case 'ContentCreator':
                return <Navigate to="/content-creator-dashboard" />;
            case 'RegularUser':
                return <Navigate to="/user-dashboard" />;
            default:
                return <Navigate to="/login" />;
        }
    };

    return (
        <Router>
            <Routes>
                {/* Redirect Home to respective dashboards */}
                <Route path="/" element={getDashboardForRole()} />

                {/* Admin Routes */}
                <Route
                    path="/admin-dashboard"
                    element={
                        <PrivateRoute role="Admin">
                            <AdminDashboard />
                        </PrivateRoute>
                    }
                />

                {/* Moderator Routes */}
                <Route
                    path="/moderator-dashboard"
                    element={
                        <PrivateRoute role="Moderator">
                            <ModeratorDashboard />
                        </PrivateRoute>
                    }
                />

                {/* Content Creator Routes */}
                <Route
                    path="/content-creator-dashboard"
                    element={
                        <PrivateRoute role="ContentCreator">
                            <ContentCreatorDashboard />
                        </PrivateRoute>
                    }
                />

                {/* Regular User Routes */}
                <Route
                    path="/user-dashboard"
                    element={
                        <PrivateRoute role="RegularUser">
                            <UserDashboard />
                        </PrivateRoute>
                    }
                />

                {/* Authentication Routes */}
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    );
};

export default AppRoutes;
