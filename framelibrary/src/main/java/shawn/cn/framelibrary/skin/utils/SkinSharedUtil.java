package shawn.cn.framelibrary.skin.utils;

import android.content.Context;

/**
 * Created by Shawn on 2017/7/16.
 */

public class SkinSharedUtil {

    private static SkinSharedUtil mInstance;

    private Context mContext;

    private SkinSharedUtil(Context context){
        this.mContext = context.getApplicationContext();
    }

    public static SkinSharedUtil getInstance(Context context){
        if(mInstance == null){
            synchronized (SkinSharedUtil.class){
                if(mInstance == null){
                    mInstance = new SkinSharedUtil(context);
                }
            }
        }
        return mInstance;
    }

    public void saveSkinPath(String path){
        mContext.getSharedPreferences(SkinConfig.SKIN_CONFIG_NAME,
               Context.MODE_PRIVATE).edit().putString(SkinConfig.SKIN_PATH,path).commit();
    }

    public String getSkinPath(){
        return mContext.getSharedPreferences(SkinConfig.SKIN_CONFIG_NAME,
                Context.MODE_PRIVATE).getString(SkinConfig.SKIN_PATH,"");
    }


    public void clearSkinPath() {
        saveSkinPath("");
    }
}
