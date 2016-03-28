package cn.apacal.weblog.comm;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.FileOutputStream;

/**
 * Created by apacalzhong on 3/23/16.
 */
public class LogFileMgr {

    private static Handler mLogFileHandler;
    private static FileOutputStream mLogFileOutputStream;
    private static final String TAG = "LogFileMgr";
    private static LogFileMgr mLogFileMgr;


    public static LogFileMgr instance() {
        if (mLogFileMgr == null) {
            mLogFileMgr = new LogFileMgr();
        }
        return mLogFileMgr;
    }

    private LogFileMgr() {

        HandlerThread logFileHandlerThread = new HandlerThread("log-file-thread");
        logFileHandlerThread.start();
        mLogFileHandler = new Handler(logFileHandlerThread.getLooper());
    }

    public void putLogToFile(final String tag, final String log) {
        mLogFileHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mLogFileOutputStream == null) {
                        mLogFileOutputStream = new FileOutputStream(Core.instance().getApplicationDataDir() + "/" + Constans.ConstansProtocol.LOG_FILE);
                    }
                    String str = String.format("%s\t%s\t%s\n", Core.instance().getStringDate(), tag, log);

                    mLogFileOutputStream.write(str.getBytes());
                    mLogFileOutputStream.flush();

                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }

            }
        });
    }
}
