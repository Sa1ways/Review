package shawn.cn.baselibrary.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Shawn on 2017/7/11.
 */

public interface IEngine {
    // get 请求
    void get(boolean sCache,Context context,String url, Map<String,Object> params, EngineCallback callback);
    // post 请求
    void post(boolean sCache,Context context,String url, Map<String,Object> params,EngineCallback callback);
    // 上传

    // 下载

    // https 添加证书

}
