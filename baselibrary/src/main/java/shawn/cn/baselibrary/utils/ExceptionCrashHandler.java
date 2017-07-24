package shawn.cn.baselibrary.utils;

import android.content.Context;
import android.util.Log;

/**
 * Created by Shawn on 2017/7/10.
 */

public class ExceptionCrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = ExceptionCrashHandler.class.getSimpleName();

    private static ExceptionCrashHandler mInstance;

    private Thread.UncaughtExceptionHandler mDefaultExceptionHandler;

    //获取应用的信息
    private Context mContext;

    private ExceptionCrashHandler(){}

    public static ExceptionCrashHandler getInstance(){
        if(mInstance == null){
            //解决多并发的问题
            synchronized (ExceptionCrashHandler.class){
                if(mInstance == null){
                    mInstance = new ExceptionCrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context;
        Thread.currentThread().setUncaughtExceptionHandler(this);
        mDefaultExceptionHandler = Thread.currentThread().getDefaultUncaughtExceptionHandler();
        Log.i(TAG, "init: "+(mDefaultExceptionHandler==this));
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Log.i(TAG, "uncaughtException: "+e);
        // throwable version
        // 崩溃的消息信息 应用的信息包名 版本号  手机信息
        // 上传问题,但是上传文件不在这里处理,保存日志，应用再次启动上传日志信息
        mDefaultExceptionHandler.uncaughtException(t,e);
    }

}
