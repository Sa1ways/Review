package cn.shawn.view.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import cn.shawn.view.recyclerview.adapter.WrapperAdapter;
import cn.shawn.view.recyclerview.utils.NetworkUtils;

/**
 * Created by root on 17-6-4.
 */

public abstract class WrapperRecyclerView extends RecyclerView {

    protected boolean isNetworkConnected = true;

    protected RecyclerView.Adapter mAdapter;

    protected WrapperAdapter mWrapperAdapter;

    public WrapperRecyclerView(Context context) {
        super(context);
    }

    public WrapperRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapperRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void checkNetworkState(){
        isNetworkConnected = NetworkUtils.isConnected(getContext());
        showNetworkState(isNetworkConnected);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mObserver);
            mAdapter = null;
        }
        if (adapter instanceof WrapperAdapter) {
            mWrapperAdapter = (WrapperAdapter) adapter;
        } else {
            mWrapperAdapter = new WrapperAdapter(adapter);
        }
        mWrapperAdapter.adjustSpanSize(this);
        super.setAdapter(mWrapperAdapter);
        mAdapter = adapter;
        mAdapter.registerAdapterDataObserver(mObserver);
    }


    public void addHeaderView(View headerView) {
        if (getAdapter() == null) throw new IllegalArgumentException("setAdapter first ! ");
        mWrapperAdapter.addHeaderView(headerView);
    }

    public void addFooterView(View footerView ) {
        if (getAdapter() == null) throw new IllegalArgumentException("setAdapter first ! ");
        mWrapperAdapter.addFooterView(footerView);
    }

    public void removeFooterView(View footerView) {
        if (getAdapter() == null) throw new IllegalArgumentException("setAdapter first ! ");
        mWrapperAdapter.removeFooterView(footerView);
    }

    public void removeHeaderView(View headerView) {
        if (getAdapter() == null) throw new IllegalArgumentException("setAdapter first ! ");
        mWrapperAdapter.removeHeaderView(headerView);
    }

    public int getHeadersCount() {
        return mWrapperAdapter == null ? 0 : mWrapperAdapter.getHeadersCount();
    }

    private AdapterDataObserver mObserver = new AdapterDataObserver() {

        @Override
        public void onChanged() {
            if(mAdapter == null) return;
            if(mWrapperAdapter != mAdapter){
                onDataChanged();
                mWrapperAdapter.notifyDataSetChanged();
            }

        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            // 观察者  列表Adapter更新 包裹的也需要更新不然列表的notifyDataSetChanged没效果
            if (mWrapperAdapter != mAdapter) {
                onDataChanged();
                mWrapperAdapter.notifyItemRangeRemoved(getHeadersCount() + positionStart,
                        itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapperAdapter != mAdapter){
                onDataChanged();
                mWrapperAdapter.notifyItemMoved(getHeadersCount()+fromPosition,getHeadersCount() + toPosition);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapperAdapter != mAdapter){
                onDataChanged();
                mWrapperAdapter.notifyItemRangeChanged(getHeadersCount()+positionStart, itemCount);
            }
        }


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mAdapter == null) return;
            if (mWrapperAdapter != mAdapter){
                onDataChanged();
                mWrapperAdapter.notifyItemRangeInserted(getHeadersCount()+positionStart,
                        itemCount);
            }
        }
    };

    public abstract void showNetworkState(boolean isNetworkConnected);

    private void onDataChanged(){
        onEmptyData(mAdapter.getItemCount() == 0);
    }

    protected abstract void onEmptyData(boolean sEmpty);

}
