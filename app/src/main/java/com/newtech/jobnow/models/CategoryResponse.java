package com.newtech.jobnow.models;

import java.io.Serializable;
import java.util.List;

public class CategoryResponse extends BaseResponse implements Serializable {
    public List<CategoryObject> result;
}
