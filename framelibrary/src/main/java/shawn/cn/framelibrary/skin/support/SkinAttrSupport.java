package shawn.cn.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shawn.cn.framelibrary.skin.attr.SkinAttr;
import shawn.cn.framelibrary.skin.attr.SkinType;

/**
 * Created by Shawn on 2017/7/14.
 */

public class SkinAttrSupport {

    private static final String TAG = SkinAttrSupport.class.getSimpleName();

    public static List<SkinAttr> getSkinViewAttrs(Context context, AttributeSet attrs) {
        //background src textColor
        List<SkinAttr> skinAttrs = new ArrayList<>();
        int count = attrs.getAttributeCount();
        for (int i = 0; i < count; i++) {
            //获取名称
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
           // Log.i(TAG, "getSkinViewAttrs: "+attrName+" --- "+attrValue);
            SkinType skinType = getSkinType(attrName);
            if(skinType != null){
                String resName = getName(context,attrValue);
                if(TextUtils.isEmpty(resName))
                    continue;
                SkinAttr attr = new SkinAttr(resName,skinType);
                skinAttrs.add(attr);
            }
        }
        return skinAttrs;
    }

    private static String getName(Context context, String attrValue) {
        Log.i(TAG, "getName: "+attrValue);
        if("@null".equals(attrValue))
            return null;
        if(attrValue.startsWith("@")){
            attrValue = attrValue.substring(1);
            Log.i(TAG, "getName: "+attrValue);
            int resId = Integer.parseInt(attrValue);
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if(skinType.getResName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
