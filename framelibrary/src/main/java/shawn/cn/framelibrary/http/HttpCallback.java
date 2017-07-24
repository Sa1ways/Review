package shawn.cn.framelibrary.http;

import com.google.gson.Gson;
import java.util.Map;

import shawn.cn.baselibrary.http.EngineCallback;
import shawn.cn.baselibrary.http.HttpUtils;

/**
 * Created by Shawn on 2017/7/11.
 */

public abstract class HttpCallback<T> implements EngineCallback {

    @Override
    public void onPrepareExecute(Map<String, Object> params) {
        //params.put("test","test");
        onPreExecute();
    }

    protected void onPreExecute(){

    }

    @Override
    public void onSuccess(String result) {
        T t = (T) new Gson().fromJson(result,HttpUtils.parseObjectClass(this));
        onSuccess(t);
    }

    public abstract void onSuccess(T result);

}
