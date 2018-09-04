package com.restObjects;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class RestObject {
    public static final String BASE_URL = "https://magic-database.herokuapp.com";
    private List<Header> headers;
    public RestObject() {
        headers = new ArrayList<>();
        headers.add(new BasicHeader("Content-type", "application/json"));

    }
    public void setHeader(String headerName, String value) {
        this.headers.add(new BasicHeader(headerName, value));
    }
    public void removeHeader(String headerName) {
        for(int i = 0; i < headers.size(); i++) {
            if(headers.get(i).getName().equals(headerName)) {
                this.headers.remove(i);
            }
        }
    }
    public void resetHeaders() {
        headers = new ArrayList<>();
    }
    public Response sendGetRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeaders(headers.toArray(new Header[headers.size()]));
        return sendRequest(request);
    }
    public Response sendPostRequest(String url, String json) {
        HttpPost request = new HttpPost(url);
        request.setHeaders(headers.toArray(new Header[headers.size()]));
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(json.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        return sendRequest(request);
    }
    public Response sendPutRequest(String url, String json) {
        HttpPut request = new HttpPut(url);
        request.setHeaders(headers.toArray(new Header[headers.size()]));
        ByteArrayEntity entity = null;
        try {
            entity = new ByteArrayEntity(json.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        request.setEntity(entity);
        return sendRequest(request);
    }
    public Response sendDeleteRequest(String url) {
        HttpDelete request = new HttpDelete(url);
        request.setHeaders(headers.toArray(new Header[headers.size()]));
        return sendRequest(request);
    }
    private Response sendRequest(HttpUriRequest request) {
        Response response = null;
        RequestConfig config = RequestConfig.DEFAULT;
        CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        try {
            HttpResponse httpResponse = client.execute(request);
            response = new Response(request, httpResponse);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
