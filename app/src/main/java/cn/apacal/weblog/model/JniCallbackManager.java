package cn.apacal.weblog.model;

import android.util.SparseArray;

import cn.apacal.weblog.comm.Core;

import java.util.LinkedList;
import java.util.List;

/**
 * 调用java管理
 * Created by apacalzhong on 3/21/16.
 */

public final class JniCallbackManager {

    private static JniCallbackManager mgr;
    private SparseArray<List<IJniCallbackReceiver>> mJniCallbackReceivers;

    private JniCallbackManager() {
        mJniCallbackReceivers = new SparseArray<List<IJniCallbackReceiver>>();
    }

    public static JniCallbackManager getMgr() {
        if (mgr == null) {
            mgr = new JniCallbackManager();
        }
        return mgr;
    }

    /**
     *
     * @param type
     * @param r
     * @return
     */
    public boolean registerReceiver(int type, IJniCallbackReceiver r) {
        if (r == null) {
            return false;
        }
        List<IJniCallbackReceiver> receivers = mJniCallbackReceivers.get(type);
        if (receivers == null) {
            receivers = new LinkedList<IJniCallbackReceiver>();
            mJniCallbackReceivers.put(type, receivers);
        } else if (receivers.contains(r)) {
            return false;
        }
        return receivers.add(r);
    }

    public boolean unregisterReceiver(int type, IJniCallbackReceiver r) {
        List<IJniCallbackReceiver> receivers = mJniCallbackReceivers.get(type);
        if (receivers != null) {
            receivers.remove(r);
            if (receivers.size() == 0) {
                mJniCallbackReceivers.remove(type);
            }
        }
        return false;
    }

    public boolean containsReceiver(int type, IJniCallbackReceiver r) {
        List<IJniCallbackReceiver> receivers = mJniCallbackReceivers.get(type);
        if (receivers != null && receivers.contains(r)) {
            return true;
        }
        return false;
    }

    public void handleCallback(final int type, final Object... args) {
        Core.instance().postToWorkThread(new Runnable() {
            @Override
            public void run() {
                List<IJniCallbackReceiver> receivers = mJniCallbackReceivers.get(type);
                if (receivers == null || receivers.size() == 0) {
                    return;
                }
                for (int i = 0; i < receivers.size(); i++) {
                    receivers.get(i).onCallback(type, args);
                }
            }
        });
    }

    public static interface IJniCallbackReceiver {
        void onCallback(int type, Object... args);
    }
}
