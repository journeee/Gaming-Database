
import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';

// Your Firebase configuration object (replace these fields with your Firebase project details)
        const firebaseConfig ={
        apiKey:"AIzaSyCMb_cDXh1FSMVHaw8nTOCkqVvyRK_MVHw",
        authDomain:"gaming-database-2a402.firebaseapp.com",
        projectId:"gaming-database-2a402",
        storageBucket:"gaming-database-2a402.firebasestorage.app",
        messagingSenderId:"739295021317",
        appId:"1:739295021317:web:a963b6a6792a0cebd4cf6c",
        measurementId:"G-0PGJYVXTB7"
        }

// Initialize Firebase
        const firebaseApp = initializeApp(firebaseConfig);

// Initialize Firebase Auth
        const auth = getAuth(firebaseApp);

        export { firebaseApp, auth };
