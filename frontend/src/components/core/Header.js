import React, {useEffect, useState} from 'react';
import {clearAuthData, getUser} from "../../utils/authUtils";
import {useHistory} from "react-router-dom";

const Header = () => {
    const history = useHistory();
    const [userData, setUserData] = useState(getUser());

    useEffect(() => {
        const updateUserData = () => {
            console.log(getUser())
            setUserData(getUser());
        }

        window.addEventListener('storage', updateUserData);

        return () => {
            window.removeEventListener("storage", updateUserData);
        }
    }, []);


    function handleLogout() {
        clearAuthData();
        history.push("/");
    }

    const div = <div className="d-flex">
        <span className="pe-3">{userData ? userData.user_name : ""}</span>
        <a href="#" onClick={handleLogout} className="link-dark">Log Out</a>
    </div>;

    return (

        <nav className="navbar bg-light">
            <div className="container-fluid">
                <span className="navbar-brand mb-0 h1">Admin UI</span>
                {userData
                    ? div
                    : ""}
            </div>
        </nav>
    );
}

export default Header;
