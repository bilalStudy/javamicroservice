import React from 'react';

export function DropdownList({ options, handleSelectChange, selectedValue, labelName }) {
    return (
        <div style={styles.dropdown}>
            <label htmlFor={labelName} style={styles.label}>
                {labelName}:
            </label>
            <select
                value={selectedValue}
                onChange={handleSelectChange}
                style={styles.select}
            >
                <option value="">Select an option</option>
                {options.map((option) => (
                    <option key={option.value} value={option.value} style={styles.option}>
                        {option.label}
                    </option>
                ))}
            </select>
        </div>
    );
}

const styles = {
    dropdown: {
        marginBottom: '15px',
    },
    label: {
        fontSize: '16px',
        fontWeight: 'bold',
        display: 'block',
        marginBottom: '5px',
    },
    select: {
        width: '100%',
        padding: '10px',
        fontSize: '16px',
        border: '1px solid #ccc',
        borderRadius: '5px',
    },
    option: {
        fontSize: '16px',
    },
};