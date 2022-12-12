import React from "react";
import {Redirect, Route} from 'react-router-dom';
import {getToken, hasAuthority} from "./authUtils";
import NotFound from "../components/core/NotFound";

const RouteSecurityWrapper = ({component: Component, role, ...routeProps}) => {
    return (
        <Route {...routeProps}
               render={props => (
                   !!getToken()
                       ? hasAuthority(role) ? <Component {...props} /> : <NotFound/>
                       : <Redirect to={{pathname: '/login'}}/>
               )}
        />
    );
};
export default RouteSecurityWrapper;