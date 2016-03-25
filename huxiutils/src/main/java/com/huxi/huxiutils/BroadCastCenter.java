package com.huxi.huxiutils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.io.Serializable;
import java.util.Map;


/**
 * Created by Phyllis on 15/6/21.
 */
public class BroadCastCenter {
    public static final String NOTICE_SESSION_EXPIRED = "sessionExpiredNotice";

    public static void sendBroadcast(String actionName,Context context){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(actionName);
        manager.sendBroadcast(intent);
    }

    public static void sendBroadcast(String actionName,Map<String ,Serializable> userInfo,Context context){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        Intent intent = new Intent(actionName);
        for (String key:userInfo.keySet()
             ) {
            intent.putExtra(key,userInfo.get(key));
        }
        manager.sendBroadcast(intent);
    }

    public static void sendBroadcast(Context context,Intent intent){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.sendBroadcast(intent);
    }
    public static void registerBroadcast(String actionName,Context context,BroadcastReceiver receiver){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter(actionName);
        manager.registerReceiver(receiver, filter);
    }
    public static void registerBroadcast(String[] actions,Context context,BroadcastReceiver receiver){
        if(actions==null||actions.length==0) return;
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        IntentFilter filter = new IntentFilter();
        for(String action:actions){
            filter.addAction(action);
        }
        manager.registerReceiver(receiver, filter);
    }
    public static void unRegisterBroadcast(Context context,BroadcastReceiver receiver){
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(context);
        manager.unregisterReceiver(receiver);
    }
}
