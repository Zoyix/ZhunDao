package com.zhaohe.app.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 *@Description:文件操作工具包
 *@Author:杨攀
 *@Since:2014年6月17日上午11:10:36
 */
public class FileUtils {

   /**
    *@Description: 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
    *@Author:杨攀
    *@Since: 2014年6月17日上午11:10:54
    *@param context
    *@param fileName
    *@param content
    */
    public static void write(Context context,String fileName,String content){
        if (content == null) content = "";
        try {
            FileOutputStream fos = context.openFileOutput (fileName, Context.MODE_PRIVATE);
            fos.write (content.getBytes ());

            fos.close ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    /**
     *@Description: 读取文本文件
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:11:23
     *@param context
     *@param fileName
     *@return
     */
    public static String read(Context context,String fileName){
        try {
            FileInputStream in = context.openFileInput (fileName);
            return readInStream (in);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        return "";
    }

    /**
     *@Description: 从输入流中返回 string 
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:13:41
     *@param inStream
     *@return
     */
    public static String readInStream(InputStream inStream){
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream ();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read (buffer)) != -1) {
                outStream.write (buffer, 0, length);
            }
            outStream.close ();
            inStream.close ();
            return outStream.toString ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return null;
    }

    /**
     *@Description: 创建文件夹
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:13:04
     *@param folderPath
     *@param fileName
     *@return
     */
    public static File createFile(String folderPath,String fileName){
        File destDir = new File (folderPath);
        if (!destDir.exists ()) {
            destDir.mkdirs ();
        }
        return new File (folderPath,fileName);
    }

    /**
     *@Description: 向手机写文件
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:14:19
     *@param buffer 文件数据
     *@param folder 相对应sdcard的路径 
     *@param fileName 文件的名称
     *@return
     */
    public static boolean writeFile(byte[] buffer,String folder,String fileName){
        boolean writeSucc = false;
        boolean sdCardExist = Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED);
        String folderPath = "";
        if (sdCardExist) {
            folderPath = Environment.getExternalStorageDirectory () + File.separator + folder + File.separator;
        } else {
            writeSucc = false;
        }
        File fileDir = new File (folderPath);
        if (!fileDir.exists ()) {
            fileDir.mkdirs ();
        }
        File file = new File (folderPath + fileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream (file);
            out.write (buffer);
            writeSucc = true;
        } catch (Exception e) {
            e.printStackTrace ();
        } finally {
            try {
                out.close ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        return writeSucc;
    }

    /**
     *@Description: 根据文件绝对路径获取文件名-包括扩展名
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:16:21
     *@param filePath
     *@return
     */
    public static String getFileName(String filePath){
        if (StringUtils.isEmpty (filePath)) return "";
        return filePath.substring (filePath.lastIndexOf (File.separator) + 1);
    }

    /**
     *@Description: 根据文件的绝对路径获取文件名但不包含扩展名
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:16:21
     *@param filePath
     *@return
     */
    public static String getFileNameNoFormat(String filePath){
        if (StringUtils.isEmpty (filePath)) { return ""; }
        int point = filePath.lastIndexOf ('.');
        return filePath.substring (filePath.lastIndexOf (File.separator) + 1, point);
    }

    /**
     *@Description: 获取文件扩展名
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:17:19
     *@param fileName
     *@return
     */
    public static String getFileFormat(String fileName){
        if (StringUtils.isEmpty (fileName)) return "";
        int point = fileName.lastIndexOf ('.');
        return fileName.substring (point + 1);
    }

    /**
     *@Description: 获取文件大小
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:17:47
     *@param filePath
     *@return
     */
    public static long getFileSize(String filePath){
        long size = 0;
        File file = new File (filePath);
        if (file != null && file.exists ()) {
            size = file.length ();
        }
        return size;
    }

    /**
     *@Description: 根据文件全路径 获取 文件的路径
     *@Author:杨攀
     *@Since: 2014年8月13日下午2:06:21
     *@param fileFullPath  文件路径+文件名称
     *@return
     */
    public static String getFilePath(String fileFullPath){
        if(!StringUtils.isEmpty (fileFullPath)){
            return fileFullPath.substring (0, fileFullPath.lastIndexOf ("/")+1);
        }
        return "";
    }
    
    /**
     *@Description: 转换文件大小
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:19:27
     *@param fileS
     *@return B/KB/MB/GB
     */
    public static String formatFileSize(long fileS){
        java.text.DecimalFormat df = new java.text.DecimalFormat ("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format ((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format ((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format ((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format ((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

   
    /**
     *@Description: 获取目录文件大小
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:20:28
     *@param dir
     *@return
     */
    public static long getDirSize(File dir){
        if (dir == null) { return 0; }
        if (!dir.isDirectory ()) { return 0; }
        long dirSize = 0;
        File[] files = dir.listFiles ();
        for ( File file : files ) {
            if (file.isFile ()) {
                dirSize += file.length ();
            } else if (file.isDirectory ()) {
                dirSize += file.length ();
                dirSize += getDirSize (file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     *@Description: 获取目录文件个数
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:20:44
     *@param dir
     *@return
     */
    public long getFileList(File dir){
        long count = 0;
        File[] files = dir.listFiles ();
        count = files.length;
        for ( File file : files ) {
            if (file.isDirectory ()) {
                count = count + getFileList (file);// 递归
                count--;
            }
        }
        return count;
    }

    /**
     *@Description: InputStream to Byte[]
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:26:02
     *@param inStream
     *@return
     *@throws Exception
     */
    public static byte[] toBytes(InputStream inStream) throws Exception{
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream ();
        byte[] buffer = new byte[1024*3];//加快速度
        int len = -1;
        while ((len = inStream.read (buffer)) != -1) {
            outSteam.write (buffer, 0, len);
        }
        outSteam.close ();
        inStream.close ();
        return outSteam.toByteArray ();
    }
    
     
    /**
     *@Description: 计算SD卡的剩余空间
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:27:07
     *@return 返回-1，说明没有安装sd卡
     */
    public static long getFreeDiskSpace(){
        String status = Environment.getExternalStorageState ();
        long freeSpace = 0;
        if (status.equals (Environment.MEDIA_MOUNTED)) {
            try {
                File path = Environment.getExternalStorageDirectory ();
                StatFs stat = new StatFs (path.getPath ());
                long blockSize = stat.getBlockSize ();
                long availableBlocks = stat.getAvailableBlocks ();
                freeSpace = availableBlocks * blockSize / 1024;
            } catch (Exception e) {
                e.printStackTrace ();
            }
        } else {
            return -1;
        }
        return (freeSpace);
    }
 

    /**
     *@Description: 检查是否安装SD卡
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:27:51
     *@return
     */
    public static boolean checkSaveLocationExists(){
        String sDCardStatus = Environment.getExternalStorageState ();
        return sDCardStatus.equals (Environment.MEDIA_MOUNTED);
    }

    /**
     *@Description: 检查是否安装外置的SD卡 
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:28:56
     *@return
     */
    public static boolean checkExternalSDExists(){
        Map<String, String> evn = System.getenv ();
        return evn.containsKey ("SECONDARY_STORAGE");
    }

    /**
     *@Description: 删除目录(包括：目录里的所有文件)
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:29:15
     *@param fileName
     *@return
     */
    public static boolean deleteDirectory(String fileName){
        boolean status;
        SecurityManager checker = new SecurityManager ();
        if (!fileName.equals ("")) {
            File path = Environment.getExternalStorageDirectory ();
            File newPath = new File (path.toString () + fileName);
            checker.checkDelete (newPath.toString ());
            if (newPath.isDirectory ()) {
                String[] listfile = newPath.list ();
                // delete all files within the specified directory and then
                // delete the directory
                try {
                    for ( int i = 0 ; i < listfile.length ; i++ ) {
                        File deletedFile = new File (newPath.toString () + "/" + listfile[i].toString ());
                        deletedFile.delete ();
                    }
                    newPath.delete ();
                    status = true;
                } catch (Exception e) {
                    e.printStackTrace ();
                    status = false;
                }

            } else status = false;
        } else status = false;
        return status;
    }
 
    
    /**
     *@Description: 删除空目录
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:29:50
     *@param path
     *@return  0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
     */
    public static int deleteBlankPath(String path){
        File f = new File (path);
        if (!f.canWrite ()) { return 1; }
        if (f.list () != null && f.list ().length > 0) { return 2; }
        if (f.delete ()) { return 0; }
        return 3;
    }

    /**
     *@Description: 重命名
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:30:34
     *@param oldName
     *@param newName
     *@return
     */
    public static boolean reNamePath(String oldName,String newName){
        File f = new File (oldName);
        return f.renameTo (new File (newName));
    }

    /**
     *@Description: 删除文件
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:31:21
     *@param filePath
     *@return
     */
    public static boolean deleteFileWithPath(String filePath){
        SecurityManager checker = new SecurityManager ();
        File f = new File (filePath);
        checker.checkDelete (filePath);
        if (f.isFile ()) {
            f.delete ();
            return true;
        }
        return false;
    }

    /**
     *@Description: 清空一个文件夹
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:31:35
     *@param filePath
     */
    public static void clearFileWithPath(String filePath){
        List<File> files = FileUtils.listPathFiles (filePath);
        if (files.isEmpty ()) { return; }
        for ( File f : files ) {
            if (f.isDirectory ()) {
                clearFileWithPath (f.getAbsolutePath ());
            } else {
                f.delete ();
            }
        }
    }

    /**
     *@Description: 获取SD卡的根目录
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:31:55
     *@return
     */
    public static String getSDRoot(){
        return Environment.getExternalStorageDirectory ().getAbsolutePath ();
    }

    /**
     *@Description: 获取手机外置SD卡的根目录
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:32:10
     *@return
     */
    public static String getExternalSDRoot(){
        Map<String, String> evn = System.getenv ();
        return evn.get ("SECONDARY_STORAGE");
    }

    /**
     *@Description: 列出root目录下所有子目录
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:32:32
     *@param root
     *@return
     */
    public static List<String> listPath(String root){
        List<String> allDir = new ArrayList<String> ();
        SecurityManager checker = new SecurityManager ();
        File path = new File (root);
        checker.checkRead (root);
        // 过滤掉以.开始的文件夹
        if (path.isDirectory ()) {
            for ( File f : path.listFiles () ) {
                if (f.isDirectory () && !f.getName ().startsWith (".")) {
                    allDir.add (f.getAbsolutePath ());
                }
            }
        }
        return allDir;
    }

    /**
     *@Description: 获取一个文件夹下的所有文件
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:32:45
     *@param root
     *@return
     */
    public static List<File> listPathFiles(String root){
        List<File> allDir = new ArrayList<File> ();
        SecurityManager checker = new SecurityManager ();
        File path = new File (root);
        checker.checkRead (root);
        File[] files = path.listFiles ();
        for ( File f : files ) {
            if (f.isFile ()) allDir.add (f);
            else listPath (f.getAbsolutePath ());
        }
        return allDir;
    }

    public enum PathStatus {
        SUCCESS, EXITS, ERROR
    }

      
    /**
     *@Description: 获取应用程序缓存文件夹下的指定目录
     *@Author:杨攀
     *@Since: 2014年6月17日上午11:33:22
     *@param context
     *@param dir
     *@return
     */
    public static String getAppCache(Context context,String dir){
        String savePath = context.getCacheDir ().getAbsolutePath () + "/" + dir + "/";
        File savedir = new File (savePath);
        if (!savedir.exists ()) {
            savedir.mkdirs ();
        }
        savedir = null;
        return savePath;
    }
    
    public static boolean copyFilesFassets(Context context,String fileName,String newPath){
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets ().open (fileName);
            File file = new File (newPath);
            file.createNewFile ();
            FileOutputStream fos = new FileOutputStream (file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read (temp)) > 0) {
                fos.write (temp, 0, i);
            }
            fos.close ();
            is.close ();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace ();
        }
        return copyIsFinish;
    }
    
    /**
     *@Description: 删除缓存文件夹下不是当天的文件的文件
     *@Author:杨攀
     *@Since: 2014年7月25日下午4:38:31
     */
    public static void deleteCacheDirByDay(File file){
        String day = DateUtils.getCurrentDayFormat ();
        for (File fileItem : file.listFiles ()) {
            if(fileItem.getName ().indexOf (day) == -1){
                fileItem.delete ();
            }
        }
    }
}