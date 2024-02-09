import packageJson from '../package.json';
// Backend
export const API_ROOT = packageJson["backend-url"] + "/api"
export const API_USERS = API_ROOT + "/users";
export const API_TOKEN = API_ROOT + "/token";
export const API_REFRESH_TOKEN = API_TOKEN + "/refresh";
export const API_TASKS = API_ROOT + "/tasks";
export const API_EMOTIONAL_STATES = API_ROOT + "/emotionalStates"

// Roles
export const MANAGER_ROLE_NAME = "ROLE_MANAGER";
export const DEVELOPER_ROLE_NAME = "ROLE_DEVELOPER";

//attributes of local storage
export const LOCALSTORAGE_ACCESS_TOKEN = "accessToken";
export const LOCALSTORAGE_REFRESH_TOKEN = "refreshToken";