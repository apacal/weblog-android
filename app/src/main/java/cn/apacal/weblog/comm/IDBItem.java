package cn.apacal.weblog.comm;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by apacalzhong on 1/21/16.
 */
public abstract class IDBItem {

    public abstract void convertFrom(final Cursor cu);

    public abstract ContentValues convertTo();
    public abstract int getPKeyValue();
    public abstract String getPKey();



}
