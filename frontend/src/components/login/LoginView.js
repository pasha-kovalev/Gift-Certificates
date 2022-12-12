import React, {Component} from "react";
import {Alert} from "react-bootstrap";

export default class LoginView extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        const error = this.props.error;
        return (
            <div className="container d-flex justify-content-center">
                <div className="w-50" style={{marginTop: '10%'}}>
                    <Alert show={error} variant='danger'>{error}</Alert>
                    <form className="">
                        <div className="mb-3">
                            <label htmlFor="name" className="col-sm-2 col-form-label text-start">Name</label>
                            <div className="col-sm-10">
                                <input type="text" className="form-control" id="name" placeholder="Name" minLength="3"
                                       maxLength="30" ref={this.props.userNameRef}/>
                            </div>
                        </div>
                        <div className="mb-3">
                            <label htmlFor="password" className="col-sm-2 col-form-label text-start">Password</label>
                            <div className="col-sm-10">
                                <input type="password" className="form-control" id="password" placeholder="Password"
                                       minLength="3"
                                       maxLength="30" ref={this.props.passwordRef}/>
                            </div>
                        </div>
                    </form>
                    <button type="submit" className="btn btn-primary" onClick={this.props.handleSubmit}>Login</button>
                </div>
            </div>

        )
    }

}