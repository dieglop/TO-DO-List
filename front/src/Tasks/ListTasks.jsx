import { useState, useEffect } from "react";
import './ListTasks.css';
import { API_URL } from "../config";
import Modal from "../components/Modal";

const url = API_URL + "/task/user";
const deleteUrl = API_URL + "/task"

function ListTasks (){

    const [tasks, setTasks] = useState([]);
    const [showModal, setShowModal] = useState(false);

    useEffect(() => {
        
        async function getAPI(){
            try{
                const response = await fetch(url, { 
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"                
                    },
                    credentials: "include"
                });

                if(response.ok){
                    var data = await response.json();
                    setTasks(data);
                }
            } catch (error){
                console.error("Failed to fetch tasks ", error);
            }
        }

        getAPI();
    }, [])

    async function handleDelete(taskId){

        const response = await fetch(deleteUrl + `/${taskId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include"
  
        })

        if(response.ok){
            setTasks((prevTasks) => prevTasks.filter((task) => task.id !== taskId));
        }
    }

    const handleTaskCreated = (newTask) => {
        setTasks((prevTasks) => [...prevTasks, newTask]);
    }

    return (
        <div className="parent">
            <h1 className="title">Tasks List</h1>
            <h2 className="username">{localStorage.getItem("username")}</h2>

            <button className="newTask" onClick={() => setShowModal(true)}>Create new task</button>
            
            {showModal && <Modal 
                                onClose={() => setShowModal(false)}
                                onTaskCreated={handleTaskCreated} />}

            <table className="tasksTable" align="center">
                <thead className="tableHeader">
                    <tr>
                        <th scope="col">Id</th>
                        <th scope="col">Description</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.map((task) => (
                        <tr key={task.id}>
                            <th scope="row">{task.id}</th>
                            <td align="left">{task.description}</td>
                            <td><button className="delete" onClick={() => handleDelete(task.id)}>Delete</button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )

}export default ListTasks;