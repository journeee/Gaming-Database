import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from '../context/AuthContext';
import SignIn from '../components/Auth/SignIn';
import Register from '../components/Auth/Register';
import AdminDashboard from '../components/Admin/AdminDashboard';
import UserDashboard from '../components/User/UserDashboard';
import CreatorDashboard from '../components/ContentCreator/CreatorDashboard';
import ModeratorDashboard from '../components/Moderator/ModeratorDashboard';
import PrivateRoute from './PrivateRoute';

function AppRoutes() {
    return (
        <Router>
            <AuthProvider>
                <Routes>
                    <Route path="/" element={<SignIn />} />
                    <Route path="/register" element={<Register />} />
                    <Route
                        path="/admin"
                        element={
                            <PrivateRoute role="Admin">
                                <AdminDashboard />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/user"
                        element={
                            <PrivateRoute role="User">
                                <UserDashboard />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/creator"
                        element={
                            <PrivateRoute role="ContentCreator">
                                <CreatorDashboard />
                            </PrivateRoute>
                        }
                    />
                    <Route
                        path="/moderator"
                        element={
                            <PrivateRoute role="Moderator">
                                <ModeratorDashboard />
                            </PrivateRoute>
                        }
                    />
                </Routes>
            </AuthProvider>
        </Router>
    );
}

export default AppRoutes;
