package com.blackbook.survey.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 *
 * Created by c119 on 04/04/16.
 *
 */
public class Preferences implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String preference_text;

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

    @JsonProperty("preference_text")
    public String getPreference_text() {
        return preference_text;
    }

    public void setPreference_text(String preference_text) {
        this.preference_text = preference_text;
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
