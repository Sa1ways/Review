package shawn.cn.framelibrary;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import shawn.cn.baselibrary.navigation.AbsNavigationBar;

/**
 * Created by Shawn on 2017/7/11.
 */

public class DefaultNavigationBar extends AbsNavigationBar<DefaultNavigationBar.Builder.NavigationParams> {


    public DefaultNavigationBar(Builder.NavigationParams params) {
        super(params);
    }

    @Override
    public int bindLayoutId() {
        return R.layout.title_bar;
    }

    @Override
    public void applyView() {
        setText(R.id.tv_title,mParams.mTitle);
        setText(R.id.tv_left,mParams.mLeftText);
        setText(R.id.tv_right,mParams.mRightText);
        setImageRes(R.id.iv_left,mParams.mLeftRes);
        setImageRes(R.id.iv_right,mParams.mRightRes);
        setListeners();
    }

    public static class Builder extends AbsNavigationBar.Builder{

        private NavigationParams P;

        public Builder(Context context, ViewGroup parent) {
            super(context, parent);
            P = new NavigationParams(context,parent);
        }

        public Builder setTitle(String text){
            P.mTitle = text;
            return this;
        }

        public Builder setRightText(String text){
            P.mRightText = text;
            return this;
        }

        public Builder setRightIcon(int rightRes){
            P.mRightRes = rightRes;
            return this;
        }

        public Builder setLeftText(String text){
            P.mLeftText = text;
            return this;
        }

        public Builder setLeftIcon(int leftRes){
            P.mLeftRes = leftRes;
            return this;
        }

        public Builder setClickListener(int viewId,View.OnClickListener listener){
            P.mListeners.put(viewId,listener);
            return this;
        }

        @Override
        public DefaultNavigationBar create() {
            return new DefaultNavigationBar(P);
        }

        public class NavigationParams extends AbsNavigationBar.Builder.AbsNavigationParams{

            public String mTitle;
            public String mRightText;
            public int mRightRes;
            public String mLeftText;
            public int mLeftRes;

            public NavigationParams(Context mContext, ViewGroup mParent) {
                super(mContext, mParent);
            }
        }
    }
}
