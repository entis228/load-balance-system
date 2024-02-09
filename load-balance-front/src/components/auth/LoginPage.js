import {login} from "../../service/auth/AuthOperations";
import React from "react";
import {LOCALSTORAGE_ACCESS_TOKEN, LOCALSTORAGE_REFRESH_TOKEN} from "../../constants";

export const LoginPage = (props) => {

    return (
        <div>
            <h2>Система розподілу навантаження</h2>
            <p>Логін <input type="text" placeholder={"Уведіть логін"} id={"login"}></input></p>
            <p>Пароль<input type="password" placeholder={"Уведіть пароль"} id={"password"}></input></p>
            <input type={"button"} onClick={()=> checkInput(props.setError, props.authFunc)} value={"Увійти"}/>
        </div>
    );
}

const checkInput = (setErrors, authFunc)=>{
    let userPassword = document.getElementById("password").value;
    let userLogin = document.getElementById("login").value;

    login({username:userLogin, password: userPassword})
        .then(
            (response)=>{
        if(!response.ok){
            setErrors("Неправильний логін або пароль")
        }else {
            response.json().then((data)=>{
                localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN, data.refreshToken)
                localStorage.setItem(LOCALSTORAGE_ACCESS_TOKEN, data.accessToken)
                setErrors("");
                authFunc(true);
            })
        }
    }).catch((exception)=>{
        console.log(exception);
        setErrors('Виникла серйозна помилка, деталі у консолі')
    });
}