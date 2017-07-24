package shawn.cn.framelibrary.skin.support;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import shawn.cn.framelibrary.skin.attr.SkinView;
import shawn.cn.framelibrary.skin.callbcak.ISkinChangeListener;
import shawn.cn.framelibrary.skin.utils.SkinConfig;
import shawn.cn.framelibrary.skin.utils.SkinSharedUtil;

/**
 * Created by Shawn on 2017/7/14.
 */

public class SkinManager {

    private static final String TAG = SkinManager.class.getSimpleName();

    private Context mContext;

    public  SkinResource mSkinResource;

    private static SkinManager mInstance = new SkinManager();

    private static Map<ISkinChangeListener,List<SkinView>> mSkinViews = new ArrayMap<>();

    private SkinManager(){}

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context){
        this.mContext = context.getApplicationContext();
        String currSkinPath = SkinSharedUtil.getInstance(mContext).getSkinPath();
        File skinFile = new File(currSkinPath);
        if(!skinFile.exists()){
            SkinSharedUtil.getInstance(mContext).clearSkinPath();
        }
        //initiation
        mSkinResource = new SkinResource(mContext,currSkinPath);
    }

    public int loadSkin(String path){
        //
        File skinFile = new File(path);
        if(!skinFile.exists()){
            return SkinConfig.SKIN_FILE_NOT_EXIST;
        }
        String skinPackageName = mContext.getPackageManager().getPackageArchiveInfo(path,
                PackageManager.GET_ACTIVITIES).packageName;
        if(TextUtils.isEmpty(skinPackageName)){
            return SkinConfig.SKIN_FILE_ERROR;
        }
        String currSkinPath = SkinSharedUtil.getInstance(mContext).getSkinPath();
        if(currSkinPath.equals(path)){
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }
        mSkinResource = new SkinResource(mContext,path);
        for (ISkinChangeListener listener:mSkinViews.keySet()){
            List<SkinView> views = getSkinViews(listener);
            for (SkinView view : views) {
                view.skin();
            }
            listener.changedSkin(mSkinResource);
        }
        saveSkinPath(path);
        return SkinConfig.SKIN_CHANGE_LOAD;
    }

    public int restoreSkin(){
        //判断当前有没有皮肤
        String currentSkinPath = SkinSharedUtil.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NOTHING;
        }
        //当前运行app的路径
        String resPath = mContext.getPackageResourcePath();
        loadSkin(resPath);
        SkinSharedUtil.getInstance(mContext).clearSkinPath();
        return SkinConfig.SKIN_CHANGE_LOAD;
    }

    private void saveSkinPath(String path) {
        if(!TextUtils.isEmpty(path))
            SkinSharedUtil.getInstance(mContext).saveSkinPath(path);
    }

    private List<SkinView> getSkinViews(ISkinChangeListener listener) {
        List<SkinView> skinViews = mSkinViews.get(listener);
        if(skinViews == null){
            skinViews = new ArrayList<>();
            mSkinViews.put(listener,skinViews);
        }
        return skinViews;
    }

    public void registerView(ISkinChangeListener listener, SkinView view){
        List<SkinView> skinViews = getSkinViews(listener);
        if(view != null && view.getView() != null){
            skinViews.add(view);
        }
    }

    public SkinResource getCurrSkinResource(){
        return mSkinResource;
    }

    public void checkSkin(SkinView skinView) {
        if(!TextUtils.isEmpty(SkinSharedUtil.getInstance(mContext).getSkinPath())){
            skinView.skin();
        }
    }

    public void unregister(ISkinChangeListener listener) {
        mSkinViews.remove(listener);
    }
}
