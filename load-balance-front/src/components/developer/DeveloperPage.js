import {Menu} from "../common/Menu";
import {TaskList} from "./TaskList";
import {logout} from "../../service/auth/AuthOperations";
import {saveForToday} from "../../service/core/EmotionalStateService";

export const DeveloperPage = (props) => {
    if (props.state === null) {
        return (<div>
            <Menu
                items={
                    [
                        {name: "Logout", func: logout, args: props.authenticatedFunction}
                    ]
                }
            />
            <button onClick={() => {
                props.setState("emotionalState")
            }}>{"Емоційний стан на сьогодні"}</button>
            <button onClick={() => {
                endCurrentTask()
            }}>{"Закінчити поточне завдання"}</button>
            <button onClick={() => {
                props.setState("newTask")
            }}>{"Взяти нове завдання"}</button>
            <TaskList/>
        </div>);
    } else {
        if (props.state === "emotionalState") {
            return (
                <div>
                    <h2>Уведіть оцінку свого настрою</h2>
                    <input type={"number"} id={"markResult"} max={10} min={0}></input>
                    <button onClick={() => {
                        emotionalStateMarkSubmit(props.setState, props.setError, props.authenticatedFunction)
                    }}>{"Відправити"}</button>
                </div>
            )
        }else {
            if(props.state === "newTask") {
                return(
                    <div>
                        <h2>Нове завдання</h2>
                        <button onClick={()=>submitNewTaskOrReturn(props.setState, props.setError, props.authenticatedFunction)}>{"На головну"}</button>
                    </div>
                )
            } else {
                props.setError(`Невідомий стан сторінки: ${props.state}`);
            }
        }
    }
}

const endCurrentTask = () => {
    alert("Поточне завдання завершено");
}

const emotionalStateMarkSubmit = (setState, setError, setAuthenticated) => {
    const mark = document.getElementById("markResult").value;
    saveForToday(mark).then((response) => {
        if (!response.ok) {
            setError("Користувач не має права на цю операцію");
            logout(setAuthenticated);
        }
    });
    setState(null);
}

const submitNewTaskOrReturn = (setState, setError, setAuthenticated)=>{

    setState(null);
}