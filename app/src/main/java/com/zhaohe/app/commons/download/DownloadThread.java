package com.zhaohe.app.commons.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.util.Log;

public class DownloadThread extends Thread {

    private static final String TAG      = "DownloadThread";
    private File                saveFile;
    private URL                 downUrl;
    private int                 block;
    /* 下载开始位置 */
    private int                 threadId = -1;
    private int                 downLength;
    private boolean             finish   = false;
    private FileDownloader      downloader;

    public DownloadThread(FileDownloader downloader, URL downUrl, File saveFile, int block, int downLength, int threadId) {
        this.downUrl = downUrl;
        this.saveFile = saveFile;
        this.block = block;
        this.downloader = downloader;
        this.threadId = threadId;
        this.downLength = downLength;
    }

    @Override
    public void run(){
        if (downLength < block) {// 未下载完成
            try {
                HttpURLConnection http = (HttpURLConnection) downUrl.openConnection ();
                http.setConnectTimeout (5 * 1000);
                http.setRequestMethod ("GET");
                http.setRequestProperty (
                        "Accept",
                        "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
                http.setRequestProperty ("Accept-Language", "zh-CN");
                http.setRequestProperty ("Referer", downUrl.toString ());
                http.setRequestProperty ("Charset", "UTF-8");
                int startPos = block * (threadId - 1) + downLength;// 开始位置
                int endPos = block * threadId - 1;// 结束位置
                http.setRequestProperty ("Range", "bytes=" + startPos + "-" + endPos);// 设置获取实体数据的范围
                http.setRequestProperty (
                        "User-Agent",
                        "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
                http.setRequestProperty ("Connection", "Keep-Alive");

                InputStream inStream = http.getInputStream ();
                /*- 因为使用  RandomAccessFile 速度特别慢
                byte[] buffer = new byte[2048];
                int offset = 0;
                print ("Thread " + this.threadId + " start download from position " + startPos);
                // rwd: 打开以便读取和写入，对于 "rw"，还要求对文件内容的每个更新都同步写入到基础存储设备。
                //对于Android移动设备一定要注意同步，否则当移动设备断电的话会丢失数据 RandomAccessFile
                RandomAccessFile threadfile = new RandomAccessFile (this.saveFile,"rwd");
                //直接移动到文件开始位置下载的
                threadfile.seek (startPos);
                while (!downloader.getExit () && (offset = inStream.read (buffer, 0, buffer.length)) != -1) {
                    //开始写入数据到文件
                    threadfile.write (buffer, 0, offset);
                    //该线程以及下载的长度增加
                    downLength += offset;
                    //修改数据库中该线程已经下载的数据长度
                    downloader.update (this.threadId, downLength);
                    //文件下载器已经下载的总长度增加
                    downloader.append (offset);
                }
                threadfile.close ();
                inStream.close ();
                print ("Thread " + this.threadId + " download finish");
                this.finish = true;*/
                
                BufferedInputStream bis = new BufferedInputStream(inStream);  
                byte[] buffer = new byte[1024 * 8];  
                int offset = 0;  
                RandomAccessFile threadfile = new RandomAccessFile(  
                        this.saveFile, "rwd");  
                // 获取RandomAccessFile的FileChannel  
                FileChannel outFileChannel = threadfile.getChannel();  
                // 直接移动到文件开始位置下载的  
                outFileChannel.position(startPos);  
                // 分配缓冲区的大小  
                while (!downloader.getExit()  
                        && (offset = bis.read(buffer)) != -1) {  
                    outFileChannel.write(ByteBuffer.wrap(buffer, 0, offset));// 开始写入数据到文件  
                    downLength += offset; // 该线程以及下载的长度增加  
                    // 修改数据库中该线程已经下载的数据长度
                    // 更新数据库的位置换到发生异常时，保存下载进度, 修改数据库会占用很多时间
                    //downloader.update(this.threadId, downLength);
                    downloader.append(offset);// 文件下载器已经下载的总长度增加  
                }
                outFileChannel.close();  
                threadfile.close();  
                inStream.close();  
                print("Thread " + this.threadId + " download finish");  
                this.finish = true;  
            } catch (Exception e) {
                // 发生异常时，保存下载进度，后面继续下载
                downloader.update(this.threadId, downLength);// 修改数据库中该线程已经下载的数据长度
                this.downLength = -1;
                print ("Thread " + this.threadId + ":" + e);
            }
        }
    }

    private static void print(String msg){
        Log.i (TAG, msg);
    }

    /**
     * 下载是否完成
     * @return
     */
    public boolean isFinish(){
        return finish;
    }

    /**
     * 已经下载的内容大小
     * @return 如果返回值为-1,代表下载失败
     */
    public long getDownLength(){
        return downLength;
    }
}
