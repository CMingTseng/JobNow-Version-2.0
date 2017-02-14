package com.newtech.jobnow.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SANG on 8/21/2016.
 */
public class LevelResponse extends BaseResponse implements Serializable {
    public List<LevelObject> result;
}
