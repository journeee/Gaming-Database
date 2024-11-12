// Sample data for users and content creators
let users = [
    { id: 1, username: 'john_doe', followedContentCreators: [] },
    { id: 2, username: 'jane_smith', followedContentCreators: [] }
  ];
  
  let contentCreators = [
    { id: 1, username: 'techguru', followers: [] },
    { id: 2, username: 'gamingmaster', followers: [] }
  ];
  
  // Function to follow a content creator
  function followContentCreator(userId, creatorId) {
    const user = users.find(u => u.id === userId);
    const creator = contentCreators.find(c => c.id === creatorId);
    
    if (!user || !creator) {
      console.log("User or Content Creator not found.");
      return;
    }
    
    // Add creator to user's followed list if not already followed
    if (!user.followedContentCreators.includes(creatorId)) {
      user.followedContentCreators.push(creatorId);
      console.log(`${user.username} followed ${creator.username}`);
    } else {
      console.log(`${user.username} is already following ${creator.username}`);
    }
    
    // Add user to creator's followers list if not already a follower
    if (!creator.followers.includes(userId)) {
      creator.followers.push(userId);
      console.log(`${creator.username} has a new follower: ${user.username}`);
    }
  }
  
  // Function to unfollow a content creator
  function unfollowContentCreator(userId, creatorId) {
    const user = users.find(u => u.id === userId);
    const creator = contentCreators.find(c => c.id === creatorId);
    
    if (!user || !creator) {
      console.log("User or Content Creator not found.");
      return;
    }
    
    // Remove creator from user's followed list
    const index = user.followedContentCreators.indexOf(creatorId);
    if (index !== -1) {
      user.followedContentCreators.splice(index, 1);
      console.log(`${user.username} unfollowed ${creator.username}`);
    } else {
      console.log(`${user.username} is not following ${creator.username}`);
    }
    
    // Remove user from creator's followers list
    const followerIndex = creator.followers.indexOf(userId);
    if (followerIndex !== -1) {
      creator.followers.splice(followerIndex, 1);
      console.log(`${creator.username} lost a follower: ${user.username}`);
    }
  }
  
  // Function to list all content creators a user is following
  function getFollowing(userId) {
    const user = users.find(u => u.id === userId);
    if (!user) {
      console.log("User not found.");
      return;
    }
    
    const following = user.followedContentCreators.map(creatorId => {
      const creator = contentCreators.find(c => c.id === creatorId);
      return creator ? creator.username : null;
    }).filter(Boolean);
    
    console.log(`${user.username} is following: ${following.join(", ") || "No one"}`);
  }
  
  // Function to list all followers of a content creator
  function getFollowers(creatorId) {
    const creator = contentCreators.find(c => c.id === creatorId);
    if (!creator) {
      console.log("Content Creator not found.");
      return;
    }
    
    const followers = creator.followers.map(userId => {
      const user = users.find(u => u.id === userId);
      return user ? user.username : null;
    }).filter(Boolean);
    
    console.log(`${creator.username} has followers: ${followers.join(", ") || "No followers"}`);
  }
  
  // Example Usage
  
  followContentCreator(1, 1); // john_doe follows techguru
  followContentCreator(2, 2); // jane_smith follows gamingmaster
  
  getFollowing(1); // john_doe's following list
  getFollowers(1); // techguru's followers
  
  unfollowContentCreator(1, 1); // john_doe unfollows techguru
  getFollowing(1); // john_doe's updated following list
  