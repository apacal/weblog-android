package cn.apacal.weblog.comm;

import java.util.List;

/**
 * Created by apacalzhong on 1/26/16.
 */
public class Util {
    public static boolean isNullOrNil(List list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }
    public static boolean isNullOrNil(final String object) {
        if ((object == null) || (object.length() <= 0)) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrNil(final CharSequence object) {
        if(object == null || object.length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrNil(final byte[] object) {
        if ((object == null) || (object.length <= 0)) {
            return true;
        }
        return false;
    }
    public static String getFileExtension(String name) {
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}
