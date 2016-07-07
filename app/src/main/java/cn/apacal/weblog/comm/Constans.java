package cn.apacal.weblog.comm;

/**
 * Created by apacalzhong on 1/20/16.
 */
public class Constans {
    public static class ConstansProtocol {
        public final static int VERSION = 0x00000001;
        public final static String DB_NAME = "weblog.db";
        public final static String PACKNAME = "weblog";
        public final static String COMPANY = "apacal";
        public final static String LOG_FILE = "core.log";
    }

    public static class StgChangeType {
        public final static int STG_CHANGE_TYPE_UPDATE = 1;
        public final static int STG_CHANGE_TYPE_INSERT = 2;
        public final static int STG_CHANGE_TYPE_DELETE = 3;

    }

    public static class JniCallbackType {

    }
}
