package com.blackbook.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * Created by c119 on 04/04/16.
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoreMatrix implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String matrix_title;

    private String matrix_description;

    private String start_range;

    private String end_range;

    private String created_date;

    private String modified_date;

    private String is_deleted;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("matrix_title")
    public String getMatrix_title() {
        return matrix_title;
    }

    public void setMatrix_title(String matrix_title) {
        this.matrix_title = matrix_title;
    }

    @JsonProperty("matrix_description")
    public String getMatrix_description() {
        return matrix_description;
    }

    public void setMatrix_description(String matrix_description) {
        this.matrix_description = matrix_description;
    }

    @JsonProperty("start_range")
    public String getStart_range() {
        return start_range;
    }

    public void setStart_range(String start_range) {
        this.start_range = start_range;
    }

    @JsonProperty("end_range")
    public String getEnd_range() {
        return end_range;
    }

    public void setEnd_range(String end_range) {
        this.end_range = end_range;
    }

    @JsonProperty("created_date")
    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    @JsonProperty("modified_date")
    public String getModified_date() {
        return modified_date;
    }

    public void setModified_date(String modified_date) {
        this.modified_date = modified_date;
    }

    @JsonProperty("is_deleted")
    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }

}
