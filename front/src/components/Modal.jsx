import { useState, useRef } from "react";
import { API_URL } from "../config";
import './Modal.css'

function Modal({onClose, onTaskCreated}){

    const [description, setDescription] = useState("");
    const modalRef = useRef();

    const closeModal = (e) => {

        if(modalRef.current === e.target){
            onClose();
        }
    }
   
    async function handleAddition(){

        const response = await fetch(API_URL + '/task', {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",

            body: JSON.stringify({
                description: description
            })
        })

        if (response.ok){

            const newTask = await response.json();

            if(onTaskCreated){
                onTaskCreated(newTask);
            }

            onClose();
        }
    } 


    return (
        <div ref={modalRef} onClick={closeModal} className="modalBackground">
            <div className="modal">
                <button onClick={onClose}>X</button>     
                <h2 className="modalHeader">Creating a new task</h2>   
                <div className="modalElements">
                    <textarea className="inputModal" placeholder="Insert the new task" value={description} onChange={(e) => setDescription(e.target.value)}/>
                    <button type="button" onClick={handleAddition} className="createTask">Create Task</button>   
                </div>    
            </div>
        </div>
    )
    

}export default Modal;