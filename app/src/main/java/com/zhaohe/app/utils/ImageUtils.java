package com.zhaohe.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description:图片操作工具包
 * @Author:杨攀
 * @Since:2014年8月17日上午9:23:17
 */
public class ImageUtils {

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;

    private static final int imgView_width = 100;          // 缩略图照片的大小
    private static final int imgView_height = 100;          // 缩略图照片的大小

    /**
     * gridlayout离边距的dp值
     */
    public static final int GRIDLAYOUT_PADDING_DP = 10;

    public final static String SDCARD_MNT = "/mnt/sdcard";
    public final static String SDCARD = "/sdcard";

    /**
     * @param filePath
     * @param w
     * @param h
     * @return
     * @Description: 获取图片缩略图
     * @Author:杨攀
     * @Since: 2014年8月17日上午11:02:55
     */
    public static Bitmap loadImgThumbnail(String filePath, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = 8;
        Bitmap bitmap = getBitmapByPath(filePath, opts);
        return zoomBitmap(bitmap, w, h);
    }

    /**
     * @param filePath
     * @return
     * @Description: 获取bitmap
     * @Author:杨攀
     * @Since: 2014年8月17日上午10:46:56
     */
    public static Bitmap getBitmapByPath(String filePath) {
        return getBitmapByPath(filePath, null);
    }

    /**
     * @param filePath
     * @return
     * @Description: 获取bitmap根据路径
     * @Author:杨攀
     * @Since: 2014年8月17日上午10:46:56
     */
    public static Bitmap getBitmapByPath(String filePath, BitmapFactory.Options opts) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            File file = new File(filePath);
            fis = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(fis, null, opts);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return bitmap;
    }

    /**
     * @param bitmap
     * @param w
     * @param h
     * @return
     * @Description: 放大缩小图片
     * @Author:杨攀
     * @Since: 2014年8月17日上午10:ic_launcher:57
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        Bitmap newbmp = null;
        if (bitmap != null) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            float scaleWidht = ((float) w / width);
            float scaleHeight = ((float) h / height);
            matrix.postScale(scaleWidht, scaleHeight);
            newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            bitmap.recycle();
        }
        return newbmp;
    }

    /**
     * @param mUri
     * @return
     * @Description: 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
     * @Author:杨攀
     * @Since: 2014年11月20日上午10:55:40
     */
    public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
        String filePath = null;

        String mUriString = mUri.toString();
        mUriString = Uri.decode(mUriString);

        String pre1 = "file://" + SDCARD + File.separator;
        String pre2 = "file://" + SDCARD_MNT + File.separator;

        if (mUriString.startsWith(pre1)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre1.length());
        } else if (mUriString.startsWith(pre2)) {
            filePath = Environment.getExternalStorageDirectory().getPath() + File.separator + mUriString.substring(pre2.length());
        }
        return filePath;
    }


    /**
     * @param context
     * @param uri
     * @return
     * @Description: 通过uri获取文件的绝对路径
     * @Author:杨攀
     * @Since: 2014年8月17日上午11:07:22
     */
    @SuppressLint("NewApi")
    public static String getAbsoluteImagePath(Activity context, Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;

    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = MediaStore.Images.Media.DATA;
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param context        上下文，如果为null,则压缩的图片不显示，否是则显示压缩后的图片
     * @param largeImagePath 原始大图路径
     * @param thumbfilePath  输出缩略图路径
     * @param square_size    输出图片宽度
     * @param quality        输出图片质量
     * @throws IOException
     * @Description: 创建缩略图--压缩图片
     * @Author:杨攀
     * @Since: 2014年8月17日下午2:52:ic_launcher
     */
    public static void createImageThumbnail(Context context, String largeImagePath, String thumbfilePath, int square_size, int quality) throws IOException {

        // 生成缩放后的bitmap
        Bitmap thb_bitmap = decodeSampledBitmapFromFile(largeImagePath, square_size);
        // 生成缩放后的图片文件
        saveNomediaImageToSD(context, thumbfilePath, thb_bitmap, quality);
    }

    /**
     * @param img_size
     * @param square_size
     * @return
     * @Description: 计算缩放图片的宽高
     * @Author:杨攀
     * @Since: 2014年8月17日下午2:53:36
     */
    public static int[] scaleImageSize(int[] img_size, int square_size) {
        if (img_size[0] <= square_size && img_size[1] <= square_size) return img_size;
        double ratio = square_size / (double) Math.max(img_size[0], img_size[1]);
        return new int[]{(int) (img_size[0] * ratio), (int) (img_size[1] * ratio)};
    }

    /**
     * @param ctx
     * @param filePath 图片的路径
     * @param bitmap   图片bitmap
     * @param quality  输出图片质量
     * @throws IOException
     * @Description: 写图片文件到SD卡
     * @Author:杨攀
     * @Since: 2014年8月17日下午2:54:15
     */
    public static void saveImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            bitmap.compress(CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }
        }
    }

    /**
     * @param ctx      上下文，如果为null,则压缩的图片不显示，否是则显示压缩后的图片
     * @param filePath 图片的路径
     * @param bitmap   图片bitmap
     * @param quality  输出图片质量
     * @throws IOException
     * @Description: 写图片文件到SD卡  .nomedia， 却不需要在 图库中显示图片
     * @Author:杨攀
     * @Since: 2014年8月13日下午1:54:56
     */
    public static void saveNomediaImageToSD(Context ctx, String filePath, Bitmap bitmap, int quality) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if (!file.exists()) {
                file.mkdirs();
                // .nomedia， 却不需要在 图库中显示图片
                String path = FileUtils.getFilePath(filePath);
                new File(path + ".nomedia").createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
            if (null != bitmap) {
                bitmap.compress(CompressFormat.JPEG, quality, bos);//30 是压缩率，表示压缩70%; 如果不压缩是100，表示压缩率为0
                bitmap.recycle();
                bitmap = null;
            }

            bos.flush();
            bos.close();
            if (ctx != null) {
                scanPhoto(ctx, filePath);
            }

        }
    }

    /**
     * @param ctx
     * @param imgFilePathName
     * @Description: 让Gallery上能马上看到该图片
     * @Author:杨攀
     * @Since: 2014年8月17日下午2:55:36
     */
    public static void scanPhoto(Context ctx, String imgFilePathName) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFilePathName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        ctx.sendBroadcast(mediaScanIntent);
    }

    /**
     * @param context
     * @return
     * @Description: 获取图片宽度
     * @Author:邹苏启
     * @Since: 2014-10-15下午1:59:19
     */
    public static int getImageWidth(Context context) {
        return DensityUtil.dip2px(context, imgView_width);
    }

    /**
     * @param context
     * @return
     * @Description: 获取图片高度
     * @Author:邹苏启
     * @Since: 2014-10-15下午2:00:23
     */
    public static int getImageHeight(Context context) {
        return DensityUtil.dip2px(context, imgView_height);
    }


    // --------------------------上面和下面是 两种 压缩图片的方法--------------------------------------

    /**
     * @param pathName 文件全路径和名称
     * @param reqWidth 所需图片压缩尺寸最小宽度
     * @return
     * @Description: 创建缩略图--返回压缩图片
     * @Author:杨攀
     * @Since: 2014年8月10日下午4:40:44
     */
    public static Bitmap decodeSampledBitmapFromFile(String pathName, int reqWidth) {
        // 首先不加载图片,仅获取图片尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // 当inJustDecodeBounds设为true时,不会加载图片仅获取图片尺寸信息
        options.inJustDecodeBounds = true;
        // 此时仅会将图片信息会保存至options对象内,decode方法不会返回bitmap对象
        BitmapFactory.decodeFile(pathName, options);
        // 计算压缩比例,如inSampleSize=4 时,图片会压缩成原图的1/4
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 当inJustDecodeBounds设为false时,BitmapFactory.decode...就会返回图片对象了
        options.inJustDecodeBounds = false;
        // 利用计算的比例值获取压缩后的图片对象
        return BitmapFactory.decodeFile(pathName, options);
    }


    /**
     * @param options     解析图片的配置信息
     * @param square_size 所需图片压缩尺寸最小宽度
     * @return
     * @Description: 计算 bitmap sampleSize
     * @Author:杨攀
     * @Since: 2015年4月15日下午7:17:25
     */
    public final static int calculateInSampleSize(BitmapFactory.Options options, int square_size) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        // 原始图片的高宽
        int[] cur_img_size = new int[]{width, height};
        // 计算原始图片缩放后的宽高
        int[] new_img_size = scaleImageSize(cur_img_size, square_size);

        if (height > new_img_size[1] || width > new_img_size[0]) {
            final int heightRatio = Math.round((float) height / (float) new_img_size[1]);
            final int widthRatio = Math.round((float) width / (float) new_img_size[0]);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }
}
