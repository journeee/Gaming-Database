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

document.addEventListener('DOMContentLoaded', () => {
    // Login form logic
    document.getElementById('loginForm').addEventListener('submit', (e) => {
        e.preventDefault();
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Sign in the user with email and password
        signInWithEmailAndPassword(auth, email, password)
            .then((userCredential) => {
                const user = userCredential.user;

                // Redirect based on role after login
                redirectToDashboard(user.uid);
            })
            .catch((error) => {
                document.getElementById('error-message').innerText = error.message;
            });
    });
});
// Redirect based on role after login
function redirectToDashboard(userId) {
    const userRef = doc(db, 'users', userId);
    getDoc(userRef)
        .then((snapshot) => {
            const userData = snapshot.data();
            if (userData) {
                const role = userData.role;
                if (role === 'Administrator') {
                    window.location.href = 'admin_dashboard.html';
                } else if (role === 'Regular User') {
                    window.location.href = 'regularUser_dashboard.html';
                } else if (role === 'Moderator') {
                    window.location.href = 'moderator_dashboard.html';
                } else if (role === 'Content Creator') {
                    window.location.href = 'contentCreatorDashboard.html';
                } else {
                    console.error('Unknown role:', role);
                }
            } else {
                console.error('User data not found.');
            }
        })
        .catch((error) => {
            console.error('Error fetching user data:', error);
        });
}
// Handle the form submission
document.getElementById('uploadForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const file = document.getElementById('file').files[0];
    const uploadTime = firebase.firestore.FieldValue.serverTimestamp();

    try {
        let fileURL = '';

        if (file) {
            // Upload file to Firebase Storage
            const storageRef = storage.ref(`uploads/${file.name}`);
            const snapshot = await storageRef.put(file);
            fileURL = await snapshot.ref.getDownloadURL();
        }

        // Add file metadata and other information to Firestore
        await db.collection('content').add({
            title: title,
            description: description,
            fileURL: fileURL,
            uploadTime: uploadTime,
        });

        // Redirect to the creator dashboard after successful upload
        window.location.href = 'creator_dashboard.html';
    } catch (error) {
        console.error('Error uploading content:', error);
        alert('Failed to upload content. Please try again.');
    }
});

/*// Function to assign admin role
async function assignAdminRole(userId) {
    try {
        // Reference to the user document in Firestore
        const userRef = doc(db, 'users', userId);

        // Set the role in Firestore
        await setDoc(userRef, { role: 'Admin' }, { merge: true });
        console.log('Admin role assigned to user:', userId);
    } catch (error) {
        console.error('Error assigning role:', error);
    }
}*/
async function fetchRecentUploads() {
    const contentSection = document.querySelector('.content-grid');
    contentSection.innerHTML = ''; // Clear the content section

    try {
        const querySnapshot = await db.collection('content')
            .orderBy('uploadTime', 'desc') // Order by upload time, descending
            .limit(5) // Limit to the 5 most recent uploads
            .get();

        querySnapshot.forEach((doc) => {
            const data = doc.data();
            const contentCard = document.createElement('div');
            contentCard.classList.add('content-card');

            contentCard.innerHTML = `
                <h3>${data.title}</h3>
                <p>${data.description}</p>
            `;

            contentSection.appendChild(contentCard);
        });
    } catch (error) {
        console.error('Error fetching recent uploads:', error);
    }
}

// Call the function when the page loads
window.onload = fetchRecentUploads;



