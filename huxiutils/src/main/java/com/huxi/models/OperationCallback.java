package com.huxi.models;

/**
 * Created by Phyllis on 15-5-14.
 */
public interface OperationCallback<T> {
    public void onResultReceived(HXError error, T data);

}
