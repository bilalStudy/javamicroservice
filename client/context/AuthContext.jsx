import React, {createContext, useEffect, useState} from 'react';
//import userApi from "./userAPI.jsx";
import {useCookies} from "react-cookie";

// this is just the concept of how i would create login, this is the way i have implemented login
// in my free time when working with the past exam to provide a simpler approach then having all login logic in frontpage


// Create the authentication context
export const AuthContext = createContext();

// Create the AuthContextProvider component
export function AuthContextProvider({ children }) {
    const [user, setUser] = useState(null);
    const [authenticated, setAuthenticated] = useState(null);
    const [cookies, setCookie, removeCookie] = useCookies(["cookie-name"]);



    // Login function
    const login = async (user, cookie, userRole) => {
        setUser(user);
        if (userRole === "manager"){
            setCookie("manager", cookie)
        }else if (userRole === "employee"){
            setCookie("employee", cookie)
        }
        setAuthenticated(true);
        console.log(user)
        console.log(cookies)

    };

    useEffect(() => {
        (async () => {
            if (cookies["employee"] || cookies["manager"]) {
                const user = await userApi.authenticated();
                setUser(user);
            }
        })();
    }, [cookies]);


    // Logout function
    const logout = () => {
        removeCookie("employee");
        removeCookie("manager");
        setUser(null);
        setAuthenticated(false);
        document.location.reload()
    };

    // Provide the authentication state and methods to the components
    return (
        <AuthContext.Provider value={{ user, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
}