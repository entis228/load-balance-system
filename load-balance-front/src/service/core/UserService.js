import {API_USERS, LOCALSTORAGE_ACCESS_TOKEN} from "../../constants";

export const getCurrentUserInfo = async ()=>await fetch(API_USERS + "/current", {
    method: "GET",
    headers: {
        "Authorization": `Bearer ${localStorage.getItem(LOCALSTORAGE_ACCESS_TOKEN)}`
    }
})