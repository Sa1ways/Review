package shawn.cn.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Shawn on 2017/7/12.
 */

public class DaoSupport<T> implements IDaoSupport<T> {

    //反射 activity的创建 View的创建
    //反射在一定程度上回影响性能
    public static final String TAG = "DaoSupport";

    private static final ExecutorService ASYNC_EXECUTOR = Executors.newSingleThreadExecutor();

    private SQLiteDatabase mSQLiteDatabase;

    private Class<?> mClazz;

    private QuerySupport<T> mQuerySupport;

    @Override
    public void init(SQLiteDatabase sqLiteDatabase, Class<?> clz) {
        this.mSQLiteDatabase = sqLiteDatabase;
        this.mClazz = clz;
        //create table if not exists Person ( id integer primary key autoincrement ,
        StringBuffer sb = new StringBuffer();
        sb.append("create table if not exists ").append(DaoUtil.getTableName(clz))
                .append(" (id integer primary key autoincrement, ");
        Field[] fields = mClazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String name = field.getName();
            if (name.contains("$change") || name.contains("serialVersionUID")) continue;
            String type = field.getType().getSimpleName();
            sb.append(name + " ").append(DaoUtil.getColumnString(type)).append(", ");
        }
        sb.replace(sb.length() - 2, sb.length(), ")");
        mSQLiteDatabase.execSQL(sb.toString());
    }

    @Override
    public void insert( T target) {
        ContentValues values = DaoUtil.parseToValues(target, mClazz);
        mSQLiteDatabase.insert(DaoUtil.getTableName(mClazz), null, values);
    }


    @Override
    public void insert(final List<T> data) {
        mSQLiteDatabase.beginTransaction();
        ASYNC_EXECUTOR.execute(new Runnable() {
            @Override
            public void run() {
                for (T t : data) {
                    insert(t);
                }
            }
        });
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public QuerySupport<T> getQuerySupport() {
        if(mQuerySupport == null){
            mQuerySupport = new QuerySupport<>(mClazz,mSQLiteDatabase);
        }
        return mQuerySupport;
    }

    @Override
    public void delete(String whereClause, String[] whereArgs) {
        mSQLiteDatabase.delete(DaoUtil.getTableName(mClazz), whereClause, whereArgs);
    }

    @Override
    public void update(T obj, String whereClause, String[] whereArgs) {
        mSQLiteDatabase.update(DaoUtil.getTableName(mClazz), DaoUtil.parseToValues(obj,mClazz)
                , whereClause, whereArgs);
    }

}
