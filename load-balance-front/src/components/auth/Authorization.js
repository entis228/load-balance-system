import {DEVELOPER_ROLE_NAME, MANAGER_ROLE_NAME} from "../../constants";
import {ManagerPage} from "../manager/ManagerPage";
import {DeveloperPage} from "../developer/DeveloperPage";

export const Authorization = (props) => {
    if(props.userInfo === null){
        return (
            <div>
                <p>Loading...</p>
            </div>
        )
    }
    if (props.userInfo.userRole === MANAGER_ROLE_NAME) {
        return <ManagerPage/>;
    } else {
        if (props.userInfo.userRole === DEVELOPER_ROLE_NAME) {
            return <DeveloperPage
                authenticatedFunction={props.setIsAuthenticated}
                state={props.pageState}
                setState={props.setPageState}
                setError={props.setError}
            />;
        } else {
            props.setError(`Невідома роль на сервері "${props.userInfo.userRole}"`);
        }
    }
}