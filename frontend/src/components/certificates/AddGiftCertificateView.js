import React from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import {Alert} from "react-bootstrap";
import TagsInput from "./TagsInput";

const AddGiftCertificateView = (props) => {
    const {
        modalShow,
        error,
        nameInput,
        descriptionInput,
        priceInput,
        durationInput,
        tagsInput,
        handleSubmit,
        setModalShow,
        setError
    } = props;

    return (
        <>
            <Button variant="primary" onClick={() => setModalShow(true)} className="mt-5">
                Create
            </Button>

            <Modal
                show={modalShow}
                onHide={() => {
                    setModalShow(false);
                    setError(null)
                }}
                size="lg"
                aria-labelledby="contained-modal-title-vcenter"
                centered
            >
                <Modal.Header closeButton>
                    <Modal.Title id="contained-modal-title-vcenter">
                        Create new certificate </Modal.Title>

                </Modal.Header>
                <Modal.Body>
                    <Alert show={error} variant='danger'>{error}</Alert>
                    <form>
                        <div className="form-group row py-2">
                            <label htmlFor="name" className="col-sm-2 col-form-label text-start">Name</label>
                            <div className="col-sm-10">
                                <input type="text" className="form-control" id="name" placeholder="Name"
                                       minLength="6"
                                       maxLength="30" ref={nameInput}/>
                            </div>
                        </div>
                        <div className="form-group row py-2">
                            <label htmlFor="description"
                                   className="col-sm-2 col-form-label text-start">Description</label>
                            <div className="col-sm-10">
                                <input type="text" className="form-control" id="description" minLength="12"
                                       maxLength="1000" placeholder="Description" ref={descriptionInput}
                                       required/>
                            </div>
                        </div>
                        <div className="form-group row py-2">
                            <label htmlFor="price" className="col-sm-2 col-form-label text-start">Price</label>
                            <div className="col-sm-10">
                                <input type="number" className="form-control" id="price" placeholder="0.00"
                                       min="0" max="50000" step="0.01" ref={priceInput} required/>
                            </div>
                        </div>
                        <div className="form-group row py-2">
                            <label htmlFor="duration"
                                   className="col-sm-2 col-form-label text-start">Duration</label>
                            <div className="col-sm-10">
                                <input type="number" className="form-control" id="duration" placeholder="0"
                                       min="1" max="365" step="1" ref={durationInput} required/>
                            </div>
                        </div>
                        <TagsInput tagsInput={tagsInput}/>
                    </form>
                </Modal.Body>
                <Modal.Footer>
                    <button type="submit" className="btn btn-primary" onClick={handleSubmit}>Create</button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
export default AddGiftCertificateView;