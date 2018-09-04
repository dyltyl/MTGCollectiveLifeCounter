package com.restObjects;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Response {
    private HttpUriRequest request;
    private HttpResponse response;
    private String requestBody, url, method, stringResponse;
    private int statusCode;
    private ObjectMapper mapper = new ObjectMapper();
    public Response(HttpUriRequest request, HttpResponse httpResponse) {
        this.request = request;
        if(request instanceof HttpEntityEnclosingRequest) {
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();
            if(entity != null) {
                try {
                    requestBody = EntityUtils.toString(entity);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        url = request.getURI().toString();
        method = request.getMethod();
        response = httpResponse;
        if(response.getEntity() != null) {
            try {
                stringResponse = EntityUtils.toString(response.getEntity());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        statusCode = response.getStatusLine().getStatusCode();
    }
    public<T> T mapJSONToObject(Class<T> type) {
        T map = null;
        try {
            map = mapper.readValue(stringResponse, type);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public String getStringResponse() {
        return stringResponse;
    }

    public HttpUriRequest getRequest() {
        return request;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
