// Backend
export const BACKEND_URL = "http://localhost:8080";
export const API_ROOT = BACKEND_URL + "/api"
export const API_USERS = API_ROOT + "/users";
export const API_TOKEN = API_ROOT + "/token";
export const API_REFRESH_TOKEN = API_TOKEN + "/refresh";
export const API_TASKS = API_ROOT + "/tasks";
export const API_EMOTIONAL_STATES = API_ROOT + "/emotionalStates"

// Endpoints
export const LOGIN_ENDPOINT = "/login";
export const DEVELOPER_ENDPOINT = "/dev";
export const MANAGER_ENDPOINT = "/manager";
export const REDIRECT_ENDPOINT = "/redirect"

// Roles
export const MANAGER_ROLE_NAME = "ROLE_MANAGER";
export const DEVELOPER_ROLE_NAME = "ROLE_DEVELOPER";

//attributes of local storage
export const LOCALSTORAGE_ACCESS_TOKEN = "accessToken";
export const LOCALSTORAGE_REFRESH_TOKEN = "refreshToken";