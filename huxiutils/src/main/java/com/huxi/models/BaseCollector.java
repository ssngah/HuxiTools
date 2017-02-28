package com.huxi.models;

import android.content.Context;


import com.huxi.huxiutils.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Phyllis on 15-5-14.
 */
public abstract class BaseCollector<T> implements Serializable{

    protected int curPage;
    private boolean hasMoreItems = true;
    /**
     * All the items ;NOT a single page
     */
    private ArrayList<T> items;
    protected static final int PAGE_SIZE = 10;

    public boolean isHasMoreItems() {
        return hasMoreItems;
    }

    protected void setHasMoreItems(boolean hasMoreItems) {
        this.hasMoreItems = hasMoreItems;
    }

    /**
     * @return The T list,if there is no items ,an empty list will be returned;
     */
    public ArrayList<T> getItems() {
        if (items == null) {
            items = new ArrayList<>(1);
        }
        return items;
    }

    public void clear() {
        curPage = 0;
        hasMoreItems = true;
        if (items != null) {
            items.clear();
        }
    }

    public void reloadItems(Context context, OperationCallback<List<T>> callback) {
        curPage = 0;
        hasMoreItems = true;
        if (items != null) {
            items.clear();
        }
        loadMoreItems(context, callback);
    }

    public void loadMoreItems(Context context, final OperationCallback<List<T>> callback) {
        if (!hasMoreItems) {
            HXError error = new HXError(HXError.LOCAL_FAIL_CODE,
                    HXError.MODEL_DOMAIN,
                    R.string.no_more_items);
            if(callback!=null){
                callback.onResultReceived(error, null);
            }
            return;
        }
        HXError error = isAllowToLoad();
        if (error != null) {
            callback.onResultReceived(error, null);
            return;
        }
        doLoadRequest(context, new LoadFinishCallback() {
            @Override
            public void onLoadFinish(HXError error, List arrays, boolean hasNext) {
                if (error != null&&callback!=null) {
                    callback.onResultReceived(error, null);
                    return;
                }
                curPage++;
                if (arrays != null) {
                    List list = getItems();
                    list.addAll(arrays);
                }
                hasMoreItems = hasNext;
                onDatasLoaded();
                if(callback!=null){
                    callback.onResultReceived(null, getItems());
                }


            }
        });
    }

    public void deleteItemAtIndex(final int index, Context context,
                                  final OperationCallback callback) {

    }

    /**
     * When the data is finish loaded,this method will be called;You can override this method if you have some extra operation after we load the data
     */
    protected void onDatasLoaded() {
    }

    /**
     * This method should be override when you have extra requirement to determine if the collector is allowed to load
     *
     * @return Default value is null;
     */
    protected HXError isAllowToLoad() {
        return null;
    }

    protected abstract void doLoadRequest(Context context, LoadFinishCallback callback);

    public interface LoadFinishCallback {
        public void onLoadFinish(HXError error, List arrays, boolean hasNext);
    }

    public static class  CollectorResult<T> {
        int total;
        int page;

//        @SerializedName("has_next")
        private boolean has_next;

        List<T> results;
        public boolean hasNext(){
            return has_next;
        }
    }
}
