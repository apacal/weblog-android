package cn.apacal.weblog.comm;

import android.app.Application;
import android.content.Context;

/**
 * Created by apacalzhong on 1/20/16.
 */
public class CommApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }
}
