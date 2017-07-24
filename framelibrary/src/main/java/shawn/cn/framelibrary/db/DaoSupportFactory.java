package shawn.cn.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import java.io.File;

/**
 * Created by Shawn on 2017/7/12.
 */

public class DaoSupportFactory {

    public static final String TAG =DaoSupportFactory.class.getSimpleName();

    public static DaoSupportFactory mInstance;

    private SQLiteDatabase mSqLiteDatabase;

    private DaoSupportFactory() {
        //把数据库放到内存卡里面
        //判断是否有内存卡 动态权限
        File dbRoot = new File(Environment.getExternalStorageDirectory(),
                "nhdz"+File.separator+"database");
        if(!dbRoot.exists()){
            dbRoot.mkdirs();
        }
        File dbFile = new File(dbRoot,"nhdz.db");
        mSqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile,null);
    }

    public static DaoSupportFactory getFactory(){
        if(mInstance == null){
            synchronized (DaoSupportFactory.class){
                if(mInstance == null){
                    mInstance = new DaoSupportFactory();
                }
            }
        }
        return mInstance;
    }

    public  <T> IDaoSupport<T> getDaoSupport(Class<T> clz){
        DaoSupport<T> daoSupport = new DaoSupport<>();
        daoSupport.init(mSqLiteDatabase,clz);
        return daoSupport;
    }

}
