import React, {useEffect, useState} from "react";
import "bootstrap-icons/font/bootstrap-icons.css";

const TagsInput = (props) => {
    const [tags, setTags] = useState(new Set());
    const maxTagsArraySize = 10;

    useEffect(() => {
        if (props.tags) {
            setTags(new Set(props.tags));
        }
    }, [])

    const removeTags = elem => {
        setTags(new Set([...tags].filter(x => x !== elem.tag)));
    };

    const addTags = event => {
        if (event.key === "Enter" && event.target.value !== "") {
            const tagInput = props.tagsInput.current;
            if (!tagInput.reportValidity()) {
                tagInput.setCustomValidity("");
                return;
            }

            if (tags.size === maxTagsArraySize) {
                tagInput.setCustomValidity("Max amount of tags: " + maxTagsArraySize);
                tagInput.reportValidity();
                return;
            }

            setTags(new Set([...tags, event.target.value.toLowerCase()]));
            event.target.value = "";
        }
    };

    return (
        <div>
            <div className="form-group row py-2">
                <label htmlFor="tag" className="col-sm-2 col-form-label text-start">Tags</label>
                <div className="col-sm-10">
                    <input type="text" className="form-control" id="tag" placeholder="Press Enter to add tag"
                           minLength="3" maxLength="15" onKeyDown={e => addTags(e)} ref={props.tagsInput}/>
                </div>
            </div>
            <div className="form-group row py-2">
                <div className="col-sm-2 col-form-label text-start"></div>
                <div className="col-sm-10">
                    <div className="d-flex flex-wrap bd-highlight">
                        {
                            Array.from(tags).map((tag) =>
                                <div className="p-2 bd-highlight" key={tag}>
                                    <span className="tag-name">{tag}</span>
                                    <span className="bi bi-x-circle ps-1" role="button"
                                          onClick={() => removeTags({tag})}></span>
                                </div>
                            )
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}
export default TagsInput