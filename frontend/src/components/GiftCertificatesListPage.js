import React, {Component} from "react";
import SingleGiftCertificate from "./certificates/SingleGiftCertificate";
import PaginationNavBar from "./core/PaginationNavBar";
import SearchBar from "./core/SearchBar";
import SortableColumn from "./core/SortableColumn";
import {withRouter} from 'react-router-dom'
import AddGiftCertificate from "./certificates/AddGiftCertificate";

const searchParams = new URLSearchParams(document.location.search)

class GiftCertificatesListPage extends Component {
    constructor(props) {
        super(props);
        this.searchInput = React.createRef();

        this.getPaginationLinks = this.getPaginationLinks.bind(this);
        this.handleNextPageLink = this.handleNextPageLink.bind(this);
        this.loadFromServer = this.loadFromServer.bind(this);
        this.handlePrevPageLink = this.handlePrevPageLink.bind(this);
        this.handlePageLink = this.handlePageLink.bind(this);
        this.handleFirstPageLink = this.handleFirstPageLink.bind(this);
        this.handleLastPageLink = this.handleLastPageLink.bind(this);
        this.handlePageSize = this.handlePageSize.bind(this);
        this.handleSearch = this.handleSearch.bind(this);
        this.isSelectedSort = this.isSelectedSort.bind(this);

        this.sort = searchParams.get('sort') || "";
        this.filter = searchParams.get('search') || "";
        this.state = {
            giftCertificates: [],
            pageSize: searchParams.get('size') || 10,
            page: searchParams.get('page') || 1,
            totalPages: 0,
            links: {}
        }
    }

    componentDidMount() {
        this.loadFromServer()
    }

    setSort(sort) {
        this.sort = sort;
        return sort;
    }

    setFilter(filter) {
        this.filter = filter;
        return filter;
    }

    loadFromServer(linkStr = null, pageNum = null, pageSize = null, sortValue = null, filterValue = null) {
        const page = pageNum ? pageNum : this.state.page;
        const size = pageSize ? pageSize : this.state.pageSize;
        const filterQuery = filterValue
            ? "&search=" + this.setFilter(filterValue)
            : (this.filter ? "&search=" + this.filter : this.filter);
        const sortQuery = sortValue
            ? "&sort=" + this.setSort(sortValue)
            : (this.sort ? "&sort=" + this.sort : this.sort);
        const queryString = `?page=${page}&size=${size}${sortQuery}${filterQuery}`;
        const link = linkStr
            ? linkStr
            : `http://localhost:8081/gift-certificates${queryString}`;

        fetch(link)
            .then(response => response.json())
            .then(data => {
                searchParams.set('page', data.page.number);
                searchParams.set('size', data.page.size);
                this.setState(
                    {
                        page: data.page.number,
                        giftCertificates: data._embedded ? data._embedded.giftCertificates : [],
                        links: data._links ? data._links : [],
                        pageSize: data.page.size,
                        totalPages: data.page.totalPages
                    }
                );
            })
            .then(() => {
                this.props.history.push({
                    pathname: '/certificates',
                    search: new URL(link).search
                });
            })
            .catch(error => {
                console.error(error)
            })
    }

    handleFirstPageLink() {
        this.loadFromServer(this.state.links.first.href);
    }

    handleLastPageLink() {
        this.loadFromServer(this.state.links.last.href);
    }

    handleNextPageLink() {
        this.loadFromServer(this.state.links.next.href);
    }

    handlePrevPageLink() {
        this.loadFromServer(this.state.links.prev.href);
    }

    handlePageLink(pageNum) {
        this.loadFromServer(null, pageNum);
    }

    handlePageSize(event) {
        const newPageSize = event.target.value;
        const currPageSize = this.state.pageSize;
        const maxPageNum = Math.floor(this.state.totalPages / (newPageSize / currPageSize));
        let pageNum = null;

        if (newPageSize > currPageSize && this.state.page > maxPageNum) {
            pageNum = maxPageNum;
        }
        this.loadFromServer(null, pageNum, event.target.value);
    }

    getPaginationLinks(totalPages = 5) {
        const linksElem = [];
        const currentPage = this.state.page;
        const total = Math.min(totalPages, this.state.totalPages);
        const hasPrev = "prev" in this.state.links;
        const hasNext = "next" in this.state.links;
        const halfPos = Math.ceil(total / 2);
        const hasPrevAndNextPagePosition = (currentPage < halfPos)
            ? halfPos - 1
            : (this.state.totalPages - currentPage + 1 < halfPos) ? halfPos + 1 : halfPos;
        const currentPagePosition = (hasPrev && hasNext)
            ? hasPrevAndNextPagePosition
            : (hasPrev ? total : 1);
        const startPage = currentPage - currentPagePosition + 1;

        if (hasPrev) {
            linksElem.push(<li key="first" className="page-item"><a className="page-link" href="javascript:void(0)"
                                                                    onClick={this.handleFirstPageLink}>First</a>
            </li>)
            linksElem.push(<li key="prev" className="page-item"><a className="page-link" href="javascript:void(0)"
                                                                   onClick={this.handlePrevPageLink}>Previous</a>
            </li>)
        }

        for (let i = 0; i < total; i++) {
            const pageNum = startPage + i;
            if (i === currentPagePosition - 1) {
                linksElem.push(
                    <li key={pageNum} className="page-item active">
                        <a className="page-link" href="javascript:void(0)"
                           onClick={() => this.handlePageLink(pageNum)}>{pageNum}</a>
                    </li>
                )
                continue;
            }
            linksElem.push(
                <li key={pageNum} className="page-item">
                    <a className="page-link" href="javascript:void(0)"
                       onClick={() => this.handlePageLink(pageNum)}>{pageNum}</a>
                </li>
            )
        }

        if (hasNext) {
            linksElem.push(<li key="next" className="page-item"><a className="page-link" href="javascript:void(0)"
                                                                   onClick={this.handleNextPageLink}>Next</a>
            </li>)
            linksElem.push(<li key="last" className="page-item"><a className="page-link" href="javascript:void(0)"
                                                                   onClick={this.handleLastPageLink}>Last</a>
            </li>)

        }
        return linksElem;
    }

    handleSort(name) {
        if (!this.sort || !this.isSelectedSort(name)) {
            this.sort = name;
        } else if (this.sort.startsWith('-') && this.isSelectedSort(name)) {
            this.sort = "";
        } else if (this.sort === name) {
            this.sort = "-" + name;
        }

        this.loadFromServer(null, 1);
    }

    handleSearch() {
        let flag = true;
        const inputValue = this.searchInput.current.value;
        if (!inputValue) {
            this.setFilter("");
            this.loadFromServer();
        }
        let split = inputValue.split(' ');
        let resArr = [];
        split.forEach(i => {
            if (i !== '') {
                if (i.startsWith('#')) {
                    resArr.push("tagName:" + i.substring(1));
                } else if (flag) {
                    resArr.push("name~" + i);
                    flag = false;
                }
            }
        });
        const filter = [...new Set(resArr)].join(',');
        this.loadFromServer(null, 1, null, null, filter);
    }

    isSelectedSort(field) {
        return (this.sort === field || this.sort === "-" + field)
    }

    render() {
        const certificates = this.state.giftCertificates.map(
            certificate => <SingleGiftCertificate key={certificate.id} certificate={certificate}
                                                  reload={this.loadFromServer}/>
        );
        const linksElem = this.getPaginationLinks();
        return (
            <div>
                <AddGiftCertificate/>
                <SearchBar processSearch={this.handleSearch} inputRef={this.searchInput}></SearchBar>
                <table className="table table-borderless table-sm">
                    <thead>
                    <tr>
                        <SortableColumn name="Name" field="name" handleSort={() => this.handleSort("name")}
                                        selected={this.isSelectedSort("name")} class="w-50"/>
                        <th className="w-auto">Price</th>
                        <SortableColumn name="CreateDate" field="createDate"
                                        handleSort={() => this.handleSort("createDate")}
                                        selected={this.isSelectedSort("createDate")}/>
                        <th className="w-25">Tags</th>
                        <th className="w-auto">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    {certificates}
                    </tbody>
                </table>
                <PaginationNavBar linksElem={linksElem} pageNum={this.state.pageSize}
                                  handlePageSize={this.handlePageSize}/>
            </div>
        )
    }
}

export default withRouter(GiftCertificatesListPage)