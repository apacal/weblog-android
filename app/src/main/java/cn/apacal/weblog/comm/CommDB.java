package cn.apacal.weblog.comm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import cn.apacal.weblog.codegen.table.Msg;

import java.util.HashMap;

/**
 * Created by apacalzhong on 1/20/16.
 */
public class CommDB extends SQLiteOpenHelper {
    protected static final String TAG = "CommDB";

    public interface IFactory {
        String[] getSQLs();
    }

    private static HashMap<Integer, IFactory> baseDBFactories = new HashMap<Integer, IFactory>();

    private SQLiteDatabase mWriteDb;


    private void initSql() {
        try {
            baseDBFactories.put(Msg.getSql().hashCode(), new IFactory() {
                @Override
                public String[] getSQLs() {
                    return new String[]{Msg.getSql()
                    };
                }
            });

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
    }

    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        return mWriteDb.update(tableName, values, whereClause, whereArgs);
    }

    public void execSQL(String sql) {
        mWriteDb.execSQL(sql);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mWriteDb.rawQuery(sql, selectionArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return mWriteDb.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values ) {
        try {
            return mWriteDb.insert(table, nullColumnHack, values);
        }catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            return -1;
        }
    }

    public long replace(String table, String nullColumnHack, ContentValues values ) {
        return mWriteDb.replace(table, nullColumnHack, values);
    }



    public CommDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        initSql();
        mWriteDb = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int key : baseDBFactories.keySet()) {
            for(String sql : baseDBFactories.get(key).getSQLs()) {
                Log.d(TAG, "create table sql %s", sql);
                db.execSQL(sql);
            }
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
