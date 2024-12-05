import React, { useState, useEffect } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { getAuth, onAuthStateChanged } from 'firebase/auth';
import { firebaseApp } from './firebaseConfig'; // Correct import for Firebase configuration
import Login from './pages/Login';
import UserDashboard from './pages/UserDashboard';
import AdminDashboard from './pages/AdminDashboard';
import ModeratorDashboard from './pages/ModeratorDashboard';
import ContentCreatorDashboard from './pages/ContentCreatorDashboard';

const App = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const auth = getAuth(firebaseApp);
        const unsubscribe = onAuthStateChanged(auth, (currentUser) => {
            if (currentUser) {
                setUser(currentUser);
            } else {
                setUser(null);
            }
            setLoading(false);
        });

        return () => unsubscribe();
    }, []);

    if (loading) {
        return <div>Loading...</div>;
    }

    const getDashboardForRole = () => {
        if (!user) return <Navigate to="/login" />;
        // Assuming you have some mechanism to differentiate the user role
        const userRole = user.role || "User"; // Example placeholder for role checking
        switch (userRole) {
            case 'Admin':
                return <Navigate to="/admin-dashboard" />;
            case 'Moderator':
                return <Navigate to="/moderator-dashboard" />;
            case 'ContentCreator':
                return <Navigate to="/content-creator-dashboard" />;
            default:
                return <Navigate to="/user-dashboard" />;
        }
    };

    return (
        <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/user-dashboard" element={user ? <UserDashboard /> : <Navigate to="/login" />} />
            <Route path="/admin-dashboard" element={user ? <AdminDashboard /> : <Navigate to="/login" />} />
            <Route path="/moderator-dashboard" element={user ? <ModeratorDashboard /> : <Navigate to="/login" />} />
            <Route path="/content-creator-dashboard" element={user ? <ContentCreatorDashboard /> : <Navigate to="/login" />} />
            <Route path="/" element={getDashboardForRole()} />
        </Routes>
    );
};

export default App;
