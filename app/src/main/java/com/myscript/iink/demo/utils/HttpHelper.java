package com.myscript.iink.demo.utils;

import java.io.IOException;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelper {


    public static String dowloadUrl(RequestPackage requestPackage)
            throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if(encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        OkHttpClient client = new OkHttpClient();
        Request.Builder requestBuilder = new Request.Builder()
                .url(address);

        if (requestPackage.getMethod().equals("POST")) {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            Map<String, String> params = requestPackage.getPostParams();
            for (String key: params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
            RequestBody requestBody = builder.build();
            requestBuilder.method("POST", requestBody);
        }


        Request request = requestBuilder.build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Exception: response code" + response.code());
        }

    }
}