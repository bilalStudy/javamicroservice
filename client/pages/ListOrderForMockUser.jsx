import React, { useState, useEffect } from 'react';
import {userApi} from "../controller/UserApi.jsx";
import {orderApi} from "../controller/OrderApi.jsx";

export function ListOrderForMockUser() {
    const [users, setUsers] = useState([]);
    const [selectedUserId, setSelectedUserId] = useState('');
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        (async () => {
            const fetchedUsers = await userApi.listAll();
            if (fetchedUsers.length > 0) {
                setSelectedUserId(fetchedUsers[0].id); // Set the first user as default
            }
            setUsers(fetchedUsers);
        })();
    }, []);

    useEffect(() => {
        if (selectedUserId) {
            (async () => {
                const fetchedOrders = await orderApi.listBasedOnUser(selectedUserId);
                setOrders(fetchedOrders);
            })();
        }
    }, [selectedUserId]);

    const handleUserChange = (event) => {
        setSelectedUserId(event.target.value);
    };

    return (
        <div>
            <h2>User Orders</h2>
            <div>
                <label>Select User:</label>
                <select value={selectedUserId} onChange={handleUserChange}>
                    <option value="">Select a user</option>
                    {users.map(user => (
                        <option key={user.id} value={user.id}>{user.name}</option>
                    ))}
                </select>
            </div>
            <div>
                <h3>Orders for User ID: {selectedUserId}</h3>
                <ul>
                    {orders.map(order => (
                        <li key={order.id}>
                            <div>
                                <h4>Order ID: {order.id}</h4>
                                <p>Product Name: {order.productName}</p>
                                <p>Order Date: {order.orderDate}</p>
                                <p>Status: {order.status}</p>
                                {/* Display other order details as needed */}
                            </div>
                        </li>
                    ))}
                </ul>
            </div>
        </div>
    );
}
