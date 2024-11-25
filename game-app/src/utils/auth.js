// utils/auth.js

// Mock function to check if a user is authenticated
export const isAuthenticated = () => {
    const token = localStorage.getItem('authToken'); // Replace with your token logic
    return !!token; // Returns true if token exists
};

// Mock function to get the current user's role
export const getUserRole = () => {
    const role = localStorage.getItem('userRole'); // Replace with your role-fetching logic
    return role;
};