package cn.shawn.view.recyclerview.adapter;

import android.os.Handler;

import java.util.List;

/**
 * Created by root on 17-6-4.
 */
public abstract class MultiTypeAdapter<T> extends BaseMultiAdapter<T> {

    public MultiTypeAdapter() {
        addItemTypeDelegate();
    }

    public void showData(List<T> data){
        this.mData.clear();
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public void appendData(List<T> data){
        int rangeL = mData.size();
        this.mData.addAll(data);
        notifyItemRangeInserted(rangeL,data.size());
    }

    public abstract void addItemTypeDelegate();

}
