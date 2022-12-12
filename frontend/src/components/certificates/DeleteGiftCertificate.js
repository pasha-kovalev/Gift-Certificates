import React, {Component} from "react";
import Modal from "react-bootstrap/Modal";
import {Alert} from "react-bootstrap";
import {getAuthHeader} from "../../utils/authUtils";

export default class DeleteGiftCertificate extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.state = {
            modalShow: false,
            error: null
        };
    }

    setModalShow = (visible) => {
        this.setState({modalShow: visible});
    }

    setError = (err) => {
        this.setState({error: err})
    }

    handleSubmit(e) {
        e.preventDefault();
        if (!document.querySelector('form').reportValidity()) {
            return;
        }
        fetch(`http://localhost:8081/gift-certificates/${this.props.certificateId}`,
            {
                method: "DELETE",
                headers: {
                    Authorization: getAuthHeader()
                }
            })
            .then(res => {
                this.setError(null);
                if (!res.ok) {
                    return Promise.reject(res.json())
                }
                this.setModalShow(false);
            })
            .then(this.props.reload)
            .catch(err => {
                console.log("ERR");
                console.log(err)
            });
    }


    render() {
        const {modalShow} = this.state;
        const {error} = this.state
        return (
            <>
                <i className={this.props.className} style={this.props.style}
                   onClick={() => this.setModalShow(true)}></i>
                <Modal
                    show={modalShow}
                    onHide={() => {
                        this.setModalShow(false)
                    }}
                    size="lg"
                    aria-labelledby="contained-modal-title-vcenter"
                    centered
                >
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title-vcenter">
                            Delete Confirmation</Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Alert show={error} variant='danger'>{error}</Alert>
                        Do you really want to delete certificate with id = {this.props.certificateId}
                    </Modal.Body>
                    <Modal.Footer>
                        <button type="submit" className="btn btn-primary" onClick={this.handleSubmit}>Yes</button>
                        <button type="button" className="btn btn-secondary"
                                onClick={() => this.setModalShow(false)}>Cancel
                        </button>
                    </Modal.Footer>

                </Modal>
            </>
        );
    }
};
