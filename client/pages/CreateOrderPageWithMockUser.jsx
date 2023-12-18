import React, { useState } from 'react';

export function OrderForm() {
    const [userId, setUserId] = useState('');
    const [productId, setProductId] = useState('');
    const [productName, setProductName] = useState('');
    const [orderDate, setOrderDate] = useState('');
    const [status, setStatus] = useState('');
    const [productAmount, setProductAmount] = useState('');
    const [totalAmount, setTotalAmount] = useState('');
    // Assume orderItems is an array of objects, each with specific fields
    const [orderItems, setOrderItems] = useState([]);


    const productOptions = [
        { name: 'Product A', id: '1' },
        { name: 'Product B', id: '2' },
        { name: 'Product C', id: '3' },
        // ... other product options
    ];

    const handleProductNameChange = (event) => {
        const selectedProduct = productOptions.find(product => product.name === event.target.value);
        setProductName(event.target.value);
        setProductId(selectedProduct ? selectedProduct.id : '');
    };

    // Function to handle the form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        const success = await insertOrder({
            userId,
            productId,
            productName,
            orderDate,
            status,
            productAmount,
            totalAmount,
            orderItems
        });

        if (success) {
            console.log("Order created successfully");
            // Reset form fields or navigate to another page
        } else {
            console.error("Failed to create order");
        }
    };

    return (
        <div>
            <h2>Create Order</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>User ID:</label>
                    <select value={userId} onChange={(e) => setUserId(e.target.value)}>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                    </select>
                </div>
                {/* Dropdown for productName */}
                <div>
                    <label>Product Name:</label>
                    <select value={productName} onChange={handleProductNameChange}>
                        {productOptions.map(product => (
                            <option key={product.id} value={product.name}>{product.name}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Order Date:</label>
                    <input type="text" value={orderDate} onChange={(e) => setOrderDate(e.target.value)} />
                </div>
                <div>
                    <label>Product Amount:</label>
                    <input type="number" value={productAmount} onChange={(e) => setProductAmount(e.target.value)} />
                </div>
                <div>
                    <label>Total Amount:</label>
                    <input type="number" value={totalAmount} onChange={(e) => setTotalAmount(e.target.value)} />
                </div>
                {/* Add components for managing orderItems here */}
                <button type="submit">Submit Order</button>
            </form>
        </div>
    );
}

// Async function to send the order data to the server
const insertOrder = async (orderData) => {
    try {
        const response = await fetch('/api/orders/save', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(orderData)
        });
        return response.ok;
    } catch (error) {
        console.error('Error submitting order:', error);
        return false;
    }
};