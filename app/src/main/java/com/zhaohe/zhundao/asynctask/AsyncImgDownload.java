package com.zhaohe.zhundao.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/12/7 14:35
 */
public class AsyncImgDownload extends AsyncTask {
  private Context context;
    private ImageView imageView;
    private String url;
    public AsyncImgDownload(Context context, ImageView imgview,String url){
  this.url=url;
        this.context=context;
        this.imageView=imgview;
    }
    /**
     * 主要是完成耗时的操作
     */
    @Override
    protected Object doInBackground(Object[] params) {

        try {
            // 创建一个URL
            URL url = new URL(this.url);

            // 从URL获取对应资源的 InputStream
            InputStream inputStream = url.openStream();
            // 用inputStream来初始化一个Bitmap 虽然此处是Bitmap，但是URL不一定非得是Bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            // 关闭 InputStream
            inputStream.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    @Override
    protected void onPostExecute(Object o){
    imageView.setImageBitmap((Bitmap)o);

    }

}
