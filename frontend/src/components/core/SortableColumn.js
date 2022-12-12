import React, {Component} from "react";

const DESC_FILTER_CLASS = "bi bi-filter-left";
const ASC_FILTER_CLASS = "bi bi-filter-right";

export default class SortableColumn extends Component {
    constructor(props) {
        super(props);
        this.flag = false;
    }

    render() {
        let filterClass = "";
        if (this.props.selected) {
            filterClass = this.flag ? DESC_FILTER_CLASS : ASC_FILTER_CLASS;
            this.flag = true;
        } else {
            this.flag = false;
        }
        const className = "sortable " + this.props.class;
        return (
            <th className={className} onClick={this.props.handleSort}>{this.props.name} <i className={filterClass}></i>
            </th>
        )
    }

}