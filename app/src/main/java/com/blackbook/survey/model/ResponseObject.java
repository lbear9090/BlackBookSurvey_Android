package com.blackbook.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseObject implements Serializable
{

    private static final long serialVersionUID = 1L;

    private String status;

    private String error_status;

    private ArrayList<SurveyType> arr_surveytype;

    private ArrayList<Vendors> arr_vendors;

    private ArrayList<Rates> arr_rates;

    private ArrayList<OrganizationType> arr_organizationtype;

    private ArrayList<Roles> arr_roles;

    private ArrayList<Questions> arr_questions;

    private ArrayList<ScoreMatrix> arr_scorematrix;

    private ArrayList<Preferences> arr_preferences;

    private ArrayList<User> arr_user;

    @JsonProperty("User")
    public ArrayList<User> getArr_user() {
        return arr_user;
    }

    public void setArr_user(ArrayList<User> arr_user) {
        this.arr_user = arr_user;
    }

    @JsonProperty("OrganizationType")
    public ArrayList<OrganizationType> getArr_organizationtype() {
        return arr_organizationtype;
    }

    public void setArr_organizationtype(ArrayList<OrganizationType> arr_organizationtype) {
        this.arr_organizationtype = arr_organizationtype;
    }

    @JsonProperty("Roles")
    public ArrayList<Roles> getArr_roles() {
        return arr_roles;
    }

    public void setArr_roles(ArrayList<Roles> arr_roles) {
        this.arr_roles = arr_roles;
    }

    @JsonProperty("Questions")
    public ArrayList<Questions> getArr_questions() {
        return arr_questions;
    }

    public void setArr_questions(ArrayList<Questions> arr_questions) {
        this.arr_questions = arr_questions;
    }

    @JsonProperty("ScoreMatrix")
    public ArrayList<ScoreMatrix> getArr_scorematrix() {
        return arr_scorematrix;
    }

    public void setArr_scorematrix(ArrayList<ScoreMatrix> arr_scorematrix) {
        this.arr_scorematrix = arr_scorematrix;
    }

    @JsonProperty("Preferences")
    public ArrayList<Preferences> getArr_preferences() {
        return arr_preferences;
    }

    public void setArr_preferences(ArrayList<Preferences> arr_preferences) {
        this.arr_preferences = arr_preferences;
    }

    @JsonProperty("Vendors")
    public ArrayList<Vendors> getArr_vendors() {
        return arr_vendors;
    }

    @JsonProperty("Rates")
    public ArrayList<Rates> getArr_rates() {
        return arr_rates;
    }

    public void setArr_vendors(ArrayList<Vendors> arr_vendors) {
        this.arr_vendors = arr_vendors;
    }

    public void setArr_rates(ArrayList<Rates> arr_rates) {
        this.arr_rates = arr_rates;
    }

    @JsonProperty("status")
    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @JsonProperty("error_status")
    public String getError_status() {
        return error_status;
    }

    public void setError_status(String error_status) {
        this.error_status = error_status;
    }

    @JsonProperty("SurveyType")
    public ArrayList<SurveyType> getArr_surveytype() {
        return arr_surveytype;
    }

    public void setArr_surveytype(ArrayList<SurveyType> arr_surveytype) {
        this.arr_surveytype = arr_surveytype;
    }
}

	

	

