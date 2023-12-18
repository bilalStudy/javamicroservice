import React, { useState, useEffect } from 'react';
import {productApi} from "../controller/ProductApi.jsx";

export function ListCarsPage() {
    const [cars, setCars] = useState([]);


    useEffect(() => {
        (async () => {
            setCars(await productApi.listAll());
        })();
    }, []);

    return (
        <div>
            <h1>Cars List</h1>
            <ul>
                {cars.map(car => (
                    <li key={car.id}>
                        <h2>{car.carName}</h2>
                        <p>Manufacturer: {car.manufacturer}</p>
                        <p>Color: {car.color}</p>
                        <p>Fuel: {car.fuel}</p>
                        <p>Transmission: {car.transmission}</p>
                        <p>Price: {car.price}</p>
                        <p>Description: {car.description}</p>
                        <p>Available: {car.available ? 'Yes' : 'No'}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}
