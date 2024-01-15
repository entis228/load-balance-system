import {API_REFRESH_TOKEN, API_TOKEN, LOCALSTORAGE_ACCESS_TOKEN, LOCALSTORAGE_REFRESH_TOKEN} from "../constants";

class Auth {

    isError = false;
    userData = {
        userName: '',
        roleName: ''
    };
    isManager = false;

    async login(credentials) {
        return fetch(API_TOKEN,
            {
                method: 'POST',
                headers: {
                    'Content-type': 'application/json',
                },
                body: JSON.stringify(credentials)
            })
    }

    async updateAccessToken() {
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
                this.isError = true;
            } else {
                response.json().then((data) => {
                    localStorage.setItem(LOCALSTORAGE_REFRESH_TOKEN, data.refreshToken)
                    localStorage.setItem(LOCALSTORAGE_ACCESS_TOKEN, data.accessToken)
                });
                this.isError = false;
            }
        } catch (error) {
            console.log(error)
        }
    }
}

export default Auth;