package shawn.cn.framelibrary.skin.support;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by Shawn on 2017/7/14.
 */

public class SkinResource {

    private static final String TAG = SkinResource.class.getSimpleName();

    private String mResourcePath;

    private Resources mResources;

    private String mPackageName;

    public SkinResource(Context context,String resourcePath) {
        this.mResourcePath = resourcePath;
        Resources superRes = context.getResources();
        try {
            AssetManager manager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(manager, resourcePath);
            mResources = new Resources(manager,
                    superRes.getDisplayMetrics(), superRes.getConfiguration());
            mPackageName = context.getPackageManager().getPackageArchiveInfo(
                    mResourcePath, PackageManager.GET_ACTIVITIES).packageName;
            Log.i(TAG, "SkinResource: "+resourcePath+"   "+mPackageName);
            /*int drawableId = resources.getIdentifier("gtr","drawable","cn.shawn.shawnrvproject");
            Drawable drawable = resources.getDrawable(drawableId);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Drawable getDrawableByName(String resName){
        try{

            int drawableId = mResources.getIdentifier(resName,"drawable",mPackageName);
            Log.i(TAG, "getDrawableByName: "+resName+" --- "+drawableId);
            Drawable drawable = mResources.getDrawable(drawableId);
            return drawable;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public ColorStateList getColorByName(String resName){
        try{
            int colorId = mResources.getIdentifier(resName,"color",mPackageName);
            ColorStateList color = mResources.getColorStateList(colorId);
            return color;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


}
