package com.lunadeveloper.codered.model;

/**
 * Created by Luna on 2/8/16.
 */
public class ResultModel {
    int status;
    String message;

    public ResultModel() {}

    public ResultModel(int s, String m) {
        this.status = s;
        this.message = m;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
