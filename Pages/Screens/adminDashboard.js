// Import Firebase SDKs and functions
import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.0.2/firebase-app.js';
import { getAuth, signInWithEmailAndPassword } from 'https://www.gstatic.com/firebasejs/9.0.2/firebase-auth.js';
import { getFirestore, doc, getDoc } from 'https://www.gstatic.com/firebasejs/9.0.2/firebase-firestore.js';

// Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCMb_cDXh1FSMVHaw8nTOCkqVvyRK_MVHw",
    authDomain: "gaming-database-2a402.firebaseapp.com",
    projectId: "gaming-database-2a402",
    storageBucket: "gaming-database-2a402.appspot.com",
    messagingSenderId: "739295021317",
    appId: "1:739295021317:web:938fc911c3d36127d4cf6c",
    measurementId: "G-P9PCDRYXM1"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);
let currentUser = null;

// Check if the admin is logged in
auth.onAuthStateChanged(user => {
    if (user) {
        currentUser = user;
        console.log(`Admin logged in as ${user.email}`);
    } else {
        console.log("Admin not logged in");
        window.location.href = "adminSignIn.html"; // Redirect to sign-in page if not logged in
    }
});

// Function to search for users
async function searchUsers() {
    const query = document.getElementById('searchInput').value.toLowerCase();
    const usersRef = db.collection('users');
    const snapshot = await usersRef.get();
    const userListDiv = document.getElementById('userList');
    userListDiv.innerHTML = ''; // Clear the list before displaying

    snapshot.forEach(doc => {
        const userData = doc.data();
        const username = userData.username.toLowerCase();
        const email = userData.email.toLowerCase();

        if (username.includes(query) || email.includes(query)) {
            const userDiv = document.createElement('div');
            userDiv.classList.add('user-card');
            userDiv.innerHTML = `
                <p><strong>Username:</strong> ${userData.username}</p>
                <p><strong>Email:</strong> ${userData.email}</p>
                <button onclick="viewUserDetails('${doc.id}')">View Details</button>
            `;
            userListDiv.appendChild(userDiv);
        }
    });
}

// Function to view user details
async function viewUserDetails(userId) {
    const userRef = db.collection('users').doc(userId);
    const userDoc = await userRef.get();

    if (userDoc.exists) {
        const userData = userDoc.data();
    }}
