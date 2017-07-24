package cn.shawn.view.recyclerview.utils;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by root on 17-6-4.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViewContainers;

    public ViewHolder(View itemView) {
        super(itemView);
        mViewContainers=new SparseArray<>();
    }

    public View getView(int id){
        View view=mViewContainers.get(id);
        if(view == null){
            view = itemView.findViewById(id);
            mViewContainers.put( id , view);
        }
        return view;
    }

    public void setText(int id, String text){
        TextView textView = (TextView) getView(id);
        textView.setText(text);
    }

}
