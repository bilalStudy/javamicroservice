import React, {useEffect, useState} from 'react';
import {productApi} from "../controller/ProductApi.jsx";
import {userApi} from "../controller/UserApi.jsx";
import {orderApi} from "../controller/OrderApi.jsx";

export function OrderForm() {
    const [userId, setUserId] = useState(1);
    const [productId, setProductId] = useState(0);
    const [productName, setProductName] = useState('');
    const [orderDate, setOrderDate] = useState('12.12.12');
    const [productAmount, setProductAmount] = useState(0);
    const [totalAmount, setTotalAmount] = useState(0);
    const [orderStatus, setOrderStatus] = useState('')
    // OrderItem implementation will probably come later, first we worry about buying our car
    // Assume orderItems is an array of objects, each with specific fields
    const [status, setStatus] = useState('');
    const [users, setUsers] = useState([])
    const [cars, setCars] = useState([]);


    useEffect(() => {
        (async () => {
            const fetchedCars = await productApi.listAll();
            setCars(fetchedCars);
            if (fetchedCars.length > 0) {
                // Optionally, set a default car
                const defaultCar = fetchedCars[0];
                setProductId(defaultCar.id);
                setProductName(defaultCar.carName);
                setProductAmount(defaultCar.price);
                setTotalAmount(defaultCar.price);
            }
            setUsers(await userApi.listAll());
            setStatus("Pending")
        })();
    }, []);

    const handleProductChange = (event) => {
        const selectedCar = cars.find(car => car.id.toString() === event.target.value);
        if (selectedCar) {
            setProductId(selectedCar.id);
            setProductName(selectedCar.carName);
            setProductAmount(selectedCar.price); // Assuming price is per unit
        }
    };

    const handleUserChange = (event) => {
        setUserId(event.target.value);
    };

    // Function to handle the form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        console.log(userId,productId,productName,orderDate,status,productAmount,totalAmount)
        const success = await orderApi.insert({
            userId,
            productId,
            productName,
            orderDate,
            status,
            productAmount,
            totalAmount,
        })

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
                    <select value={userId} onChange={handleUserChange}>
                        {users.map(user => (
                            <option key={user.id} value={user.id}>{user.name}</option> // Assuming users have a username field
                        ))}
                    </select>
                </div>
                {/* Dropdown for Cars */}
                <div>
                    <label>Car:</label>
                    <select value={productId} onChange={handleProductChange}>
                        {cars.map(car => (
                            <option key={car.id} value={car.id}>{car.carName}</option>
                        ))}
                    </select>
                </div>
                <div>
                    <label>Order Date:</label>
                    <input type="text" value={orderDate} onChange={(e) => setOrderDate(e.target.value)} />
                </div>
                <div>
                    <label>Total Amount:</label>
                    <p>{productAmount}</p> {/* Display the product amount here */}
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