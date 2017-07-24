package shawn.cn.framelibrary.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import java.util.List;
import shawn.cn.baselibrary.base.BaseActivity;
import shawn.cn.framelibrary.skin.callbcak.ISkinChangeListener;
import shawn.cn.framelibrary.skin.support.SkinAttrSupport;
import shawn.cn.framelibrary.skin.support.SkinManager;
import shawn.cn.framelibrary.skin.attr.SkinAttr;
import shawn.cn.framelibrary.skin.attr.SkinView;
import shawn.cn.framelibrary.skin.support.AppCompatViewInflater;
import shawn.cn.framelibrary.skin.support.SkinResource;

/**
 * Created by Shawn on 2017/7/14.
 */

public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory,ISkinChangeListener {

    private AppCompatViewInflater mAppCompatViewInflater;

    private String TAG = "BaseSkinActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater,this);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        //创建View
        View view = createView(parent,name,context,attrs);
        //解析属性
        if(view != null){
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinViewAttrs(context,attrs);
            SkinView skinView = new SkinView(view,skinAttrs);
            //统一交给SkinManager管理
            SkinManager.getInstance().registerView(this,skinView);
            //判断是否需要换肤
            SkinManager.getInstance().checkSkin(skinView);
        }
        return view;
    }

    @Override
    public void changedSkin(SkinResource resource) {

    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new AppCompatViewInflater();
        }

        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }
}
