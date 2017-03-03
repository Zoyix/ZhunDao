package com.zhaohe.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Description: 异步加载图片--主线程中、列表适配器中使用，最后在 activity 的 ondestroy 中调用 deletecacheDir 方法
 * @Author:杨攀
 * @Since:2014年7月25日上午11:05:54
 */
public class AsyncImageLoadUtils {

    private Context context;
    private String cacheDir;
    private final String dir = "/cacheDir/";
    private File cache;

    /**
     * <p>Description: </p> 使用默认使用 当前程序的路径 getApplicationContext().getFilesDir().getAbsolutePath() + /cacheDir/;
     *
     * @param context
     */
    public AsyncImageLoadUtils(Context context) {
        this.context = context;
        this.cacheDir = context.getApplicationContext().getFilesDir().getAbsolutePath() + dir;
        checkDir();
    }

    /**
     * <p>Description: </p>
     *
     * @param context
     * @param cacheDir 自定义缓存的路径
     */
    public AsyncImageLoadUtils(Context context, String cacheDir) {
        this.context = context;
        this.cacheDir = cacheDir;
        checkDir();
    }

    /**
     * @Description: 检查并创建检查目录
     * @Author:杨攀
     * @Since: 2014年7月25日下午2:35:28
     */
    private void checkDir() {
        if (cacheDir == null) {
            cacheDir = context.getApplicationContext().getFilesDir().getAbsolutePath() + dir;
        }
        cache = new File(cacheDir);
        if (!cache.exists()) {
            cache.mkdirs();
        }
    }

    /**
     * @Description: 删除缓存文件夹下的文件
     * @Author:杨攀
     * @Since: 2014年7月25日下午4:38:31
     */
    public void deleteCacheDir() {
        for (File fileItem : cache.listFiles()) {
            fileItem.delete();
        }
    }

    /**
     * @Description: 删除缓存文件夹下不是当天的文件的文件
     * @Author:杨攀
     * @Since: 2014年7月25日下午4:38:31
     */
    public void deleteCacheDirByDay() {
        String day = DateUtils.getCurrentDayFormat();
        for (File fileItem : cache.listFiles()) {
            if (fileItem.getName().indexOf(day) == -1) {
                fileItem.delete();
            }
        }
    }

    /**
     * @Description: 删除缓存文件夹下的文件
     * @Author:杨攀
     * @Since: 2014年7月25日下午4:38:31
     */
    public void deletecacheDir() {
        for (File fileItem : cache.listFiles()) {
            fileItem.delete();
        }
    }

    /**
     * @param imagePath
     * @param imageView
     * @Description: 本地异步显示图片
     * @Author:杨攀
     * @Since: 2015年4月20日下午8:01:51
     */
    public void asyncLocalImageLoad(String imagePath, ImageView imageView) {
        AsyncLocalImageTask task = new AsyncLocalImageTask(imageView, imagePath);
        task.execute();
    }

    /**
     * @Description: 加载本地图片
     * @Author:杨攀
     * @Since:2015年4月20日下午8:04:48
     */
    private final class AsyncLocalImageTask extends AsyncTask<String, Integer, Uri> {

        private ImageView imageView;
        private String imagePath;

        public AsyncLocalImageTask(ImageView imageView, String imagePath) {
            this.imageView = imageView;
            this.imagePath = imagePath;
        }

        @Override
        protected Uri doInBackground(String... params) {
            Uri uri = getLocalIFile(imagePath, cacheDir);
            return uri;
        }

        @Override
        protected void onPostExecute(Uri result) {
            if (imageView != null && result != null) {
                imageView.setImageURI(result);
            }
        }

    }

    /**
     * @param path
     * @param cachePath
     * @return
     * @throws IOException
     * @Description: 校验本地是否存在该图片，不存在则生成缩略图
     * @Author:杨攀
     * @Since: 2015年4月20日下午8:23:45
     */
    private Uri getLocalIFile(String path, String cachePath) {
        String dir = MD5.getMD5(path) + DateUtils.getCurrentDayFormat() + ".jpg";
        File localFile = new File(cachePath, dir);
        // 判断图片是否存在
        if (localFile.exists()) {
            return Uri.fromFile(localFile);
        } else {
            try {
                //生成缩略图
                Bitmap bitmap = ImageUtils.loadImgThumbnail(path, ImageUtils.getImageWidth(context), ImageUtils.getImageHeight(context));
                ImageUtils.saveImageToSD(null, dir, bitmap, 100);

                return Uri.fromFile(localFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * @param imageUrl  图片路径
     * @param imageView 图片控件
     * @Description: 异步加载图片，并显示在ImageView中，有缓存功能
     * @Author:杨攀
     * @Since: 2014年7月25日下午2:28:26
     */
    public void asyncImageLoad(String imageUrl, ImageView imageView) {
        // 异步任务
        AsyncImageTask asyncImageTask = new AsyncImageTask(imageView);
        asyncImageTask.execute(imageUrl);
    }


    /**
     * @Description:网络加载图片
     * @Author:杨攀
     * @Since:2015年4月20日下午8:03:06
     */
    private final class AsyncImageTask extends AsyncTask<String, Integer, Uri> {

        private ImageView imageView;

        public AsyncImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Uri doInBackground(String... params) {// 子线程中执行
            try {
                Uri uri = getFile(params[0], cacheDir);
                return uri;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Uri result) {
            if (imageView != null && result != null) {
                imageView.setImageURI(result);
            }
        }
    }

    /**
     *@Description: 异步加载图片，并显示在ImageView中，有缓存功能
     *@Author:杨攀
     *@Since: 2014年7月25日下午2:28:26
     *@param imageUrl  图片路径
     *@param imageView 图片控件
     */
    /**public void asyncImageLoad(final String imageUrl, final ImageView imageView) {

     final Handler handler = new Handler (){
    @Override public void handleMessage(Message msg){
    Uri uri = (Uri) msg.obj;
    if(imageView != null && uri != null){
    imageView.setImageURI (uri);
    }
    }
    };

     Runnable runnable = new Runnable() {
    @Override public void run(){
    try {
    Uri uri = getFile (imageUrl, cacheDir);
    handler.sendMessage (handler.obtainMessage (0, uri));
    } catch (Exception e) {
    e.printStackTrace();
    }
    }
    };
     //开启线程
     new Thread(runnable).start ();
     }*/

    /**
     * @param path      文件路径
     * @param cachePath 文件缓存路径
     * @return
     * @throws IOException
     * @throws MalformedURLException
     * @Description: 获取文件，如果文件在缓存中，则返回文件，否则就网络获取该文件并缓存
     * @Author:杨攀
     * @Since: 2014年7月25日上午11:17:53
     */
    private Uri getFile(String path, String cachePath) throws Exception {
        //String dir = MD5.getMD5 (path) + path.substring (path.lastIndexOf ("."));
        //String dir = DateUtils.getCurrentTimeFormat () + path.substring (path.lastIndexOf ("."));
        String dir = MD5.getMD5(path) + DateUtils.getCurrentDayFormat() + ".jpg";
        File localFile = new File(cachePath, dir);
        // 判断图片是否存在
        if (localFile.exists()) {
            return Uri.fromFile(localFile);
        } else {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                FileOutputStream outputStream = new FileOutputStream(localFile);
                InputStream inputStream = conn.getInputStream();
                // 创建缓冲器
                byte[] buffer = new byte[2048];
                int len = -1;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                outputStream.close();
                inputStream.close();
                return Uri.fromFile(localFile);
            }
        }
        return null;
    }
}
