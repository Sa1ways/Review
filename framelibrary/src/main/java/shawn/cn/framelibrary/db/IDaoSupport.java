package shawn.cn.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Shawn on 2017/7/12.
 */

public interface IDaoSupport<T> {

    void init(SQLiteDatabase sqLiteDatabase, Class<?> clz);

    //插入
    void insert(T t);

    //多条插入
    void insert(List<T> t);

    QuerySupport<T> getQuerySupport();

    void delete(String whereClause, String...whereArgs);

    void update(T obj, String whereClause, String...whereArgs);
}
