package com.myscript.iink.demo.models;

import android.util.Log;

public class WebResponse {
    private String success;
    private String reply;
    private String finished = "true";

    public WebResponse(String success, String reply) {
        this.success = success;
        this.reply = reply;
        Log.i("Constructor", "Used First");
    }

    public WebResponse(String success, String reply, String finished) {
        this.success = success;
        this.reply = reply;
        this.finished = finished;
        Log.i("Constructor", "Used Second");
    }

    public boolean getSuccess() {
        if(success.equalsIgnoreCase("true")) {
            Log.i("Success", "Success is true");
            return true;
        } else {
            Log.i("Success", "Success is false");
            return false;
        }
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean getFinished() {
        if(finished.equalsIgnoreCase("true")) {
            return true;
        } else {
            return false;
        }
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "WebResponse{" +
                "success='" + success + '\'' +
                ", reply='" + reply + '\'' +
                ", finished='" + finished + '\'' +
                '}';
    }
}
