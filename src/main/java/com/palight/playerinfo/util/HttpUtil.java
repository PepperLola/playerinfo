package com.palight.playerinfo.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HttpUtil {
    public static HttpResponse httpGetResponse(String url) {
        try {
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setHostnameVerifier(new AllowAllHostnameVerifier())
                    .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build())
                    .build();
            HttpGet httpGet = new HttpGet(url);
            return httpclient.execute(httpGet);
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String httpGet(String url) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            System.out.println("Executing request " + httpGet.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else if (status == 429) {
                    return null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            };
            return httpclient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static HttpResponse httpPost(String url, String data) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new StringEntity(data));
            httpPost.addHeader("content-type", "application/json");

            return httpclient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void httpGet(final String url, final HttpUtilResponseHandler handler) {
        Thread reqThread = new Thread(() -> {
            try {
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setHostnameVerifier(new AllowAllHostnameVerifier())
                        .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build())
                        .build();
                HttpGet httpGet = new HttpGet(url);
                if (handler != null) {
                    handler.handleResponse(httpclient.execute(httpGet));
                }
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });

        reqThread.start();
    }

    public static void httpGet(final String url, Map<String, String> headers, final HttpUtilResponseHandler handler) {
        Thread reqThread = new Thread(() -> {
            try {
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setHostnameVerifier(new AllowAllHostnameVerifier())
                        .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build())
                        .build();
                HttpGet httpGet = new HttpGet(url);
                for (String header : headers.keySet()) {
                    httpGet.addHeader(header, headers.get(header));
                }
                if (handler != null) {
                    handler.handleResponse(httpclient.execute(httpGet));
                }
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });

        reqThread.start();
    }

    public static void httpPut(final String url, final Map<String, String> headers, final String data, final HttpUtilResponseHandler handler) {
        Thread reqThread = new Thread(() -> {
            try {
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setHostnameVerifier(new AllowAllHostnameVerifier())
                        .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build())
                        .build();
                HttpPut httpPut = new HttpPut(url);
                httpPut.setEntity(new StringEntity(data));
                for (String header : headers.keySet()) {
                    httpPut.addHeader(header, headers.get(header));
                }
                if (handler != null) {
                    handler.handleResponse(httpclient.execute(httpPut));
                }
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });

        reqThread.start();
    }

    public static void httpPost(final String url, final Map<String, String> headers, final String data, final HttpUtilResponseHandler handler) {
        Thread reqThread = new Thread(() -> {
            try {
                CloseableHttpClient httpclient = HttpClients.custom()
                        .setHostnameVerifier(new AllowAllHostnameVerifier())
                        .setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build())
                        .build();
                HttpPost httpPut = new HttpPost(url);
                httpPut.setEntity(new StringEntity(data));
                for (String header : headers.keySet()) {
                    httpPut.addHeader(header, headers.get(header));
                }
                if (handler != null) {
                    handler.handleResponse(httpclient.execute(httpPut));
                }
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
                e.printStackTrace();
            }
        });

        reqThread.start();
    }
}
