import React, {Component} from "react";

export default class SearchBar extends Component {
    constructor(props) {
        super(props);
        this.handleKeyDown = this.handleKeyDown.bind(this);
    }

    handleKeyDown = e => {
        if (e.key === "Enter" && e.target.value !== "") {
            e.preventDefault();
            this.props.processSearch();
        }
    }

    render() {
        return (
            <form className="d-flex my-2">
                <input type="search" placeholder="Search..." className="form-control me-2" aria-label="Search"
                       ref={this.props.inputRef} onKeyDown={e => this.handleKeyDown(e)}/>
                <button type="button" className="btn btn-outline-dark" onClick={this.props.processSearch}>Search
                </button>
            </form>
        )
    }

}