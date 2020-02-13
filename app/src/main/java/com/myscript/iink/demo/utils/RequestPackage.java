package com.myscript.iink.demo.utils;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestPackage implements Parcelable {

    private String endPoint;
    private String method = "GET";
    private Map<String, String> postParams = new HashMap<>();
    private Map<String, String> requestParams = new HashMap<>();

    public RequestPackage() {

    }

    public Map<String, String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String key, String value) {
        requestParams.put(key, value);
    }

    public String getEndpoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getPostParams() {
        return postParams;
    }

    public void setPostParams(String key, String value) {
        postParams.put(key, value);
    }



    public String getEncodedParams() {
        StringBuilder sb = new StringBuilder();
        for (String key : requestParams.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(requestParams.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.endPoint);
        dest.writeString(this.method);
        dest.writeInt(this.postParams.size());
        for (Map.Entry<String, String> entry : this.postParams.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeInt(this.requestParams.size());
        for (Map.Entry<String, String> entry : this.requestParams.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }

    protected RequestPackage(Parcel in) {
        this.endPoint = in.readString();
        this.method = in.readString();
        int postParamsSize = in.readInt();
        this.postParams = new HashMap<String, String>(postParamsSize);
        for (int i = 0; i < postParamsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.postParams.put(key, value);
        }
        int requestParamsSize = in.readInt();
        this.requestParams = new HashMap<String, String>(requestParamsSize);
        for (int i = 0; i < requestParamsSize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.requestParams.put(key, value);
        }
    }

    public static final Creator<RequestPackage> CREATOR = new Creator<RequestPackage>() {
        @Override
        public RequestPackage createFromParcel(Parcel source) {
            return new RequestPackage(source);
        }

        @Override
        public RequestPackage[] newArray(int size) {
            return new RequestPackage[size];
        }
    };
}