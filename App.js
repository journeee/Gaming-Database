import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from './src/pages/Login';
import Register from './src/pages/Register';
import AdminDashboard from './src/pages/AdminDashboard';
import ModeratorDashboard from './src/pages/ModeratorDashboard';
import RegularUserDashboard from './src/pages/RegularUserDashboard';
import ContentCreatorDashboard from './src/pages/ContentCreatorDashboard';
import ProtectedRoute from './src/Components/ProtectedRoute';

const App = () => {
    return (
        <BrowserRouter>
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<Login />} />
                <Route path="/register" element={<Register />} />

                {/* Protected Routes */}
                <Route path="/admin" element={<ProtectedRoute role="Admin" />}>
                    <Route path="" element={<AdminDashboard />} />
                </Route>
                <Route path="/moderator" element={<ProtectedRoute role="Moderator" />}>
                    <Route path="" element={<ModeratorDashboard />} />
                </Route>
                <Route path="/regular-user" element={<ProtectedRoute role="Regular User" />}>
                    <Route path="" element={<RegularUserDashboard />} />
                </Route>
                <Route path="/content-creator" element={<ProtectedRoute role="Content Creator" />}>
                    <Route path="" element={<ContentCreatorDashboard />} />
                </Route>

                {/* Optional: Not Authorized Page */}
                <Route path="/not-authorized" element={<div>Not Authorized</div>} />
            </Routes>
        </BrowserRouter>
    );
};

export default App;
