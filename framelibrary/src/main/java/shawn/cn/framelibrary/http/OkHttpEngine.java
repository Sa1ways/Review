package shawn.cn.framelibrary.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shawn.cn.baselibrary.http.EngineCallback;
import shawn.cn.baselibrary.http.IEngine;
import shawn.cn.framelibrary.bean.CacheData;
import shawn.cn.framelibrary.db.DaoSupportFactory;
import shawn.cn.framelibrary.db.IDaoSupport;

/**
 * Created by Shawn on 2017/7/11.
 */

public class OkHttpEngine implements IEngine {

    public static final String TAG = OkHttpEngine.class.getSimpleName();

    private static OkHttpClient mClient = new OkHttpClient();

    private static final Handler mHandler = new Handler(Looper.getMainLooper());

    private static final IDaoSupport<CacheData> mDaoSupport =
            DaoSupportFactory.getFactory().getDaoSupport(CacheData.class);

    @Override
    public void post(final boolean sCache, Context context, String url, Map<String, Object> params, final EngineCallback callback) {
        final String requestUrl = joinParams(url, params);
        Log.i(TAG, requestUrl);
        if (sCache) {
            String resultJson = getCacheFromDatabase(requestUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                callback.onSuccess(resultJson);
            }
        }
        //
        RequestBody body = appendBody(params);
        Request request = new Request.Builder()
                .url(requestUrl)
                .tag(context)
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //workerThread
                String result = response.body().string();
                if (sCache) {
                    String resultJson = getCacheFromDatabase(requestUrl);
                    if (!result.equals(resultJson)) {
                        callback.onSuccess(result);
                        saveCache(requestUrl, result);
                    }
                } else {
                    callback.onSuccess(result);
                }

            }

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }
        });

    }

    @Override
    public void get(final boolean sCache, Context context, String url, Map<String, Object> params, final EngineCallback callback) {
        final String requestUrl = joinParams(url, params);
        Log.i(TAG, requestUrl);
        if (sCache) {
            String resultJson = getCacheFromDatabase(requestUrl);
            if (!TextUtils.isEmpty(resultJson)) {
                callback.onSuccess(resultJson);
            }
        }
        //
        Request.Builder requestBuilder = new Request.Builder().url(requestUrl).tag(context);
        requestBuilder.method("GET", null);
        Request request = requestBuilder.build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (sCache) {
                    String resultJson = getCacheFromDatabase(requestUrl);
                    if (!result.equals(resultJson)) {
                        Log.i(TAG, "onResponse: diff");
                        callback.onSuccess(result);
                        saveCache(requestUrl, result);
                    }else{
                        Log.i(TAG, "onResponse: same");
                    }
                } else {

                    callback.onSuccess(result);
                }
               /* mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(result);
                    }
                });*/
            }
        });
    }

//================================ util method ================================

    private void saveCache(String keyUrl, String json) {
        mDaoSupport.delete("mKey=?", keyUrl);
        mDaoSupport.insert(new CacheData(keyUrl, json));
    }

    private String getCacheFromDatabase(String urlKey) {
        String cache = null;
        List<CacheData> cacheData = mDaoSupport.getQuerySupport()
                .selection("mKey=?")
                .selectionArgs(urlKey)
                .query();
        if (cacheData.size() != 0) {
            CacheData data = cacheData.get(0);
            cache = data.getResultJson();
        }
        return cache;
    }

    private String joinParams(String url, Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return url;
        }
        StringBuffer buffer = new StringBuffer(url);
        if (!url.contains("?")) {
            buffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                buffer.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            buffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        return buffer.toString();
    }

    private RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    // handle File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody
                            .create(MediaType.parse(guessMineType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {
                    try {
                        List<File> fileList = (List<File>) value;
                        for (int i = 0; i < fileList.size(); i++) {
                            File file = fileList.get(i);
                            builder.addFormDataPart(key, file.getName(), RequestBody
                                    .create(MediaType.parse(guessMineType(file.getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }

    private String guessMineType(String absolutePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(absolutePath);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}
