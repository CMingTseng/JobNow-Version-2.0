package com.jobnow.models;

import java.io.Serializable;
import java.util.List;

public class EmployeeResponse extends BaseResponse implements Serializable {
    public List<EmployeeObject> result;
}
