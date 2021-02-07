package com.palight.playerinfo.util;

import org.apache.http.HttpResponse;

import java.io.IOException;

public interface HttpUtilResponseHandler {
    void handleResponse(HttpResponse response) throws IOException;
}
