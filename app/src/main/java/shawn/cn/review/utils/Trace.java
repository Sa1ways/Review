package shawn.cn.review.utils;

import android.util.Log;

import shawn.cn.review.BuildConfig;

/**
 * Created by Shawn on 2017/7/17.
 */

public class Trace {

    private Trace(){}

    public static final String TRACE_TAG  = "Trace";

    public static void i(CharSequence text){
        if(BuildConfig.DEBUG){
            Log.i(TRACE_TAG, "i: "+text);
        }
    }
}
