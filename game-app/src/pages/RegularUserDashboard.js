import React from 'react';

const RegularUserDashboard = () => {
    const handleBrowseGames = () => {
        alert('Browse and explore available games.');
        // Add navigation or logic for browsing games
    };

    const handleBookmarkContent = () => {
        alert('Bookmark your favorite content.');
        // Add navigation or logic for bookmarking content
    };

    const handleFollowCreators = () => {
        alert('Follow and engage with your favorite content creators.');
        // Add navigation or logic for following creators
    };

    return (
        <div className="dashboard regular-user-dashboard">
            <header>
                <h1>User Dashboard</h1>
            </header>
            <div className="dashboard-content">
                <button onClick={handleBrowseGames}>
                    Browse Games
                </button>
                <button onClick={handleBookmarkContent}>
                    Bookmark Content
                </button>
                <button onClick={handleFollowCreators}>
                    Follow Creators
                </button>
            </div>
        </div>
    );
};

export default RegularUserDashboard;
