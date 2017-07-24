package cn.shawn.view.recyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by root on 17-6-22.
 */

public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String TAG = WrapperAdapter.class.getSimpleName();

    private RecyclerView.Adapter mAdapter;

    private SparseArray<View> mHeaders = new SparseArray<>();

    private SparseArray<View> mFooters = new SparseArray<>();

    private  int HEADER_INDEX_START = 0x1000;

    private  int FOOTER_INDEX_START = 0x2000;

    public WrapperAdapter(RecyclerView.Adapter adapter) {
        this.mAdapter = adapter;
    }

    public void clearInnerData(){
        if(mAdapter instanceof BaseMultiAdapter){
            ((BaseMultiAdapter) mAdapter).mData.clear();
        }
        notifyDataSetChanged();
    }

    public int getHeadersCount(){
        return mHeaders.size();
    }

    public int getFooterCount(){
        return mFooters.size();
    }

    public void addHeaderView(View view) {
        if (view == null) throw new IllegalArgumentException("the header view can not be null");
        if (mHeaders.indexOfValue(view) < 0) {
            mHeaders.put(HEADER_INDEX_START ++, view);
        }
        notifyDataSetChanged();
    }

    public void addFooterView(View view) {
        if (view == null) throw new IllegalArgumentException("the footer view can not be null");
        if (mFooters.indexOfValue(view) < 0) {
            mFooters.put(FOOTER_INDEX_START ++, view);
        }
        notifyDataSetChanged();
    }

    public void removeHeaderView(View view){
        if(mHeaders.indexOfValue(view) < 0) return;
        mHeaders.removeAt(mHeaders.indexOfValue(view));
        notifyDataSetChanged();
    }

    public void removeFooterView(View view){
        if(mFooters.indexOfValue(view) < 0) return;
        mFooters.removeAt(mFooters.indexOfValue(view));
        notifyDataSetChanged();

    }

    private boolean isHeaderForType(int viewType) {
        return mHeaders.indexOfKey(viewType) < 0 ? false : true;
    }

    public boolean isFooterForType(int viewType) {
        return mFooters.indexOfKey(viewType) < 0 ? false : true;
    }

    private boolean isHeaderType(int position) {
        return position < mHeaders.size() ? true : false;
    }

    private boolean isFooterType(int position) {
        int adjustPosition = position - mHeaders.size() - mAdapter.getItemCount();
        return adjustPosition >= 0 ? true : false;
    }

    private int getHeaderType(int position) {
        return mHeaders.keyAt(position);
    }

    private int getFooterType(int position) {
        int adjustPosition = position - mHeaders.size() - mAdapter.getItemCount();
        return mFooters.keyAt(adjustPosition);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderType(position)) {
            return getHeaderType(position);
        } else if (isFooterType(position)) {
            return getFooterType(position);
        }
        return mAdapter.getItemViewType(position - mHeaders.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderForType(viewType)) {
            return new RecyclerView.ViewHolder(mHeaders.get(viewType)) {
            };
        } else if (isFooterForType(viewType)) {
            return new RecyclerView.ViewHolder(mFooters.get(viewType)) {
            };
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isFooterType(position) || isHeaderType(position)) {
            return;
        };
        mAdapter.onBindViewHolder(holder, position - mHeaders.size());
    }

    @Override
    public int getItemCount() {
        return mHeaders.size()+mFooters.size()+mAdapter.getItemCount();
    }

    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter =
                            isHeaderType(position) || isFooterType(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderType(position) || isFooterType(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }
}
