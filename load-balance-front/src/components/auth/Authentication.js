import React, {useEffect, useState} from "react";
import {Authorization} from "./Authorization";
import {LoginPage} from "./LoginPage";
import {authenticateAndRetrieveUserInfo} from "../../service/auth/AuthOperations";

export const Authentication = () => {
    const [userInfo, setUserInfo] = useState(null);
    const [isAuthenticated, setIsAuthenticated] = useState(false)
    const [errors, setErrors] = useState("");
    const [pageState, setPageState] = useState(null);

    useEffect(()=>{
        authenticateAndRetrieveUserInfo(setIsAuthenticated, setErrors).then(userInfoResult=>{
            setUserInfo(userInfoResult);
        })
        return () =>{
            setUserInfo(null);
        }
    },[setUserInfo, setIsAuthenticated, setErrors]);

    return (
        <div>
            {
                isAuthenticated ? Authorization({
                    userInfo: userInfo,
                    setError: setErrors,
                    setIsAuthenticated:setIsAuthenticated,
                    pageState: pageState,
                    setPageState: setPageState
                }) :
                LoginPage({
                    authFunc: setIsAuthenticated,
                    setError: setErrors
                })
            }
            <div>{errors}</div>
        </div>
    )
}