package shawn.cn.baselibrary.base;

import android.app.Application;

import shawn.cn.baselibrary.utils.ExceptionCrashHandler;

/**
 * Created by Shawn on 2017/7/12.
 */

public class BaseApplication extends Application {

    public static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        ExceptionCrashHandler.getInstance().init(this);
    }
}
