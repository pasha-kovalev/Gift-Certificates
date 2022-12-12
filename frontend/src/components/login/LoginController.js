import React, {Component} from "react";
import LoginView from "./LoginView";
import jwt from 'jwt-decode'
import {getUser, setToken, setUser} from "../../utils/authUtils";

export default class LoginController extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.userNameInput = React.createRef();
        this.passwordInput = React.createRef();
        this.state = {
            error: null
        };
    }

    setError = (err) => {
        this.setState({error: err})
    }

    componentDidMount() {
        if (getUser()) {
            this.props.history.push('/certificates');
        }
    }

    handleSubmit(e) {
        e.preventDefault();
        if (!document.querySelector('form').reportValidity()) {
            return;
        }
        let details = {
            scope: 'write',
            grant_type: 'password',
            username: this.userNameInput.current.value,
            password: this.passwordInput.current.value
        }
        fetch('http://localhost:9002/oauth/token',
            {
                method: "POST",
                headers: {
                    'Authorization': process.env.REACT_APP_AUTH_TOKEN,
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams(details)
            })
            .then(res => {
                this.setError(null);
                this.userNameInput.current.value = '';
                this.passwordInput.current.value = '';
                if (!res.ok) {
                    return Promise.reject(res.json())
                }
                return res.json()
            })
            .then(data => {
                const user = jwt(data.access_token);
                if (!user.authorities.includes('ROLE_ADMIN')) {
                    this.setError('Access forbidden');
                    return;
                }
                setToken(data.access_token);
                setUser(user);
                this.props.history.push("/certificates");
            })
            .catch(err => {
                console.log("ERR");
                err.then((json) => {
                    this.setError(json.error_description);
                })
            });
    }

    render() {
        return (
            <LoginView error={this.state.error} userNameRef={this.userNameInput} passwordRef={this.passwordInput}
                       handleSubmit={this.handleSubmit}/>
        )
    }

}