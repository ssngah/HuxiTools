package com.huxi.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.TextView;

import com.huxi.huxiutils.R;
import com.huxi.models.BaseCollector;
import com.huxi.models.HXError;
import com.huxi.models.OperationCallback;
import com.huxi.tools.ProgressHUD;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.ui.DividerItemDecoration;

import java.util.List;

/**
 * Created by Phyllis on 15/12/25.
 * A base fragment for refresh and load more list.The list will refresh the page when we are successful to get data only if your adapter have the
 * same refrerence with the one return by BaseCollector.getItems().
 */
public abstract class LoadMoreListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, UltimateRecyclerView.OnLoadMoreListener {
    private View containerView;
    private UltimateRecyclerView mRecyclerView;
    private BaseCollector mCollector;
    private RecyclerView.Adapter mAdapter;
    private Dialog hud;
    private View emptyView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (containerView == null) {
            containerView = inflater
                    .inflate(R.layout.activity_ultimate_recyclerview, container, false);
            configRecyclerView();
        } else {
            ViewParent parent = containerView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(containerView);
            }
        }
        return containerView;
    }

    private void showEmptyView() {
        if (emptyView == null) {
            ViewStub emptyViewStub = (ViewStub) containerView.findViewById(R.id.stub_empty_view);
            emptyView = emptyViewStub.inflate();
            TextView textView = (TextView) emptyView.findViewById(R.id.txt_empty_data);
            textView.setText(getEmptyViewText());
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void configRecyclerView() {
        mRecyclerView = (UltimateRecyclerView) containerView.findViewById(R.id.ultimate_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(getItemDecoration());
        mRecyclerView.enableDefaultSwipeRefresh(true);
        mRecyclerView.setDefaultOnRefreshListener(this);
        mRecyclerView.setOnLoadMoreListener(this);
        loadData();
    }

    private void loadData() {
        mCollector = getCollector();
        if (!mCollector.isHasMoreItems()) return;
        if (hud == null) {
            hud = ProgressHUD.show(getActivity(), "");
        } else {
            hud.show();
        }
        mCollector.loadMoreItems(getContext(), new OperationCallback<List>() {
            @Override
            public void onResultReceived(HXError error, List data) {
                if (hud != null && hud.isShowing()) {
                    hud.dismiss();
                }
                mRecyclerView.setRefreshing(false);
                if (error != null) {
                    ProgressHUD.showError(getContext(), error.getReason(getContext()), 3000);
                } else {
                    if (  showEmptyViewWhenNoData()) {
                        if((data == null || data.isEmpty())) {
                            showEmptyView();
                        }else{
                            hideEmptyView();
                        }
                    }
                    refreshList();
                    onFinishDataLoaded();
                }
            }
        });
    }

    private void refreshList() {

        if (mAdapter == null) {
            mAdapter = getAdapter(mCollector.getItems());
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        if (mCollector.isHasMoreItems()) {
            mRecyclerView.enableLoadmore();
        } else {
            mRecyclerView.disableLoadmore();
        }
    }

    private void hideEmptyView() {
        if (emptyView != null ) {
            emptyView.setVisibility(View.GONE);
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
    }

    @Override
    public void onRefresh() {
        if (mCollector == null) return;
        mCollector.clear();
        loadData();
    }

    @Override
    public void loadMore(int itemsCount, int maxLastVisiblePosition) {
        loadData();
    }

    protected boolean showEmptyViewWhenNoData() {
        return false;
    }

    protected String getEmptyViewText() {
        return getString(R.string.no_data);
    }

    //hook
    protected void onFinishDataLoaded() {


    }

    //////////////////////////////////
    protected abstract BaseCollector getCollector();

    /**
     * This method will be called when we need to config the recyclerView
     *
     * @param items the items retruen from collector.getItems();
     * @return The adapter you are going to use for recyclerview.
     */
    protected abstract RecyclerView.Adapter getAdapter(List items);


}
