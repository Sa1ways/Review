package shawn.cn.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shawn.cn.framelibrary.skin.support.SkinManager;
import shawn.cn.framelibrary.skin.support.SkinResource;

/**
 * Created by Shawn on 2017/7/14.
 */

public enum SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getCurrSkinResource();
            ColorStateList color = skinResource.getColorByName(resName);
            if(color == null)
                return;
            TextView textView = (TextView) view;
            textView.setTextColor(color);
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getCurrSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable != null){
                ImageView imageView = (ImageView) view;
                imageView.setBackgroundDrawable(drawable);
                return;
            }
            ColorStateList color = skinResource.getColorByName(resName);
            if(color != null){
                view.setBackgroundColor(color.getDefaultColor());
            }
        }
    },SRC("src") {
        @Override
        public void skin(View view, String resName) {
            SkinResource skinResource = getCurrSkinResource();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable != null){
                ImageView imageView = (ImageView) view;
                imageView.setImageDrawable(drawable);
                return;
            }
        }
    };

    private String mResName;

    SkinType(String resName){
        this.mResName = resName;
    }

    private static SkinResource getCurrSkinResource() {
        return SkinManager.getInstance().getCurrSkinResource();
    }

    public abstract void skin(View view, String resName);

    public String getResName() {
        return mResName;
    }
}
