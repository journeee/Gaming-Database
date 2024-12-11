import React from 'react';
import { useNavigate } from 'react-router-dom';

const RoleDropdown = ({ role, options }) => {
    const navigate = useNavigate();

    const handleDropdownChange = (event) => {
        const selectedOption = event.target.value;
        if (selectedOption) navigate(selectedOption);
    };

    return (
        <div className="dropdown-navigation">
            <label htmlFor="role-dropdown">Navigate:</label>
            <select id="role-dropdown" onChange={handleDropdownChange}>
                <option value="">Select an Option</option>
                {options.map((option) => (
                    <option key={option.value} value={option.value}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default RoleDropdown;
