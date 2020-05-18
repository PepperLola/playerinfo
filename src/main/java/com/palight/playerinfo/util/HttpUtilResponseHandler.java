package com.palight.playerinfo.util;

import org.apache.http.HttpResponse;

public interface HttpUtilResponseHandler {
    void handleResponse(HttpResponse response);
}
