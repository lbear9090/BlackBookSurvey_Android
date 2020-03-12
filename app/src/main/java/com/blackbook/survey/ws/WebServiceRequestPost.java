package com.blackbook.survey.ws;

import android.util.Log;

import com.blackbook.survey.Utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.HttpsURLConnection;


public class WebServiceRequestPost {

    private String url;
    private JSONObject jsonData = null;

    private ObjectMapper mapper = null;
    private final Lock lock = new ReentrantLock();

    public WebServiceRequestPost(String url)
    {
        this.url = url;
    }

    public WebServiceRequestPost(String url,JSONObject jsonData)
    {
        this.url = url;
        this.jsonData = jsonData;
    }

    public <Response> Response execute(
            Class<Response> responseType) throws Exception {

        Response response;
        InputStream inputStream;
        HttpURLConnection urlConnection;
        int statusCode = 0;

        try {

            /* forming th java.net.URL object */
            URL url = new URL(this.url);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.connect();

            /* pass post data */
            if(jsonData != null)
            {
                byte[] outputBytes = jsonData.toString().getBytes("UTF-8");
                OutputStream os = urlConnection.getOutputStream();
                os.write(outputBytes);
                os.close();
            }

            /* Get Response and execute WebService request*/
            statusCode = urlConnection.getResponseCode();

            /* 200 represents HTTP OK */
            if (statusCode == HttpsURLConnection.HTTP_OK) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                String responseString = convertInputStreamToString(inputStream);
                Log.i(Utils.TAG, "jsonData : " + jsonData + "");
                Log.i(Utils.TAG, "url : " + this.url + "");
                Log.i(Utils.TAG, "Response : " + responseString + "");
                response = getMapper().readValue(responseString, responseType);
            } else {
                response = null;
            }


        } catch (Exception e) {
            Log.i(Utils.TAG, "Status code: " + Integer.toString(statusCode) + " Exception thrown: " + e.getMessage());
            throw e;
        }

        return response;

    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String result = "";

        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

        inputStream.close();

        return result;
    }

    protected synchronized ObjectMapper getMapper() {

        if (mapper != null) {
            return mapper;
        }

        try {
            lock.lock();
            if (mapper == null) {
                mapper = new ObjectMapper();
                mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES,
                        false);
            }
            lock.unlock();
        } catch (Exception ex) {
            Log.i(Utils.TAG, "DatingApp Mapper Initialization Failed Exception : " + ex.getMessage());
        }

        return mapper;
    }
}