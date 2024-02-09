import {
    API_REFRESH_TOKEN,
    API_TOKEN,
    LOCALSTORAGE_ACCESS_TOKEN,
    LOCALSTORAGE_REFRESH_TOKEN
} from "../../constants";
import {getCurrentUserInfo} from "../core/UserService";


export const login = async (credentials) => {
    return fetch(API_TOKEN,
        {
            method: 'POST',
            headers: {
                'Content-type': 'application/json',
            },
            body: JSON.stringify(credentials)
        })
}

export const authenticateAndRetrieveUserInfo = async (setAuthentication, setError) => {
    try {
        let accessToken = localStorage.getItem(LOCALSTORAGE_ACCESS_TOKEN);
        if (accessToken === null || accessToken === "") {
            setAuthentication(false);
            return null;
        }
        let response = await getCurrentUserInfo();
        if (!response.ok) {
            let result = await updateAccessToken();
            if (!result) {
                setAuthentication(false);
                return null;
            } else {
                let secondTryResponse = await getCurrentUserInfo();
                if (!secondTryResponse.ok) {
                    setAuthentication(false);
                    return null;
                } else {
                    setAuthentication(true);
                    return secondTryResponse.json();
                }
            }
        } else {
            setAuthentication(true);
            return response.json();
        }
    }catch (error){
        setError(`Виникла серйозна помилка : ${error}`);
    }
}

export const updateAccessToken = async () => {
    try {
        let requestBody = {
            'refreshToken': localStorage.getItem(LOCALSTORAGE_REFRESH_TOKEN)
        }
        const response = await fetch(API_REFRESH_TOKEN,
            {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                },
                body: JSON.stringify(requestBody)
            })
        if (!response.ok) {
            localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN, null)
            localStorage.setItem(LOCALSTORAGE_ACCESS_TOKEN, null)
            return false;
        } else {
            response.json().then((data) => {
                localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN, data.refreshToken)
                localStorage.setItem(LOCALSTORAGE_ACCESS_TOKEN, data.accessToken)
                return true;
            });

        }
    } catch (error) {
        console.log(error)
    }
}

export const logout = (setIsAuthenticated)=>{
    localStorage.setItem(LOCALSTORAGE_ACCESS_TOKEN, null);
    localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN, null);
    setIsAuthenticated(false);
}