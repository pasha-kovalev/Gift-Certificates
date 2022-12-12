import React, {Component} from "react";
import Modal from "react-bootstrap/Modal";
import {Alert} from "react-bootstrap";
import TagsInput from "./TagsInput";
import {getAuthHeader} from "../../utils/authUtils";

export default class EditGiftCertificate extends Component {
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.nameInput = React.createRef();
        this.descriptionInput = React.createRef();
        this.priceInput = React.createRef();
        this.durationInput = React.createRef();
        this.tagsInput = React.createRef();
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
        let certificate = {
            duration: this.durationInput.current.value,
            tags: [].map
                .call(document.querySelectorAll('.tag-name'), e => ({"name": e.innerHTML}))
        }
        fetch(`http://localhost:8081/gift-certificates/${this.props.certificate.id}`,
            {
                method: "PATCH",
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: getAuthHeader()
                },
                body: JSON.stringify(certificate)
            })
            .then(res => {
                this.setError(null);
                if (!res.ok) {
                    return Promise.reject(res.json())
                }
                return res.json()
            })
            .then(() => {
                this.setModalShow(false);
            })
            .then(this.props.reload)
            .catch(err => {
                console.log("ERR");
                err.then((json) => {
                    this.setError(json.message);
                })
            });
    }

    render() {
        const {modalShow} = this.state;
        const {error} = this.state
        const cert = this.props.certificate
        let tags = [];
        cert.tags.forEach(tag => tags.push(tag.name));
        return (
            <>
                <i className={this.props.className} style={this.props.style}
                   onClick={() => this.setModalShow(true)}></i>

                <Modal
                    show={modalShow}
                    onHide={() => {
                        this.setModalShow(false);
                        this.setError(null)
                    }}
                    size="lg"
                    aria-labelledby="contained-modal-title-vcenter"
                    centered
                >
                    <Modal.Header closeButton>
                        <Modal.Title id="contained-modal-title-vcenter">
                            Update certificate </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Alert show={error} variant='danger'>{error}</Alert>
                        <form>
                            <div className="form-group row py-2">
                                <label htmlFor="name" className="col-sm-2 col-form-label text-start">Name</label>
                                <div className="col-sm-10">
                                    <input type="text" className="form-control-plaintext" id="name" minLength="6"
                                           maxLength="30" ref={this.nameInput} value={cert.name} readOnly={true}/>
                                </div>
                            </div>
                            <div className="form-group row py-2">
                                <label htmlFor="description"
                                       className="col-sm-2 col-form-label text-start">Description</label>
                                <div className="col-sm-10">
                                    <input type="text" className="form-control-plaintext" id="description"
                                           minLength="12"
                                           maxLength="1000" ref={this.descriptionInput} value={cert.description}
                                           readOnly={true}/>
                                </div>
                            </div>
                            <div className="form-group row py-2">
                                <label htmlFor="price" className="col-sm-2 col-form-label text-start">Price</label>
                                <div className="col-sm-10">
                                    <input type="number" lang="en-150" className="form-control-plaintext" id="price"
                                           min="0" max="50000" step="0.01" ref={this.priceInput} value={cert.price}
                                           readOnly={true}/>
                                </div>
                            </div>
                            <div className="form-group row py-2">
                                <label htmlFor="duration"
                                       className="col-sm-2 col-form-label text-start">Duration</label>
                                <div className="col-sm-10">
                                    <input type="number" className="form-control" id="duration"
                                           min="1" max="365" step="1" ref={this.durationInput} required
                                           defaultValue={cert.duration}/>
                                </div>
                            </div>
                            <TagsInput tagsInput={this.tagsInput} tags={tags}/>
                        </form>
                    </Modal.Body>
                    <Modal.Footer>
                        <button type="submit" className="btn btn-primary" onClick={this.handleSubmit}>Update</button>
                    </Modal.Footer>
                </Modal>
            </>
        );
    }
};
