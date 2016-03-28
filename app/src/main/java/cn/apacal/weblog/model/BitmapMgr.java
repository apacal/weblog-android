package cn.apacal.weblog.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.apacal.weblog.comm.Log;

import java.util.WeakHashMap;

/**
 * Created by apacalzhong on 3/22/16.
 */
public class BitmapMgr {
    private static BitmapMgr mBitmapMgr;

    private final static String TAG = "BitmapMgr";

    public static BitmapMgr instance() {
        if (mBitmapMgr == null) {
            mBitmapMgr = new BitmapMgr();
        }

        return mBitmapMgr;
    }

    private WeakHashMap<Integer, Bitmap> mBitMapWeakMap = new WeakHashMap<>();

    public Bitmap getBitmap(String path) {
        if (mBitMapWeakMap.get(path.hashCode()) == null) {


            mBitMapWeakMap.put(path.hashCode(), genBitmap(path, 300, 300));
        }

        return mBitMapWeakMap.get(path.hashCode());
    }

    private Bitmap genBitmap(String path, int width, int height) {
        Bitmap bitmap = decodeSampledBitmap(path, width, height);
        Bitmap smallBitmap = centerSquareScaleBitmap(bitmap, width);
        if (smallBitmap == null) {
            return bitmap;
        } else {
            Log.d(TAG, "genBitmap size[%d] height[%d] width[%d] ", smallBitmap.getByteCount(), smallBitmap.getHeight(), smallBitmap.getHeight());
            return smallBitmap;
        }
    }

    /**
     * 计算bitmap压缩的比例
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap decodeSampledBitmap(String pathName,
                                       int reqWidth, int reqHeight) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);
            Log.d(TAG, "bitmap size[%d], height width[%d %d]", bitmap.getByteCount(), bitmap.getHeight(), bitmap.getWidth());
            return bitmap;

        } catch (OutOfMemoryError error) {
            Log.e(TAG, "decodeSampleBitmap error[%s]", error);
            return null;
        }
    }


    /**
     * 中心裁剪bitmap
     * @param bitmap
     * @param edgeLength
     * @return
     */
    private Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (bitmap == null || edgeLength < 0) {
            return null;
        }
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();
        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;
            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
                return null;
            } catch (OutOfMemoryError error) {
                Log.e(TAG, "out of mem %s", error);
                return null;
            }
            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;
            try {
                Bitmap result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
                scaledBitmap.recycle();
                scaledBitmap = null;
                return result;
            } catch (Exception e) {
                Log.d(TAG, Log.getStackTraceString(e));
                return null;
            }
        }
        return null;

    }

}
