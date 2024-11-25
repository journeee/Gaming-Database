// firebaseConfig.js
import { initializeApp } from 'firebase/app';
import { getAuth } from 'firebase/auth';


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

const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export default app;
