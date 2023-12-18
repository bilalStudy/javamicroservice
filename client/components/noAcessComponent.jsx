import React from 'react';
import { FaExclamationTriangle } from 'react-icons/fa'; // Example: Using React Icons

const NoAccessComponent = () => {
    return (
        <div style={styles.container}>
            <div style={styles.icon}>
                <FaExclamationTriangle size={50} />
            </div>
            <div style={styles.text}>
                <h1 style={styles.heading}>Access Denied</h1>
                <p style={styles.paragraph}>Sorry, you don't have permission to access this area.</p>
            </div>
        </div>
    );
};

const styles = {
    container: {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        flexDirection: 'column',
        height: '100vh',
    },
    icon: {
        color: 'red', // Customize the color
    },
    text: {
        textAlign: 'center',
    },
    heading: {
        fontSize: '2rem',
        marginBottom: '1rem',
    },
    paragraph: {
        fontSize: '1rem',
    },
};

export default NoAccessComponent;