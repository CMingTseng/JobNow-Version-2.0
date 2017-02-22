package com.newtech.jobnow.models;

import java.io.Serializable;

/**
 * Created by manhi on 26/8/2016.
 */
public class CreateDateObject implements Serializable {
    public String date;
    public int timezone_type;
    String timezone;

    public CreateDateObject(String date, int timezone_type, String timezone) {
        this.date = date;
        this.timezone_type = timezone_type;
        this.timezone = timezone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTimezone_type() {
        return timezone_type;
    }

    public void setTimezone_type(int timezone_type) {
        this.timezone_type = timezone_type;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
