package cn.apacal.weblog.comm;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by apacalzhong on 1/20/16.
 */
public class Core {
    private static final String TAG = "Core";

    private static Core mInstance;
    private static Handler mHandler;

    private Date mDate;
    private SimpleDateFormat mDateFormat;


    public CommDB db;
    public static Core instance() {
        if (mInstance == null) {
            mInstance = new Core();
        }
        return mInstance;
    }

    private String mDataPath;

    /**
     * 将runnable post到工作线程
     * @param r
     */
    public void postToWorkThread(Runnable r) {
        mHandler.post(r);
    }

    private Core() {
        db = new CommDB(CommApplication.getContext(), Constans.ConstansProtocol.DB_NAME, null, Constans.ConstansProtocol.VERSION);

        try {
            HandlerThread handlerThread = new HandlerThread("core-work");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());


            mDate = new Date();
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }



        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + Constans.ConstansProtocol.COMPANY + "/" + Constans.ConstansProtocol.PACKNAME);
            if (!file.exists() && !file.mkdirs()) {
                Log.e(TAG, "mkdir fail %s", file.getPath());
            }

            if (file.isDirectory()) {
                mDataPath = file.getPath();
            }

        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }

        if (Util.isNullOrNil(mDataPath)) {
            Log.e(TAG, "mDataPath is null");
            mDataPath = CommApplication.getContext().getApplicationInfo().dataDir;
        }
        Log.d(TAG, "dataPath[%s]", mDataPath);
    }

    public String getApplicationDataDir() {
        return mDataPath;
    }



    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss.SSS
     */
    public String getStringDate() {
        mDate.setTime(System.currentTimeMillis());
        return mDateFormat.format(mDate);
    }


}
