package shawn.cn.review;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import java.io.File;
import java.lang.reflect.Method;
import cn.shawn.view.banner.Banner;
import shawn.cn.baselibrary.dialog.MyDialog;
import shawn.cn.baselibrary.http.HttpUtils;
import shawn.cn.framelibrary.DefaultNavigationBar;
import shawn.cn.framelibrary.base.BaseSkinActivity;
import shawn.cn.framelibrary.http.HttpCallback;
import shawn.cn.framelibrary.skin.support.SkinManager;
import shawn.cn.review.config.HttpConfig;


public class MainActivity extends BaseSkinActivity implements View.OnClickListener {

    public static final String[] URLS  ={
            "http://upload-images.jianshu.io/upload_images/1590087-f02e0eeed817e576." +
                    "jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "http://upload-images.jianshu.io/upload_images/1590087-3ba4fb3414e1dc28" +
                    ".jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240",
            "http://upload-images.jianshu.io/upload_images/1590087-22972e117b4d5649." +
                    "jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"
    };

    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView iv;

    private ExpandableTextView mExpandTv;

    private Banner mBanner;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        mExpandTv = (ExpandableTextView) findViewById(R.id.expand_text_view);
        mBanner = (Banner) findViewById(R.id.banner);
        mBanner.setCurrentIndicatorDrawable(getResources().getDrawable(R.drawable.indicator_current_vp))
                .setNormalIndicatorDrawable(getResources().getDrawable(R.drawable.indicator_normal_vp))
                .setData(URLS)
                .setOnPageClickListener(new Banner.OnPageClickListener() {
                    @Override
                    public void onClick(int currPage) {
                        Toast.makeText(MainActivity.this,"click "+currPage,Toast.LENGTH_SHORT).show();
                    }
                });

        mExpandTv.setText("穿过镜子与死去昨天\n" +
                "穿过黄昏与黑夜夹缝\n" +
                "你问我\n" +
                "要我怎样地\n" +
                "要我奈何地\n" +
                "去问苍天，去摇动苍天\n" +
                "---------飘着的雪山经幡。\n" +
                ".\n" +
                "要我说么\n" +
                "说给晨曦云霞的云水间\n" +
                "说给十指尖\n" +
                "飘着的雪花天，水寒涧。\n" +
                ".\n" +
                "你问我\n" +
                "我有了经年\n" +
                "经年救不活沉埋的语言\n" +
                "你问我\n" +
                "要我怎样地\n" +
                "要我奈何地\n" +
                "勇气鼓舞坚强摇动苍天\n" +
                "走在黑势致命摧残吞咽。\n" +
                ".\n" +
                "从此，从此，我孤独地\n" +
                "十二个月亮\n" +
                "曾经的，揉进海面叹息\n" +
                "海浪撕碎\n" +
                "寻找海岸枯萎死掉眼泪。\n" +
                ".");
        //View.inflate(this,R.layout.activity_main,null);
        //LayoutInflater.from(this).inflate(R.layout.activity_main,null);
        LayoutInflater.from(this).inflate(R.layout.activity_main,null,false);
        Log.i(TAG, "initView: "+iv);
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, null)
                .setTitle("评论")
                .setLeftIcon(R.mipmap.ic_back)
                .setRightText("发布")
                .setClickListener(R.id.tv_title, this)
                .setClickListener(R.id.iv_left, this)
                .create();
    }

    @Override
    protected void initData() {
        HttpUtils
                .with(this)
                .url(HttpConfig.TEL_INFO)
                .addParam("tel", "18625178301")
                .isCache(true)
                .execute(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        // Log.i(TAG, "onSuccess: " + result);
                    }

                    @Override
                    public void onError(Exception e) {
                        // Log.i(TAG, "onError: " + e.toString());
                    }
                });
    }


    private void readResource() {
        Resources superRes = getResources();
        try {
            AssetManager manager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            addAssetPath.setAccessible(true);
            addAssetPath.invoke(manager, Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "skin.skin1");
            Resources resources = new Resources(manager,
                    superRes.getDisplayMetrics(), superRes.getConfiguration());
            int drawableId = resources.getIdentifier("gtr","drawable","cn.shawn.shawnrvproject");
            Drawable drawable = resources.getDrawable(drawableId);
            iv.setImageDrawable(drawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void setListeners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_pwd:

                break;
            case R.id.get_name:
                break;
            case R.id.tv_skin:
                String path = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator
                        +"red.skin";
                int result = SkinManager.getInstance().loadSkin(path);
                Log.i(TAG, "onClick: "+result);
                break;
            case R.id.tv_restore:
                SkinManager.getInstance().restoreSkin();
                break;
            case R.id.show:
                new MyDialog
                        .Builder(this)
                        .setText(R.id.tv, "hello Shawn")
                        .setClickListener(R.id.tv, this)
                        .setClickListener(R.id.iv, this)
                        .setView(R.layout.dialog_test)
                        .setAnimation(R.style.dialog_scale)
                        .fillWidth()
                        .fromBottom(true)
                        .show();
                break;
            case R.id.tv:
                Toast.makeText(MainActivity.this, "textView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv:
                Toast.makeText(MainActivity.this, "imageView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_left:
                this.finish();
                break;
            case R.id.tv_title:
                readResource();
                break;
            case R.id.tv_jump:
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                break;
        }
    }

}
