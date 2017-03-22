package com.jobnow.models;

import java.io.Serializable;
import java.util.List;

public class LocationResponse extends BaseResponse implements Serializable {
    public List<LocationObject> result;
}
