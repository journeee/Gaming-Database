// Import the functions you need from the SDKs you need
import { initializeApp } from 'https://www.gstatic.com/firebasejs/9.0.2/firebase-app.js';
import { getAuth, createUserWithEmailAndPassword } from "https://www.gstatic.com/firebasejs/9.0.2/firebase-auth.js";
import { getFirestore, doc, setDoc } from "https://www.gstatic.com/firebasejs/11.0.1/firebase-firestore.js";

// Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyCMb_cDXh1FSMVHaw8nTOCkqVvyRK_MVHw",
    authDomain: "gaming-database-2a402.firebaseapp.com",
    projectId: "gaming-database-2a402",
    storageBucket: "gaming-database-2a402.firebasestorage.app",
    messagingSenderId: "739295021317",
    appId: "1:739295021317:web:a963b6a6792a0cebd4cf6c",
    measurementId: "G-0PGJYVXTB7"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);  // Initialize Firestore

// Submit button
const submit = document.getElementById('submit');
submit.addEventListener("click", function (event) {
    event.preventDefault();

    // Get input values
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const role = document.getElementById('role').value;  // Role input for registration

    // Create user using Firebase Authentication
    createUserWithEmailAndPassword(auth, email, password)
        .then((userCredential) => {
            // User signed up
            const user = userCredential.user;
            alert("Creating Account...");

            // Save user data to Firestore
            setDoc(doc(db, "Users", user.uid), {
                email: email,
                role: role,        // Store the user role
                active: true       // Default to active
            })
                .then(() => {
                    // Redirect user based on role
                    if (role === "Admin") {
                        window.location.href = "adminDashboard.html";
                    } else if (role === "Content Creator") {
                        window.location.href = "contentCreatorDashboard.html";
                    } else if (role === "Moderator") {   // Added moderator role check
                        window.location.href = "moderatorDashboard.html";
                    } else {
                        window.location.href = "regularDashboard.html";
                    }
                })
                .catch((error) => {
                    console.error("Error adding user data to Firestore: ", error);
                });
        })
        .catch((error) => {
            const errorMessage = error.message;
            alert(errorMessage);  // Show error message if signup fails
        });
});
