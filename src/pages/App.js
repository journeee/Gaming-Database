import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Login from '../pages/Login';
import Register from '../pages/Register';
import AdminDashboard from '../pages/AdminDashboard';
import ModeratorDashboard from '../pages/ModeratorDashboard';
import RegularUserDashboard from '../pages/UserDashboard';
import ContentCreatorDashboard from '../pages/ContentCreatorDashboard';
import ProtectedRoute from '../Components/ProtectedRoute';

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
