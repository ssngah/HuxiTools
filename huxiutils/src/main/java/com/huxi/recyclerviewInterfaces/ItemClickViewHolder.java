package com.huxi.recyclerviewInterfaces;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Phyllis on 15/12/2.
 * A view holder that supports onItemClick by default.
 */
public class ItemClickViewHolder extends RecyclerView.ViewHolder {
    private OnRecyclerItemClickListerner mListener;
    private OnRecyclerItemLongClickListener mLongClickListener;
    public ItemClickViewHolder(View itemView,OnRecyclerItemClickListerner listerner) {
        super(itemView);
        mListener = listerner;
        if(mListener!=null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(getAdapterPosition(),v);
                }
            });
        }
    }
    public void setOnItemLongClickListener(OnRecyclerItemLongClickListener listener){
        if(listener == null) return;
        mLongClickListener = listener;
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mLongClickListener!=null){
                    return mLongClickListener.onItemLongClickListener(getAdapterPosition(),v);
                }
                return false;
            }
        });
    }
}
