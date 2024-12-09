// App.js
import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import { useAuth } from './pages/AuthContext';
import Login from './pages/Login';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import UserDashboard from './pages/UserDashboard';
import ContentCreatorDashboard from './pages/ContentCreatorDashboard';
import './pages/Styles/App.css';
import DropdownMenu from "./pages/Styles/DropdownMenu";

function App() {
    const { isLoggedIn, userRole } = useAuth();

    const roleBasedOptions = {
        Admin: [
            { label: 'Dashboard', path: '/admin-dashboard' },
            { label: 'Generate Report', path: '/api/admin/report' }, // Matches AdminController
        ],
        User: [
            { label: 'Dashboard', path: '/user-dashboard' },
            { label: 'Bookmarks', path: '/api/users/{userId}/bookmark' }, // Matches UsersController
        ],
        ContentCreator: [
            { label: 'Dashboard', path: '/content-creator-dashboard' },
            { label: 'Manage Content', path: '/api/content/manage' }, // Replace with actual endpoint from your content controller
        ],
    };

    return (
        <Router>
            <div className="app-container">
                {isLoggedIn && (
                    <div className="nav-bar">
                        <DropdownMenu options={roleBasedOptions[userRole] || []} />
                    </div>
                )}
                <div className="main-content">
                    <Routes>
                        {/* Default Route: Redirect to Login */}
                        <Route path="/" element={<Navigate to="/login" replace />} />

                        {/* Authentication */}
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} /> {/* Matches UsersController */}

                        {/* Admin Routes */}
                        <Route path="/admin-dashboard" element={<AdminDashboard />} />
                        <Route path="/api/admin/report" element={<AdminDashboard />} /> {/* Matches AdminController */}

                        {/* User Routes */}
                        <Route path="/user-dashboard" element={<UserDashboard />} />
                        <Route path="/api/users/:userId/bookmark" element={<UserDashboard />} /> {/* Matches UsersController */}

                        {/* Content Creator Routes */}
                        <Route path="/content-creator-dashboard" element={<ContentCreatorDashboard />} />
                    </Routes>
                </div>
            </div>
        </Router>
    );
}

export default App;
