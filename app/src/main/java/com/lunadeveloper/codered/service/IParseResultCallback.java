package com.lunadeveloper.codered.service;

import com.lunadeveloper.codered.model.ResultModel;
/**
 * Callback interface to allow us to asyncronously retrieve data from Parse into our system.
 * Created by benjamin on 10/15/14.
 */
public interface IParseResultCallback {

    /**
     * Called if we successfully got a return String
     * @param result
     */
    public void onSuccess(ResultModel result);

    /**
     * Called if something goes wrong.
     * @param message
     */
    public void onFail(String message);
}
