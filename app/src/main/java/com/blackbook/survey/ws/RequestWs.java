package com.blackbook.survey.ws;

import org.json.JSONObject;

public class RequestWs {

	/** Get Request **/
	public <CLS> CLS getPostRequest(String url, JSONObject jsonData, Class<CLS> cls) throws Exception
	{
		return new WebServiceRequestPost(url, jsonData).execute(cls);
	}

	public <CLS> CLS getPostRequest(String url, Class<CLS> cls) throws Exception
	{
		return new WebServiceRequestPost(url).execute(cls);
	}

}
