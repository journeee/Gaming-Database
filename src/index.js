import React from "react";
import ReactDOM from "react-dom/client"; // Updated import for React 18
import App from "./App";
import { BrowserRouter } from "react-router-dom";
//import {AuthProvider} from "./api/AuthContext";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
   // <AuthProvider>
    <React.StrictMode>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </React.StrictMode>
   // </AuthProvider>

);
