package shawn.cn.framelibrary.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shawn on 2017/7/13.
 */

public class QuerySupport<T> {

    private Class<?> mClazz;

    private SQLiteDatabase mSQLiteDatabase;

    public QuerySupport(Class<?> clazz, SQLiteDatabase sQLiteDatabase) {
        this.mClazz = clazz;
        this.mSQLiteDatabase = sQLiteDatabase;
    }

    private String[] mColumns;

    private String mSelectionClause;

    private String[] mSelectionArgs;

    private String mGroupBy;

    private String mHaving;

    private String mOrderBy;

    public QuerySupport<T> columns(String[] columns){
        this.mColumns = columns;
        return this;
    }

    public QuerySupport<T> selection(String selectionClause){
        this.mSelectionClause = selectionClause;
        return this;
    }

    public QuerySupport<T> selectionArgs(String...selectionArgs){
        this.mSelectionArgs = selectionArgs;
        return this;
    }

    public QuerySupport<T> groupBy(String groupBy){
        this.mGroupBy = groupBy;
        return this;
    }

    public QuerySupport<T> having(String having){
        this.mHaving = having;
        return this;
    }

    public QuerySupport<T> orderBy(String orderBy){
        this.mOrderBy = orderBy;
        return this;
    }

    public void resetArgs(){
        this.mColumns = null;
        this.mSelectionArgs = null;
        this.mSelectionArgs = null;
        this.mOrderBy = null;
        this.mGroupBy = null;
        this.mHaving = null;
    }

    public List<T> query() {
        mSQLiteDatabase.beginTransaction();
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClazz),
                mColumns, mSelectionClause, mSelectionArgs, mGroupBy, mHaving, mOrderBy);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
        return DaoUtil.cursorToList(cursor,mClazz);
    }

    public List<T> queryAll() {
        mSQLiteDatabase.beginTransaction();
        Cursor cursor = mSQLiteDatabase.query(DaoUtil.getTableName(mClazz),
                null, null, null, null, null, null);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
        return DaoUtil.cursorToList(cursor,mClazz);
    }

}
