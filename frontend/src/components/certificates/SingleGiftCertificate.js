import React from "react";
import ViewGiftCertificate from "./ViewGiftCertificate";
import EditGiftCertificate from "./EditGiftCertificate";
import DeleteGiftCertificate from "./DeleteGiftCertificate";

const SingleGiftCertificate = (props) => {
    let tags = [];
    props.certificate.tags.forEach(tag => tags.push(tag.name));
    const date = new Date(props.certificate.createDate).toLocaleDateString("en-UK");
    return (
        <tr>
            <td>{props.certificate.name}</td>
            <td>{props.certificate.price}</td>
            <td>{date}</td>
            <td>{tags.join(", ")}</td>
            <td className="d-flex justify-content-between">
                <ViewGiftCertificate className="bi bi-eye clickable" style={{color: "darkblue"}}
                                     certificate={props.certificate}/>
                <EditGiftCertificate className="bi bi-pencil clickable" style={{color: "orange"}}
                                     certificate={props.certificate} reload={props.reload}/>
                <DeleteGiftCertificate className="bi bi-trash clickable" style={{color: "red"}}
                                       certificateId={props.certificate.id} reload={props.reload}/>
            </td>
        </tr>
    )
}
export default SingleGiftCertificate;