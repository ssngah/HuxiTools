package com.huxi.models;

import android.content.Context;
import android.support.annotation.Nullable;

/**
 * Created by Phyllis on 15-5-14.
 * 应用错误封装类，本地错误及网络错误都是使用这个类封装
 */
public class HXError {
    public static final String WEB_DOMAIN = "webDomain";
    public static final String MODEL_DOMAIN = "modelDomain";
    public static final int LOCAL_FAIL_CODE = -3;
    public static final int WEB_REQ_FAIL_CODE = -2;
    public static final int SESSION_EXPIRED_CODE = 401;
    int code;
    String domain;
    String reason;
    String detail;
    private int reasonTextId;

    public HXError(int code, String domain, String reason) {
        this.code = code;
        this.domain = domain;

        this.reason = reason;
    }
    public HXError(int code, String domain, int reasonResId){
        this.code = code;
        this.domain = domain;
        this.reasonTextId = reasonResId;
    }

    public int getCode() {
        return code;
    }

    public String getDomain() {
        return domain;
    }

    public String getReason(@Nullable Context context) {
        if(context==null||reasonTextId==0){
            if(reason==null){
                return "发生未知错误";
            } else if (detail != null) {
                return reason + "(" + detail + ")";
            }
            return reason;
        }
        return context.getResources().getString(this.reasonTextId);

    }
    public static HXError getWebFailCommonError(int textid){
        return new HXError(WEB_REQ_FAIL_CODE,WEB_DOMAIN,textid);
    }
    public static HXError getLocalFailError(int textid){
        return new HXError(LOCAL_FAIL_CODE,MODEL_DOMAIN,textid);
    }

}
