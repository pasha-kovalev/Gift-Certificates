import React, {Component} from "react";
import {getAuthHeader} from "../../utils/authUtils";
import AddGiftCertificateView from "./AddGiftCertificateView";

export default class AddGiftCertificate extends Component {
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
            name: this.nameInput.current.value,
            description: this.descriptionInput.current.value,
            price: this.priceInput.current.value,
            duration: this.durationInput.current.value,
            tags: [].map
                .call(document.querySelectorAll('.tag-name'), e => ({"name": e.innerHTML}))
        }
        fetch('http://localhost:8081/gift-certificates',
            {
                method: "POST",
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
            .catch(err => {
                console.log("ERR");
                err.then((json) => {
                    this.setError(json.message);
                })
            });
    }

    render() {
        return (
            <>
                <AddGiftCertificateView modalShow={this.state.modalShow}
                                        error={this.state.error}
                                        nameInput={this.nameInput}
                                        descriptionInput={this.descriptionInput}
                                        priceInput={this.priceInput}
                                        durationInput={this.durationInput}
                                        tagsInput={this.tagsInput}
                                        handleSubmit={this.handleSubmit}
                                        setModalShow={this.setModalShow}
                                        setError={this.setError}/>
            </>
        );
    }
};
