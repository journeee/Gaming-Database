import React from 'react';

const ModeratorDashboard = () => {
    return (
        <div className="dashboard moderator-dashboard">
            <header>
                <h1>Moderator Dashboard</h1>
            </header>
            <div className="dashboard-content">
                <button onClick={() => alert('Flag Inappropriate Content')}>
                    Flag Content
                </button>
                <button onClick={() => alert('Review Content')}>
                    Review Content
                </button>
                <button onClick={() => alert('Remove Content')}>
                    Remove Content
                </button>
            </div>
        </div>
    );
};

export default ModeratorDashboard;
