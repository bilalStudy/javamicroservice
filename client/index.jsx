import React, {useState} from "react"
import { createRoot } from "react-dom/client.js";
import {Application} from "./app.jsx";




const element = document.getElementById("app");
const root = createRoot(element);

root.render(<Application />)