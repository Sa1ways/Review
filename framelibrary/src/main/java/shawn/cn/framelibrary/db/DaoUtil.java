package shawn.cn.framelibrary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.util.ArrayMap;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by Shawn on 2017/7/12.
 */

public class DaoUtil {

    private static final Map<String,Method> mPutMethods = new ArrayMap<>();

    public static String getTableName(Class<?> clz){
        return clz.getSimpleName();
    }

    public static String getColumnString(String type){
        if(type.contains("int")){
            type = "integer";
        }else if(type.contains("String")){
            type = "text";
        }else if(type.contains("char")){
            type = "varchar";
        }else if(type.contains("boolean")){
            type = "boolean";
        }else if(type.contains("float")){
            type = "float";
        }else if(type.contains("double")){
            type = "double";
        }else if(type.contains("long")){
            type = "long";
        }else if(type.contains("short")){
            type = "integer";
        }
        return type;
    }

    public static ContentValues parseToValues(Object target,Class<?> clazz) {
        ContentValues values = new ContentValues();
        Field[] fields = clazz.getDeclaredFields();
        // 反射拿到所有成员,遍历之
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                //拿到成员的名字
                String columnName = field.getName();
                if (columnName.contains("$change") || columnName.contains("serialVersionUID"))
                    continue;
                //拿到成员的值
                Object columnValue = field.get(target);
                //反射拿到put()方法
                String fieldTypeName = field.getType().getName();
                Method put = mPutMethods.get(fieldTypeName);
                if (put == null) {
                    put = ContentValues.class.getMethod("put",
                            String.class, columnValue.getClass());
                    mPutMethods.put(fieldTypeName, put);
                }
                /*Method put = ContentValues.class.getMethod("put",
                        String.class,columnValue.getClass());*/
                //执行put方法
                put.invoke(values, columnName, columnValue);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

    public static <T> List<T> cursorToList(Cursor cursor, Class<?> clazz) {
        List<T> result = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            // cursor.getColumnIndex()
            // cursor.getInt();
            do {
                try {
                    T t = (T) clazz.newInstance();
                    Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        String fieldName = field.getName();
                        if (fieldName.contains("$change") || fieldName.contains("serialVersionUID"))
                            continue;
                        int columnIndex = cursor.getColumnIndex(fieldName);
                        if (columnIndex == -1)
                            continue;
                        //获取类型
                        Method getMethod = getTypeMethod(field.getType());
                        Object obj = getMethod.invoke(cursor, columnIndex);
                        if (boolean.class == field.getType() || Boolean.class == field.getType()) {
                            obj = (obj).equals(1) ? true : false;
                        } else if (Date.class == field.getType()) {
                            obj = new Date((Long) obj);
                        } else if (char.class == field.getType() || Character.class == field.getType()) {
                            obj = ((String) obj).charAt(0);
                        }
                        field.set(t, obj);
                    }
                    result.add(t);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    private static Method getTypeMethod(Class<?> type) {
        Method method = null;
        StringBuffer methodName = new StringBuffer("get");
        String typeName = type.getSimpleName();
        if (type.isPrimitive()) {
            typeName = typeName.replace(typeName.substring(0, 1),
                    typeName.substring(0, 1).toUpperCase());
        }
        if ("Char".equals(typeName) || "Character".equals(typeName)) {
            typeName = "String";
        } else if ("Integer".equals(typeName)) {
            typeName = "Int";
        } else if ("Boolean".equals(typeName)) {
            typeName = "Int";
        } else if ("Date".equals(typeName)) {
            typeName = "Long";
        }
        methodName.append(typeName);
        try {
            method = Cursor.class.getMethod(methodName.toString(), int.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}
