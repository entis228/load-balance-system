import {API_EMOTIONAL_STATES, LOCALSTORAGE_ACCESS_TOKEN} from "../../constants";

export const saveForToday = async (mark)=>{
    return await fetch(API_EMOTIONAL_STATES+"/today", {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem(LOCALSTORAGE_ACCESS_TOKEN)}`,
            'Content-type': 'application/json'
        },
        body: mark
    })
}