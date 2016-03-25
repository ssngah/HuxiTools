package com.huxi.web;

import android.text.TextUtils;
import com.huxi.models.HXError;

/**
 * Created by Phyllis on 15-5-21.
 */
public class HXWebResult<T> {
    private int    errcode;
    private T      data;
    private String errmsg;
    private String detail;
    public static final int    SUCCESS_CODE_START = 200;
    /**
     * The name to get data part in json in case you need to handle the json yourself;
     */
    public static final  String KEY_DATA           = "data";

    public HXWebResult() {

    }

    public HXWebResult(int errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public String getErrmsg() {
        if (TextUtils.isEmpty(detail))
            return errmsg;
        else
            return errmsg + "(" + detail + ")";
    }

    public boolean isRqSuccess() {
        return errcode >= 0;
    }

    public boolean isOpSuccess() {
        return errcode == SUCCESS_CODE_START;
    }

    public HXError getErrorDescript() {
        if(errcode == SUCCESS_CODE_START) {
            return null;
        }
        return new HXError(errcode, HXError.WEB_DOMAIN, getErrmsg());
    }
}
