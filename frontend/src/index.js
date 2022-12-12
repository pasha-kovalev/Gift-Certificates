import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";
import reportWebVitals from './reportWebVitals';
import App from "./App";
import LoginController from "./components/login/LoginController";
import RouteSecurityWrapper from "./utils/RouteSecurityWrapper";
import {getAdminAuthorities} from "./utils/authUtils";
import NotFound from "./components/core/NotFound";
import Header from "./components/core/Header";
import Footer from "./components/core/Footer";

const root = ReactDOM.createRoot(document.getElementById('root'));


root.render(
    <Router>
        <div className="d-flex flex-column">
            <Header/>
            <main id="main">
                <Switch>
                    <RouteSecurityWrapper path="/certificates" role={getAdminAuthorities()} component={App}/>
                    <Route path="/login" component={LoginController}/>
                    <Route exact path="/">
                        <Redirect from='/' to='/login'/>
                    </Route>
                    <Route component={NotFound}/>
                </Switch>
            </main>
            <Footer/>
        </div>

    </Router>
);

reportWebVitals();
