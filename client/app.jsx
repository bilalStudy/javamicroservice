import React, {useContext} from "react";
import {HashRouter, Route, Routes, Link} from "react-router-dom";
import {ListCarsPage} from "./pages/ListCarsPage";


export function Application ()  {

    return(

        <HashRouter>
            <main>
                <Routes>
                    <Route path="/" element={<HomePage/>} />
                    <Route path="/cars" element={<ListCarsPage/>} />
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
                <Link to="/register">Register</Link>
            </div>
            <div>
                <Link to="/denied">No Access Component Check</Link>
            </div>
        </div>
    )

}