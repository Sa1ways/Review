package cn.shawn.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.shawn.view.R;

/**
 * Created by Shawn on 2017/7/23.
 */

public class Banner extends FrameLayout  {

    public static final String TAG = Banner.class.getSimpleName();

    private int mDecorationBgColor;

    private BannerAdapter mBannerAdapter;

    private int mPageSize;

    private ViewPager mViewPager;

    private LinearLayout mDecorationLayout;

    private Drawable mNormalIndicatorDrawable;

    private Drawable mCurrentIndicatorDrawable;

    public void setOnPageClickListener(OnPageClickListener listener) {
        this.mBannerAdapter.setOnPageClickListener(listener);
    }

    public Banner(@NonNull Context context) {
        this(context, null);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        initView();
    }

    public static void setUpImageLoader(BannerImageLoader loader){
        BannerAdapter.mImageLoader = loader;
    }

    private void init(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.Banner);
        mDecorationBgColor = array.getColor(R.styleable.Banner_decorationBgColor,Color.TRANSPARENT);
        mNormalIndicatorDrawable = array.getDrawable(R.styleable.Banner_normalIndicatorDrawable);
        mCurrentIndicatorDrawable = array.getDrawable(R.styleable.Banner_currentIndicatorDrawable);
        array.recycle();
    }

    private void initView() {
        // add viewPager into banner
        mViewPager = new ViewPager(getContext());
        mBannerAdapter= new BannerAdapter(getContext());
        mBannerAdapter.onAttachToViewPager(mViewPager);
        mBannerAdapter.setOnPageSwitchListener(mOnPageSwitchListener);
        mViewPager.setAdapter(mBannerAdapter);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT);
        addView(mViewPager, params);
    }

    public Banner setNormalIndicatorDrawable(Drawable drawable) {
        this.mNormalIndicatorDrawable = drawable;
        return this;
    }

    public Banner setCurrentIndicatorDrawable(Drawable drawable) {
        this.mCurrentIndicatorDrawable = drawable;
        return this;
    }

    public Banner setData(String...urls){
        this.mPageSize = urls.length;
        this.mBannerAdapter.setUrlData(urls);
        generateDecoration();
        return this;
    }

    public Banner setData(int...resources){
        this.mPageSize = resources.length;
        this.mBannerAdapter.setResData(resources);
        generateDecoration();
        return this;
    }

    private void generateDecoration() {
        if(mDecorationLayout != null){
            mDecorationLayout.removeAllViews();
        }else{
            mDecorationLayout = new LinearLayout(getContext());
            mDecorationLayout.setGravity(Gravity.CENTER);
            mDecorationLayout.setOrientation(LinearLayout.HORIZONTAL);
            mDecorationLayout.setBackgroundColor(mDecorationBgColor);
            // add decoration into banner
            FrameLayout.LayoutParams decorParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT);
            decorParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            decorParams.gravity = Gravity.BOTTOM;
            addView(mDecorationLayout, decorParams);
        }
        // generate circle
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = dp2px(6);
        params.width = dp2px(6);
        params.setMargins(dp2px(4),dp2px(4),dp2px(4),dp2px(4));
        for (int i = 0; i < mPageSize; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable( i==0?mCurrentIndicatorDrawable:mNormalIndicatorDrawable);
            mDecorationLayout.addView(iv, params);
        }
    }

    private void adjustIndicator(int position){
        for (int i = 0; i < mDecorationLayout.getChildCount(); i++) {
            ImageView iv = (ImageView) mDecorationLayout.getChildAt(i);
            iv.setImageDrawable(position == i?mCurrentIndicatorDrawable :mNormalIndicatorDrawable);
        }
    }

    private int dp2px(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,
                getResources().getDisplayMetrics());
    }

    private OnPageSwitchListener mOnPageSwitchListener = new OnPageSwitchListener() {
        @Override
        public void onChange(int currPage) {
            adjustIndicator(currPage);
        }
    };

    public interface OnPageClickListener{
        void onClick(int currPage);
    }

    public interface OnPageSwitchListener{
       void onChange(int currPage);
    }

}
