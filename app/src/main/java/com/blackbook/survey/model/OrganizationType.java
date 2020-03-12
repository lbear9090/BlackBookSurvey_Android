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
public class OrganizationType implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String id;

    private String organization_type_name;

    private String level;

    private String parent_id;

    private String is_optional;

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

    @JsonProperty("organization_type_name")
    public String getOrganization_type_name() {
        return organization_type_name;
    }

    public void setOrganization_type_name(String organization_type_name) {
        this.organization_type_name = organization_type_name;
    }

    @JsonProperty("level")
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @JsonProperty("parent_id")
    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    @JsonProperty("is_optional")
    public String getIs_optional() {
        return is_optional;
    }

    public void setIs_optional(String is_optional) {
        this.is_optional = is_optional;
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
