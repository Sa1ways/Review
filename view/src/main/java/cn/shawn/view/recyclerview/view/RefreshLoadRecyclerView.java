package cn.shawn.view.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by root on 17-6-4.
 */

public abstract class RefreshLoadRecyclerView extends RefreshRecyclerView {
    
    private boolean sLoadEnable = true;
    
    private boolean sLoading = false;

    private View mLoadMoreView;
    
    private View mNoMoreView;

    public RefreshLoadRecyclerView(Context context) {
        super(context);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshLoadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public void setLoadMoreEnable(boolean enable){
        Log.i(TAG, "setLoadMoreEnable: "+enable);
        this.sLoadEnable = enable;
        if(sLoadEnable) removeFooterView(mNoMoreView);
    }

    public void showNoMoreView(boolean sShow){
        if(sShow){
            sLoadEnable = false;
            removeFooterView(mLoadMoreView);
            addFooterView(mNoMoreView);
        }
        else removeFooterView(mNoMoreView);
    }

    public void stopLoadingMore(){
        if(!sLoading) return;
        sLoading = false;
        removeFooterView(mLoadMoreView);
    }
    
    protected void setLoadMoreView(View view){
        if(view == null) return;
        this.mLoadMoreView = view;
    }

    protected void setNoMoreView(View view){
        if(view == null) return;
        this.mNoMoreView = view;
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(state == SCROLL_STATE_IDLE && !sLoading && !sRefreshing && sLoadEnable && !canScrollUp()){
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }
            if (layoutManager.getChildCount() > 0
                    && lastVisibleItemPosition >= (layoutManager.getItemCount() - 1)
                    && layoutManager.getItemCount() > layoutManager.getChildCount()){
                sLoading = true;
                addFooterView(mLoadMoreView);
                mWrapperAdapter.notifyDataSetChanged();
                smoothScrollToPosition(mWrapperAdapter.getItemCount()-1);
                if(mRlListener != null) mRlListener.onLoadingMore();
            }
        }
    }

    private boolean canScrollUp(){
        return ViewCompat.canScrollVertically(this,1);
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }
}
