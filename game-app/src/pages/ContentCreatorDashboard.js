import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../Styles/ContentCreatorDashboard.css'; // Ensure you have a CSS file for styling

const ContentCreatorDashboard = () => {
    const navigate = useNavigate();

    // Navigation handler for dashboard actions
    const handleAction = (action) => {
        switch (action) {
            case 'upload':
                navigate('/content-creator/upload');
                break;
            case 'metrics':
                navigate('/content-creator/metrics');
                break;
            case 'manage-premium':
                navigate('/content-creator/premium');
                break;
            default:
                alert('Action not recognized');
        }
    };

    return (
        <div className="dashboard content-creator-dashboard">
            <header>
                <h1>Content Creator Dashboard</h1>
            </header>
            <div className="dashboard-content">
                <button onClick={() => handleAction('upload')}>
                    Upload Content
                </button>
                <button onClick={() => handleAction('metrics')}>
                    Subscriber Metrics
                </button>
                <button onClick={() => handleAction('manage-premium')}>
                    Manage Premium Content
                </button>
            </div>
        </div>
    );
};

export default ContentCreatorDashboard;
