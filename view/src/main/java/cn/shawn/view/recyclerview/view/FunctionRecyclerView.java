package cn.shawn.view.recyclerview.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import cn.shawn.view.R;
import cn.shawn.view.recyclerview.adapter.WrapperAdapter;
import cn.shawn.view.recyclerview.iinterface.OnNetworkRefreshListener;
import cn.shawn.view.recyclerview.iinterface.RefreshViewCreator;

/**
 * Created by root on 17-6-5.
 */

public abstract class FunctionRecyclerView extends RefreshLoadRecyclerView  {

    private View mEmptyView, mErrorView, mLoadMoreView, mNoMoreView;

    private int mWrapperHeight;

    private boolean sEnableEmptyView = true;

    private OnNetworkRefreshListener mNetworkListener;

    public void setOnNetworkRefreshListener(OnNetworkRefreshListener listener) {
        if (listener != null) this.mNetworkListener = listener;
    }

    public FunctionRecyclerView(Context context) {
        this(context, null);
    }

    public FunctionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FunctionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEnableEmptyView(boolean enableEmptyView) {
        this.sEnableEmptyView = enableEmptyView;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        initFunctionView();
        super.setAdapter(adapter);
        checkNetworkState();
    }

    private void initFunctionView() {
        if (this.getParent() != null) {
            mWrapperHeight = ((View) this.getParent()).getLayoutParams().height;
        }
        mEmptyView = LayoutInflater.from(getContext()).inflate(getEmptyViewLayoutId(), this, false);
        mErrorView = LayoutInflater.from(getContext()).inflate(getErrorViewLayoutId(), this, false);
        mLoadMoreView = LayoutInflater.from(getContext()).inflate(getLoadMoreViewLayoutId(), this, false);
        mNoMoreView = LayoutInflater.from(getContext()).inflate(getNoMoreViewLayoutId(), this, false);
        mErrorView.findViewById(getNetworkRefreshButtonId()).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mNetworkListener != null) mNetworkListener.onNetRefreshClick();
            }
        });
        setRefreshViewCreator(getRefreshViewCreator());
        setLoadMoreView(mLoadMoreView);
        setNoMoreView(mNoMoreView);
    }

    @Override
    protected void onEmptyData(boolean isEmpty) {
        if(!sEnableEmptyView) return;
        if (!isNetworkConnected) {
            setPullRefreshEnable(false);
            mWrapperAdapter.clearInnerData();
            return;
        }
        if (isEmpty) {
            mEmptyView.getLayoutParams().height = mWrapperHeight;
            addFooterView(mEmptyView);
            removeFooterView(mErrorView);
            setPullRefreshEnable(true);
            setLoadMoreEnable(false);
        } else {
            removeFooterView(mEmptyView);
        }
    }

    public void showNetworkState(boolean isConnected) {
        if (isConnected) {
            setPullRefreshEnable(true);
            setLoadMoreEnable(true);
            removeFooterView(mErrorView);
            removeFooterView(mEmptyView);
            return;
        }
        mErrorView.getLayoutParams().height = mWrapperHeight;
        addFooterView(mErrorView);
        removeFooterView(mEmptyView);
        setLoadMoreEnable(false);
        setPullRefreshEnable(false);

        if (getAdapter() instanceof WrapperAdapter) {
            WrapperAdapter wrapperAdapter = (WrapperAdapter) getAdapter();
            wrapperAdapter.clearInnerData();
        }
    }

    public void showNoMoreView(String tips) {
        ((TextView) mNoMoreView.findViewById(R.id.tv_tips)).setText(tips);
        super.showNoMoreView(true);
    }

    public abstract int getEmptyViewLayoutId();

    public abstract int getErrorViewLayoutId();

    public abstract int getLoadMoreViewLayoutId();

    public abstract int getNoMoreViewLayoutId();

    public abstract int getNetworkRefreshButtonId();

    public abstract RefreshViewCreator getRefreshViewCreator();
    
}
