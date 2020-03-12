package com.blackbook.survey.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseResult implements Serializable
{
    private static final long serialVersionUID = 1L;

    private ResponseObject Result;

    @JsonProperty("Result")
    public ResponseObject getResult()
    {
        return Result;
    }

    public void setResult(ResponseObject result)
    {
        Result = result;
    }

}
