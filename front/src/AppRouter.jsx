import { BrowserRouter, Route, Routes } from "react-router-dom";
import Login from "./Login";
import ListTasks from "./Tasks/ListTasks";


function AppRouter(){

    return(
    <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login />} ></Route>
            <Route path="/login" element={<Login />} ></Route>
            <Route path="/listTasks" element={<ListTasks />}></Route>
        </Routes>
    </BrowserRouter>
    );

}export default AppRouter;