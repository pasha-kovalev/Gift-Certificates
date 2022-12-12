import React, {Component} from "react";
import Modal from "react-bootstrap/Modal";

export default class ViewGiftCertificate extends Component {
    constructor(props) {
        super(props);
        this.state = {
            modalShow: false
        };
    }

    setModalShow = (visible) => {
        this.setState({modalShow: visible});
    }

    render() {
        const {modalShow} = this.state;
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
                            {this.props.certificate.name} </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        {this.props.certificate.description}
                    </Modal.Body>
                </Modal>
            </>
        );
    }
};
