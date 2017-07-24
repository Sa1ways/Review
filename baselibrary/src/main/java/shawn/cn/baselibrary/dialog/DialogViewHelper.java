package shawn.cn.baselibrary.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import shawn.cn.baselibrary.R;

/**
 * Created by Shawn on 2017/7/10.
 */

public class DialogViewHelper {

    private View mContentView;

    private SparseArray<WeakReference<View>> mViews = new SparseArray<>();

    public DialogViewHelper(Context context, int mViewLayoutResId, ViewGroup window) {
        this.mContentView = LayoutInflater.from(context).inflate(mViewLayoutResId,window,false);
    }

    public DialogViewHelper(View view) {
        this.mContentView = view;
    }

    public View getContentView() {
        return mContentView;
    }

    public  <T extends View> T getView(int id){
        WeakReference<View> refer = mViews.get(id);
        if(refer == null){
            View v = mContentView.findViewById(id);
            refer = new WeakReference<>(v);
            mViews.put(id,refer);
        }
        return (T) refer.get();
    }

    public void setText(int id, CharSequence charSequence) {
        TextView textView = getView(id);
        if(textView != null)
            textView.setText(charSequence);
    }


    public void setOnClickListener(int id, View.OnClickListener onClickListener) {
        View v = getView(id);
        if(v != null) v.setOnClickListener(onClickListener);
    }
}
