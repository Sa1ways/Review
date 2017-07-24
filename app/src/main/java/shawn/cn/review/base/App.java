package shawn.cn.review.base;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import cn.shawn.view.banner.Banner;
import cn.shawn.view.banner.BannerImageLoader;
import shawn.cn.baselibrary.base.BaseApplication;
import shawn.cn.baselibrary.http.HttpUtils;
import shawn.cn.framelibrary.http.OkHttpEngine;
import shawn.cn.framelibrary.skin.support.SkinManager;
import shawn.cn.review.R;

/**
 * Created by Shawn on 2017/7/10.
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        HttpUtils.init(new OkHttpEngine());
        SkinManager.getInstance().init(mInstance);
        Banner.setUpImageLoader(new BannerImageLoader() {

            @Override
            public <T extends ImageView> void loadImage(Context context, String url, T iv) {
                Glide.with(context).load(url).placeholder(R.mipmap.dy_loading).into(iv);
            }

            @Override
            public <T extends ImageView> void loadImage(Context context, int resource, T iv) {
                Glide.with(context).load(resource).placeholder(R.mipmap.dy_loading).into(iv);
            }

        });
    }
}
