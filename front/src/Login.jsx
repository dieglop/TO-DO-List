import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Login.css";
import { API_URL } from "./config";

const url = API_URL + "/login"; 


function Login(){

    const [username, setUsername] = useState();
    const [password, setPassword] = useState();

    const navigate = useNavigate();

    const handleLogin = async (e) => {
         
        e.preventDefault();

        try{
            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"                
                },
                credentials: "include",

                body: JSON.stringify({
                    username: username,
                    password: password
                })
            })

            if(response.ok){
                const data = await response.json();

                localStorage.setItem("username", data.username);
            }

            navigate("/listTasks");

        } catch (error){
            console.error("Login error", error);
        }
    }
       
return (
    <div className="displayArea">
            <h1 className="pageTitle">Access your TO-DO List</h1>
        <div className="loginForm">
            <h2>Sign in</h2>
            <form onSubmit={handleLogin}>
                <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)}/>
                <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                <button className="signin" type="submit">Sign in</button>
            </form>
        </div>
    </div>
)

} export default Login;