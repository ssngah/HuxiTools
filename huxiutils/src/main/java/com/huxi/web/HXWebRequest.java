package com.huxi.web;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.reflect.TypeToken;
import com.huxi.huxiutils.BroadCastCenter;
import com.huxi.models.HXError;
import com.xiaogu.xgvolleyex.BaseWebRequest;
import com.xiaogu.xgvolleyex.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phyllis on 15-5-23.
 * This class takes care of save token ,put token into header and send broadcast when token is expired.
 */
public class HXWebRequest extends BaseWebRequest {
    private static String mToken;
    private static final String KEY_TOKEN = "tokenKey";
    private static final String SP_NAME = "config";
    private Context mContext;

    public HXWebRequest(Context context) {
        super(context.getApplicationContext());
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        mToken = sp.getString(KEY_TOKEN, "");
        mContext = context.getApplicationContext();
    }

    protected void saveToken(String token) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_TOKEN, token);
        editor.apply();
        mToken = token;

    }

    protected void clearToken() {
        SharedPreferences sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(KEY_TOKEN);
        editor.apply();
        mToken = null;
    }

    public String getToken() {
        return mToken;
    }

    @Override
    protected Response.ErrorListener getErrorListener(final OnJobFinishListener listener) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (listener != null) {
                    HXWebResult result = new HXWebResult(-1, error.getMessage());
                    if (error.networkResponse != null)
                        result = new HXWebResult(error.networkResponse.statusCode,
                                new String(error.networkResponse.data));
                    listener.onWebCallFinish(false, result);
                }
                if ( !TextUtils.isEmpty(error.getMessage())) {
                    VolleyLog.e(error.getMessage());
                }
            }
        };
    }

    @Override
    protected Response.Listener<String> getSuccessListener(final TypeToken targetType,
                                                           final OnJobFinishListener listener) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!TextUtils.isEmpty(response)) {
                    HXWebResult result = JsonUtils.fromJson(response, HXWebResult.class);
                    if (isNeedSendBroadcast(result)) {
                        clearToken();
                        BroadCastCenter
                                .sendBroadcast(BroadCastCenter.NOTICE_SESSION_EXPIRED, mContext);
                    }
                }
                if (listener == null) {
                    return;
                }
                if (response == null) {
                    listener.onWebCallFinish(false,
                            new HXWebResult<>(0, "未知错误"));
                } else {
                    VolleyLog.d(response);
                    Object result;
                    if (isAutoParseJson()) {
                        result = JsonUtils
                                .fromJson(response,
                                        targetType);
                        if (result == null) {
                            result = JsonUtils.fromJson(response, HXWebResult.class);
                        }
                        if (result == null) {
                            result = new HXWebResult<>(0, response);
                        }
                    } else {
                        result = response;
                    }

                    listener.onWebCallFinish(true,
                            result);
                }

            }

            private boolean isNeedSendBroadcast(HXWebResult result) {
                return result != null && result.getErrorDescript() != null && result
                        .getErrorDescript().getCode() == HXError.SESSION_EXPIRED_CODE;
            }
        };
    }

    @Override
    protected Map<String, String> getHeaders() {
        if (TextUtils.isEmpty(mToken)) return super.getHeaders();
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", "Token " + mToken);
        header.put("Accept", "application/json");
        return header;
    }

    public boolean hasToken() {
        return !TextUtils.isEmpty(getToken());
    }

    protected Context getContext() {
        return mContext;
    }
}
