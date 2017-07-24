package shawn.cn.baselibrary.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Shawn on 2017/7/11.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        initTitle();
        initView();
        initData();
        setListeners();
    }

    protected abstract int getContentView();

    protected abstract void initView();

    protected abstract void initTitle();

    protected abstract void initData();

    protected abstract void setListeners();
}
