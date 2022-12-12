import React from "react";

const PaginationNavBar = (props) => {
    return (
        <nav aria-label="navigation" className="d-flex justify-content-center">
            <ul className="pagination ms-auto">
                {props.linksElem}
            </ul>
            <div className="page-item ms-auto">
                <select className="form-select" aria-label="select" value={props.pageNum}
                        onChange={props.handlePageSize}>
                    <option value="5">5</option>
                    <option value="10">10</option>
                    <option value="20">20</option>
                </select>
            </div>
        </nav>
    )
}
export default PaginationNavBar;