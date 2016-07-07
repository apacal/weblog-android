package cn.apacal.weblog.comm;

import android.database.Cursor;

import cn.apacal.weblog.stg.StgOnChange;

import java.util.ArrayList;

/**
 * Created by apacalzhong on 1/21/16.
 */
public abstract class CommStg<T extends IDBItem> {
    private final static String TAG = "CommStg";
    public abstract String getTableName();

    protected CommDB db;

    public CommStg(CommDB db) {
        this.db  = db;
    }

    public int getCount() {
        String sql = String.format("select count(*) from %s", getTableName());
        Cursor cursor = db.rawQuery(sql, new String[]{});
        if (cursor == null || cursor.getCount() == 0) {
            Log.e(TAG, "getCount fail, cursor is null or count is 0");
            return -1;
        }


        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public long insert(T item) {
        long ret = db.insert(getTableName(), null, item.convertTo());
        Log.d(TAG, "insert item[%s] ret[%d]", item, ret);
        onChange(item, Constans.StgChangeType.STG_CHANGE_TYPE_UPDATE);
        return ret;
    }


    public int update(T item) {
        String sql = String.format("%s=?", item.getPKey());
        return update(item, sql, new String[]{String.valueOf(item.getPKeyValue())});
    }

    public int update(T item, String whereClause, String[] whereArgs) {
        int ret = db.update(getTableName(), item.convertTo(), whereClause, whereArgs);
        Log.d(TAG, "update row[%s], item[%s]", ret, item);
        onChange(item, Constans.StgChangeType.STG_CHANGE_TYPE_UPDATE);
        return ret;
    }

    public int delete(T item) {
        String sql = String.format("%s=?", item.getPKey());
        int ret = db.delete(getTableName(), sql, new String[]{String.valueOf(item.getPKeyValue())});
        Log.d(TAG, "delete sql[%s %s] item[%s], ret[%d]", sql, item.getPKeyValue(), item, ret);
        onChange(item, Constans.StgChangeType.STG_CHANGE_TYPE_DELETE);
        return ret;
    }

    public void execSQL(String sql) {
        db.execSQL(sql);
    }

    private ArrayList<StgOnChange> mChangeListeners =  new ArrayList<>();

    public void addChangeListener(StgOnChange stgOnChange) {
        mChangeListeners.add(stgOnChange);
        Log.d(TAG, "add ChangeListener[%d] [%s]", mChangeListeners.size(), mChangeListeners);
    }

    public void removeChangeListener(StgOnChange stgOnChange) {
        mChangeListeners.remove(stgOnChange);
    }

    protected void onChange(T item, int type) {
        Log.d(TAG, "onChange item[%s] type[%s], mChangeListeners[%d][%s]", item, type, mChangeListeners.size(), mChangeListeners);
        if (mChangeListeners.size() > 0) {
            for (StgOnChange stgOnChange : mChangeListeners) {
                stgOnChange.onChange(item, type);
            }
        }
    }
}
