package com.zhaohe.app.commons.http;

import android.util.Log;

import com.zhaohe.app.commons.http.service.FormFile;
import com.zhaohe.zhundao.constant.Constant;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HttpUtil {
    private static final String TAG = "HttpUtil";
    /**
     * 地址
     */
    private static final String INNER_URL = "http://open.zhundao.net/api/PerActivity/PostActivityList?accessKey=oX9XjjiR5v0zI4o-cG6N1ZaRqPns";

    /**
     * @param path     请求路径
     * @param params   请求参数
     * @param encoding 编码
     * @return boolean
     * @throws Exception
     * @Description: 通过HttpClient发送Post请求---不建议使用
     * @Author:杨攀
     * @Since: 2014年6月1日上午8:25:45
     */
    private static boolean sendHttpClientPOSTRequest(String path, Map<String, String> params, String encoding) {
        boolean bool = false;
        DefaultHttpClient client = null;
        try {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();// 存放请求参数
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, encoding);
            HttpPost httpPost = new HttpPost(path);
            // httpPost.setHeader("Host", (String) SPUtils.get(mContext,"HOST",Constant.HOST));
            httpPost.setHeader("Connection", "Keep-Alive");
            /* httpPost.setHeader ("Cookie", cookie); */
            // httpPost.setHeader ("User-Agent", AppContext.getHandSetInfo ());
            httpPost.setHeader("AppKey", Constant.AppKey);
            httpPost.setEntity(entity);
            client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            Log.i(TAG, "请求返回状态码：" + statusCode);
            /* 若状态码为200 ok */
            if (statusCode == 200) {
                bool = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendHttpClientPOSTRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return bool;
    }

    /**
     * @param path   请求路径
     * @param params 请求参数
     * @return InputStream
     * @Description: 发送Post请求-编码格式默认 UTF-8
     * @Author:杨攀
     * @Since: 2014年8月4日下午12:24:34
     */
    public static String sendPOSTRequest(String path, Map<String, String> params) {
        return sendPOSTRequest(path, params, "UTF-8");
    }

    /**
     * @param path     请求路径
     * @param params   请求参数
     * @param encoding 编码
     * @return String  null 请求失败
     * @Description: 发送Post请求
     * @Author:杨攀
     * @Since: 2014年8月4日下午12:25:22
     */
    public static String sendPOSTRequest(String path, Map<String, String> params, String encoding) {
        Log.i(TAG, "请求发送：" + path + "-参数-" + params);
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            // 组装 name=yangpan&age=28
            StringBuilder data = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    data.append(entry.getKey()).append("=");
                    data.append(URLEncoder.encode(entry.getValue(), encoding));
                    data.append("&");
                }
                data.deleteCharAt(data.length() - 1);
            }
            byte[] entity = data.toString().getBytes();// 生成实体数据
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);// 设置超时
            conn.setRequestMethod("POST");
            // 允许对外输出数据
            conn.setDoOutput(true);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");
            OutputStream outStream = conn.getOutputStream();
            outStream.write(entity);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendPOSTRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 发送GET请求
     *
     * @param path   请求路径
     * @param params 请求参数
     *               //     * @param encoding 编码
     * @return String  null 请求失败
     */
    public static String sendGETRequest(String path, Map<String, String> params, String ecoding) {
        InputStream inputStream = null;
        // http://192.168.1.100:8080/web/ManageServlet?name=yangpan&age=28
        HttpURLConnection conn = null;
        try {
            StringBuilder url = new StringBuilder(path);
            url.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), ecoding));
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendGETRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String sendGET2Request(String path, Map<String, String> params, String encoding) {
        Log.i(TAG, "请求发送：" + path + "-参数-" + params);
        InputStream inputStream = null;
        HttpURLConnection conn = null;
        try {
            // 组装 name=yangpan&age=28
            StringBuilder data = new StringBuilder();
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    data.append(entry.getKey()).append("=");
                    data.append(URLEncoder.encode(entry.getValue(), encoding));
                    data.append("&");
                }
                data.deleteCharAt(data.length() - 1);
            }
            byte[] entity = data.toString().getBytes();// 生成实体数据
            conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);// 设置超时
            conn.setRequestMethod("GET");
            // 允许对外输出数据
            conn.setDoOutput(true);
            // 设定传送的内容类型是可序列化的java对象
            // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");
            OutputStream outStream = conn.getOutputStream();
            outStream.write(entity);
            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendGET2Request:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    public static String sendPostNewrequest(String path, Map<String, String> params, String ecoding) {
        InputStream inputStream = null;
        // http://192.168.1.100:8080/web/ManageServlet?name=yangpan&age=28
        HttpURLConnection conn = null;
        try {
            StringBuilder url = new StringBuilder(path);
            url.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), ecoding));
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");

            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendPOSTRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    //带url参数和body参数体的post方法
    public static String sendPostNew2request(String path, Map<String, String> params, String ecoding, String param) {
        InputStream inputStream = null;
        // http://192.168.1.100:8080/web/ManageServlet?name=yangpan&age=28
        HttpURLConnection conn = null;
        try {
            StringBuilder url = new StringBuilder(path);
            url.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), ecoding));
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");
            OutputStream os = conn.getOutputStream();


            os.write(param.getBytes());

            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendGETRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    //带url参数和body参数体的post方法
    public static String sendPostNew2request(String path, Map<String, String> params, String ecoding, Map<String, String> bodys) {
        InputStream inputStream = null;
        // http://192.168.1.100:8080/web/ManageServlet?name=yangpan&age=28
        HttpURLConnection conn = null;
        try {
            StringBuilder url = new StringBuilder(path);
            url.append("?");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                url.append(entry.getKey()).append("=");
                url.append(URLEncoder.encode(entry.getValue(), ecoding));
                url.append("&");
            }
            url.deleteCharAt(url.length() - 1);
            conn = (HttpURLConnection) new URL(url.toString()).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            // conn.setRequestProperty ("User-Agent", AppContext.getHandSetInfo ());
            conn.setRequestProperty("AppKey", Constant.AppKey);
            conn.setRequestProperty("Connection", "Keep-Alive");
            OutputStream os = conn.getOutputStream();
            StringBuilder body = new StringBuilder();
            for (Map.Entry<String, String> entry : bodys.entrySet()) {
                body.append(entry.getKey()).append("=");
                body.append(URLEncoder.encode(entry.getValue(), ecoding));
                body.append("&");
            }
            String ok = body.toString();
            os.write(ok.getBytes());

            if (conn.getResponseCode() == 200) {
                inputStream = conn.getInputStream();
                byte[] dateStream = readStream(inputStream);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + conn.getResponseCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "HttpUtil.sendGETRequest:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * @param path
     * @param params
     * @param files
     * @return
     * @throws Exception
     * @Description: Post 有文件上传的提交， tMultipart/Form-data
     * @Author:杨攀
     * @Since: 2014年11月21日下午3:25:17
     */
    public static String sendPostMultipartFormdataRequests(String path, Map<String, String> params, FormFile[] files) {
        Log.i(TAG, "请求发送：" + path + "-参数-" + params);
        final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);// 允许输入
            httpURLConnection.setDoOutput(true);// 允许输出
            httpURLConnection.setUseCaches(false);// 不允许使用缓存
            httpURLConnection.setRequestMethod("POST");
            // 设置Http请求头
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 必须在Content-Type 请求头中指定分界符中的任意字符串
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            int fileDataLength = 0;
            for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
                StringBuilder fileExplain = new StringBuilder();
                fileExplain.append("--");
                fileExplain.append(BOUNDARY);
                fileExplain.append("\r\n");
                fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname()
                        + "\"\r\n");
                fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                fileDataLength += fileExplain.length();
                if (uploadFile.getInStream() != null) {
                    fileDataLength += uploadFile.getFile().length();
                } else {
                    fileDataLength += uploadFile.getData().length;
                }
                fileDataLength += "\r\n".length();
            }

            // 构造文本类型参数的实体数据
            StringBuilder textEntity = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
            // 计算传输给服务器的实体数据总长度
            int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

            // 定义数据写入流，准备上传文件
            DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
            outStream.write(textEntity.toString().getBytes());

            // 把所有文件类型的实体数据发送出来
            for (FormFile uploadFile : files) {
                StringBuilder fileEntity = new StringBuilder();
                fileEntity.append("--");
                fileEntity.append(BOUNDARY);
                fileEntity.append("\r\n");
                fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname()
                        + "\"\r\n");
                fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                outStream.write(fileEntity.toString().getBytes());
                if (uploadFile.getInStream() != null) {
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    uploadFile.getInStream().close();
                } else {
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                }
                outStream.write("\r\n".getBytes());
            }
            // 下面发送数据结束标志，表示数据已经结束
            outStream.write(endline.getBytes());
            outStream.flush();

            // 得到响应码  
            int resCode = httpURLConnection.getResponseCode();
            if (resCode == 200) {
                // 读取从服务器传过来的信息
                InputStream is = httpURLConnection.getInputStream();
                byte[] dateStream = readStream(is);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + resCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return null;
    }

    /**
     * @param path
     * @param params
     * @param files
     * @return
     * @throws Exception
     * @Description: Post 有文件上传的提交， tMultipart/Form-data
     * @Author:杨攀
     * @Since: 2014年11月21日下午3:25:
     */
    public static String photoUpload(String path, Map<String, String> params, FormFile[] files) {
        Log.i(TAG, "请求发送：" + path + "-参数-" + params);
        final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(path);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);// 允许输入
            httpURLConnection.setDoOutput(true);// 允许输出
            httpURLConnection.setUseCaches(false);// 不允许使用缓存
            httpURLConnection.setRequestMethod("POST");
            // 设置Http请求头
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 必须在Content-Type 请求头中指定分界符中的任意字符串
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);

            int fileDataLength = 0;
            for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
                StringBuilder fileExplain = new StringBuilder();
                fileExplain.append("--");
                fileExplain.append(BOUNDARY);
                fileExplain.append("\r\n");
                fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getName() + "\";filename=\"" + uploadFile.getFilname()
                        + "\"\r\n");
                fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                fileDataLength += fileExplain.length();
                if (uploadFile.getInStream() != null) {
                    fileDataLength += uploadFile.getFile().length();
                } else {
                    fileDataLength += uploadFile.getData().length;
                }
                fileDataLength += "\r\n".length();
            }

            // 构造文本类型参数的实体数据
            StringBuilder textEntity = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
                textEntity.append("--");
                textEntity.append(BOUNDARY);
                textEntity.append("\r\n");
                textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
                textEntity.append(entry.getValue());
                textEntity.append("\r\n");
            }
            // 计算传输给服务器的实体数据总长度
            int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

            // 定义数据写入流，准备上传文件
            DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
            outStream.write(textEntity.toString().getBytes());

            // 把所有文件类型的实体数据发送出来
            for (FormFile uploadFile : files) {
                StringBuilder fileEntity = new StringBuilder();
                fileEntity.append("--");
                fileEntity.append(BOUNDARY);
                fileEntity.append("\r\n");
                fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getName() + "\";filename=\"" + uploadFile.getFilname()
                        + "\"\r\n");
                fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
                outStream.write(fileEntity.toString().getBytes());
                if (uploadFile.getInStream() != null) {
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    uploadFile.getInStream().close();
                } else {
                    outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
                }
                outStream.write("\r\n".getBytes());
            }
            // 下面发送数据结束标志，表示数据已经结束
            outStream.write(endline.getBytes());
            outStream.flush();

            // 得到响应码
            int resCode = httpURLConnection.getResponseCode();
            if (resCode == 200) {
                // 读取从服务器传过来的信息
                InputStream is = httpURLConnection.getInputStream();
                byte[] dateStream = readStream(is);
                return new String(dateStream);
            } else {
                Log.i(TAG, "请求返回状态码：" + resCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }
        return null;
    }


    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }


}
