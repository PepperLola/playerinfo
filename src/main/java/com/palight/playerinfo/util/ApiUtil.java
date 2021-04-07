package com.palight.playerinfo.util;

import org.apache.http.HttpResponse;

import java.io.IOException;

public class ApiUtil {
    public static final String API_URL = "http://localhost:2733";
    public static String TOKEN;

    public static void authenticate(String username, String password, String userId) throws IOException {
        String data = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"userId\": \"%s\"}", username, password, userId);
        HttpResponse response = HttpUtil.httpPost(API_URL + "/users", data);

        assert response != null;
        int status = response.getStatusLine().getStatusCode();
        if (status >= 200 && status < 300) {
            System.out.println("SUCCESS!");
        }
    }
}
