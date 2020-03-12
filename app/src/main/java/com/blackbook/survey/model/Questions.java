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
public class Questions implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String question_title;

    private String question_description;

    private String question_type;

    private String question_format;

    private String option_count;

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

    @JsonProperty("question_title")
    public String getQuestion_title() {
        return question_title;
    }

    public void setQuestion_title(String question_title) {
        this.question_title = question_title;
    }

    @JsonProperty("question_description")
    public String getQuestion_description() {
        return question_description;
    }

    public void setQuestion_description(String question_description) {
        this.question_description = question_description;
    }

    @JsonProperty("question_type")
    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    @JsonProperty("question_format")
    public String getQuestion_format() {
        return question_format;
    }

    public void setQuestion_format(String question_format) {
        this.question_format = question_format;
    }

    @JsonProperty("option_count")
    public String getOption_count() {
        return option_count;
    }

    public void setOption_count(String option_count) {
        this.option_count = option_count;
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
