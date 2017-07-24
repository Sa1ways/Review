package shawn.cn.baselibrary.http;

import java.util.Map;

/**
 * Created by Shawn on 2017/7/11.
 */

public interface EngineCallback {

    void onPrepareExecute(Map<String,Object> params);

    void onSuccess(String result);

    void onError(Exception e);

    final EngineCallback DEFAULT_CALLBACK = new EngineCallback() {
        @Override
        public void onPrepareExecute(Map<String, Object> params) {

        }

        @Override
        public void onSuccess(String result) {

        }

        @Override
        public void onError(Exception e) {

        }
    };
}
