package shawn.cn.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by Shawn on 2017/7/14.
 */

public class SkinAttr {

    private String mResName;

    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName =resName;
        this.mType = skinType;
    }

    public void skin(View view) {
        mType.skin(view, mResName);
    }
}
