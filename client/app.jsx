import React, {useContext} from "react";
import {HashRouter, Route, Routes, Link} from "react-router-dom";
import {ListCarsPage} from "./pages/ListCarsPage";
import {OrderForm} from "./pages/CreateOrderPageWithMockUser";
import {ListOrderForMockUser} from "./pages/ListOrderForMockUser";


export function Application ()  {

    return(

        <HashRouter>
            <main>
                <Routes>
                    <Route path="/" element={<HomePage/>} />
                    <Route path="/cars" element={<ListCarsPage/>} />
                    <Route path="/order/create" element={<OrderForm/>} />
                    <Route path="/order/list/user" element={<ListOrderForMockUser/>} />
                </Routes>
            </main>
        </HashRouter>
    )
}

export function HomePage () {

    //might need for user creation later
    //const { user, logout } = useContext(AuthContext);

    return(
        <div>
            <h1>Hello from client side</h1>
            <div>
                <Link to="/cars">All our Cars</Link>
            </div>
            <div>
                <Link to="/order/create">Create Order For Car</Link>
            </div>
            <div>
                <Link to="/order/list/user">List Orders Based on User</Link>
            </div>
        </div>
    )

}