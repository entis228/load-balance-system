import "./Menu.css"

export const Menu = (props)=>{
    return (
        <div>
            <button onClick={()=>mainMenuClick()} className={"dropbtn"}>{"="}</button>
            <div id="myDropdown">
                {props.items.map(task=>(
                    <button key={task.name} onClick={()=>task.func(task.args)}>{task.name}</button>
                ))}
            </div>
        </div>
    );
}

const mainMenuClick = ()=>{
    console.log("main menu clicked")
}