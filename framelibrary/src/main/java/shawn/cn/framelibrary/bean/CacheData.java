package shawn.cn.framelibrary.bean;

/**
 * Created by Shawn on 2017/7/13.
 */

public class CacheData {

    private String mKey;

    private String mResultJson;

    public CacheData() {
    }

    public CacheData(String key, String resultJson) {
        this.mKey = key;
        this.mResultJson = resultJson;
    }

    public String geKey() {
        return mKey;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getResultJson() {
        return mResultJson;
    }

    public void setResultJson(String mResultJson) {
        this.mResultJson = mResultJson;
    }

    @Override
    public String toString() {
        return "CacheData{" +
                "mKey='" + mKey + '\'' +
                ", mResultJson='" + mResultJson + '\'' +
                '}';
    }
}
