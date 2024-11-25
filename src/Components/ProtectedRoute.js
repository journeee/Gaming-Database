import React from 'react';
import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
    const userRole = localStorage.getItem('userRole'); // Simulate fetching the user's role

    // Check if the user's role is in the list of allowed roles
    if (!allowedRoles.includes(userRole)) {
        return <Navigate to="/" replace />;
    }

    // If the user's role is allowed, render the children or the requested route
    return <Outlet />;
};

export default ProtectedRoute;
