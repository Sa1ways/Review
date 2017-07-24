package shawn.cn.baselibrary.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shawn on 2017/7/11.
 */

public class HttpUtils {

    public static final int GET = 0x12;

    public static final int POST = GET<<2;

    private static IEngine mEngine = null;

    private String mUrl;

    private int mType = GET;

    private Context mContext;

    private Map<String,Object> mParams;

    private boolean sCache = false;

    private HttpUtils(Context context){
        mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context){
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpUtils post(){
        mType = POST;
        return this;
    }

    public HttpUtils get(){
        mType = GET;
        return this;
    }

    public HttpUtils isCache(boolean isCache){
        this.sCache = isCache;
        return this;
    }

    public HttpUtils addParam(String key,Object value){
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String,Object> params){
        mParams.putAll(params);
        return this;
    }

    public void execute(){
        execute(null);
    }

    public void execute(EngineCallback callback){
        if(callback == null){
            callback = EngineCallback.DEFAULT_CALLBACK;
        }
        callback.onPrepareExecute(mParams);
       if (mType == GET) {
           get(mUrl,mParams,callback);
       }  else
           post(mUrl,mParams,callback);
    }

    //application 中初始化网络框架
    public static void init(IEngine engine){
        mEngine = engine;
    }

    //切换网络框架
    public HttpUtils exchangeEngine(IEngine engine){
        mEngine = engine;
        return this;
    }


    private void get(String url, Map<String, Object> params, EngineCallback callback) {
        mEngine.get(sCache,mContext,url,params,callback);
    }


    private void post(String url, Map<String, Object> params, EngineCallback callback) {
        mEngine.post(sCache,mContext,url,params,callback);
    }

    public static Class<?> parseObjectClass(Object object){
        Type genType = object.getClass().getGenericSuperclass();
        Type[] types = ((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) types[0];
    }
}
