package com.jobnow.models;

public class Result {
    private int code;
    private String description;
    private String session_id;
    public Result(int code, String description, String session_id) {
        this.code = code;
        this.description = description;
        this.session_id = session_id;
    }

    public Result() {
    }

    public int getCode() {
        return code;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String s) {
        this.session_id = s;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
