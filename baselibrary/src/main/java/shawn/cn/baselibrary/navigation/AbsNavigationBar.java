package shawn.cn.baselibrary.navigation;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Shawn on 2017/7/10.
 */

public abstract class AbsNavigationBar<Params extends AbsNavigationBar.Builder.AbsNavigationParams>
        implements INavigationBar {

    public String TAG = AbsNavigationBar.class.getSimpleName();

    public Params mParams;

    private View mNavigationView;

    public AbsNavigationBar(Params params) {
        this.mParams = params;
        createAndBindView();
    }

    private void createAndBindView() {
        if(mParams.mParent == null){
            Activity activity = (Activity) mParams.mContext;
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            ViewGroup contentParent = (ViewGroup) decorView.findViewById(android.R.id.content);
            Log.i(TAG, "createAndBindView: "+contentParent.getParent().getClass().getSimpleName());
            LinearLayout linearLayout = (LinearLayout) contentParent.getParent();
            //LinearLayout linearLayout = findContentParent( decorView);
            mParams.mParent = linearLayout;
        }
        if(mParams.mParent == null) return;
        mNavigationView = LayoutInflater.from(mParams.mContext).
                inflate(bindLayoutId(), mParams.mParent, false);
        mParams.mParent.addView(mNavigationView, 0);
        applyView();
    }

    private LinearLayout findContentParent(ViewGroup parent){
        if(parent == null) return null;
        ViewGroup topContainer = (ViewGroup) parent.getChildAt(0);
        if(topContainer != null &&LinearLayout.class.getName().equals(
                topContainer.getClass().getName())){
            return (LinearLayout) topContainer;
        }
        return findContentParent(topContainer);
    }

    public void setListeners(){
        int counts = mParams.mListeners.size();
        for (int i = 0; i < counts; i++) {
            getView(mParams.mListeners.keyAt(i)).setOnClickListener(mParams.mListeners.valueAt(i));
        }
    }

    public <T extends View> T getView(int id){
        return (T) mNavigationView.findViewById(id);
    }

    public void setText(int id, String title){
        TextView textView = getView(id);
        textView.setVisibility(!TextUtils.isEmpty(title)?View.VISIBLE:View.GONE);
        if(!TextUtils.isEmpty(title)){
            textView.setText(title);
        }
    }

    public void setImageRes(int id, int res){
        ImageView imageView = getView(id);
        imageView.setVisibility((res != 0)?View.VISIBLE:View.GONE);
        if(res != 0) imageView.setImageResource(res);
    }

    public void setOnClickListener(int id,View.OnClickListener listener){
        getView(id).setOnClickListener(listener);
    }

    public abstract static class Builder{

        public Builder(Context context,ViewGroup parent) {

        }

        public abstract AbsNavigationBar create();

        public static class AbsNavigationParams{

            public Context mContext;
            public ViewGroup mParent;
            public SparseArray<View.OnClickListener> mListeners = new SparseArray<>();

            public AbsNavigationParams(Context mContext, ViewGroup mParent) {
                this.mContext = mContext;
                this.mParent = mParent;
            }
        }
    }

}
