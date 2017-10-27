package com.zhaohe.zhundao.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.zhaohe.app.commons.download.DownloadProgressListener;
import com.zhaohe.app.commons.download.FileDownloader;
import com.zhaohe.app.utils.FileUtils;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.ui.MainActivity;

import java.io.File;

/**
 * @Description: 版本更新
 * @Author:杨攀
 * @Since:2014年8月6日上午9:43:22
 */
public class UpdateAppService extends Service {

    // app 名称
    private String app_name;
    // app 更新路径
    private String app_path;
    // App 保存路径
    private String saveDir;

    private NotificationManager notificationManager;
    private Notification notification;

    private Intent updateIntent;
    private PendingIntent pendingIntent;

    private int notification_id = R.layout.notification_item;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent.getStringExtra("app_name")) {
            return super.onStartCommand(intent, flags, startId);
        }

        app_name = intent.getStringExtra("app_name");
        app_path = intent.getStringExtra("app_path");

        String fileformat = FileUtils.getFileFormat(app_name);
        // 判断文件是否有扩展名
        if (fileformat.equals("")) {
            app_name += ".apk";
        }

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            saveDir = Environment.getExternalStorageDirectory() + File.separator;

            createNotification();

            // 开始去下载更新
            AsyncTaskUpdate update = new AsyncTaskUpdate();
            update.execute();
        } else {
            //如果没有 sdk 
            /*Intent it =new Intent(this, DialogActivity.class);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(it);*/
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private class AsyncTaskUpdate extends AsyncTask<String, Integer, String> {

        private int fileSize;

        @Override
        protected String doInBackground(String... params) {
            try {
                FileDownloader loader = new FileDownloader(getApplicationContext(), app_path, new File(saveDir), 3);
                fileSize = loader.getFileSize();// 得到文件总大小
                contentView.setProgressBar(R.id.notificationProgress, fileSize, 0, true);
                notificationManager.notify(notification_id, notification);

                loader.download(new DownloadProgressListener() {
                    public void onDownloadSize(int size) {
                        //System.out.println("已经下载："+ size);
                        // 执行publishProgress()调用onProgressUpdate()方法
                        publishProgress(size);
                    }
                });
                return saveDir + app_name;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                // 下载完成，点击安装
                Uri uri = Uri.fromFile(new File(result));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //设定Notification出现时的声音，一般不建议自定义  
                notification.defaults |= Notification.DEFAULT_SOUND;
                pendingIntent = PendingIntent.getActivity(UpdateAppService.this, 0, intent, 0);
//                notification.setLatestEventInfo (UpdateAppService.this, app_name, getString (R.string.app_updateApp_success), pendingIntent);

            } else {
//                notification.setLatestEventInfo (UpdateAppService.this, app_name, getString (R.string.app_updateApp_error), pendingIntent);
            }
            notificationManager.notify(notification_id, notification);
            stopService(updateIntent);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //notification.setLatestEventInfo(getApplicationContext (), app_name, "下载："+values[0], pendingIntent);
            //notificationManager.notify(notification_id, notification);

            int downCount = values[0] * 100 / fileSize;

            contentView.setTextViewText(R.id.notificationPercent, downCount + "%");
            contentView.setProgressBar(R.id.notificationProgress, fileSize, values[0], true);
            notification.contentView = contentView;
            notificationManager.notify(notification_id, notification);
        }
    }

    /***
     * 创建通知栏
     */
    RemoteViews contentView;

    public void createNotification() {

        /*-
        notificationManager = (NotificationManager) getSystemService (Context.NOTIFICATION_SERVICE);
        notification = new Notification ();
        notification.icon = R.drawable.ic_launcher;
        //设定Notification出现时的声音，一般不建议自定义  
        notification.defaults |= Notification.DEFAULT_SOUND; 
        //指定Flag，Notification.FLAG_AUTO_CANCEL意指点击这个Notification后，立刻取消自身  
        //这符合一般的Notification的运作规范  
        notification.flags|=Notification.FLAG_AUTO_CANCEL; 
        // 这个参数是通知提示闪出来的值.
        notification.tickerText = "开始下载";
        
        updateIntent = new Intent(this, SignListMoreActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        // 这里面的参数是通知栏view显示的内容
        notification.setLatestEventInfo(this, app_name, "下载：0", pendingIntent);
        //显示这个notification  
        notificationManager.notify(notification_id, notification);*/

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.mipmap.ic_launcher, app_name, System.currentTimeMillis());


        //指定Flag，Notification.FLAG_AUTO_CANCEL意指点击这个Notification后，立刻取消自身  
        //这符合一般的Notification的运作规范  
        //notification.flags|=Notification.FLAG_ONGOING_EVENT;

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载");
        contentView.setTextViewText(R.id.notificationPercent, "0");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

        updateIntent = new Intent(this, MainActivity.class);
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, notification);
    }

}
