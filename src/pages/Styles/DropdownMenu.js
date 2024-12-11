// src/components/DropdownMenu.js
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import './DropdownMenu.css';

const DropdownMenu = ({ options }) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = () => {
        setIsOpen(!isOpen);
    };

    return (
        <div className="dropdown-menu">
            <button className="dropdown-button" onClick={toggleDropdown}>
                Menu
            </button>
            {isOpen && (
                <ul className="dropdown-list">
                    {options.map((option) => (
                        <li key={option.path}>
                            <Link to={option.path} onClick={() => setIsOpen(false)}>
                                {option.label}
                            </Link>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default DropdownMenu;
