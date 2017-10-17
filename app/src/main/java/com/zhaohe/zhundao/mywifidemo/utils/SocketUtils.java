package com.zhaohe.zhundao.mywifidemo.utils;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zhaohe.zhundao.mywifidemo.bean.SocketData;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Created by JokerFish on 2017/4/25.
 */

public class SocketUtils {
    private static final String TAG = "SOCKET";
    private static final String IP = "192.168.43.1";
    private static final int PORT = 5000;


    public static void send(final SocketData data, final SendCallBack callBack) {
        //{"userID": "54","SSID": "84E0F42000A600B0","PWD": "84E0F42000A600B0"}
        new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(IP, PORT);
                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();

                    bufferedWriter.write(gson.toJson(data));
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    socket.close();

                    Log.e(TAG, "send: success  " + gson.toJson(data));

//                    InputStream inputStream = socket.getInputStream();
//                    InputStreamReader reader = new InputStreamReader(inputStream);
//                    BufferedReader bufferedReader = new BufferedReader(reader);
//                    String readLine = bufferedReader.readLine();
//                    bufferedReader.close();
//                    reader.close();
//                    inputStream.close();

//                    Log.e(TAG, "read: success  " + readLine);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.send(true);
                        }
                    });

                } catch (Exception e) {
                    Log.e(TAG, "send or read : fail  ");
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.send(false);
                        }
                    });
                }
            }
        }.start();


    }


    public static void send(final String data) {
        //{"userID": "54","SSID": "84E0F42000A600B0","PWD": "84E0F42000A600B0"}
        new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(IP, PORT);
                    OutputStream outputStream = socket.getOutputStream();
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    socket.close();
                    Log.e(TAG, "send: success  ");
                } catch (IOException e) {
                    Log.e(TAG, "send: fail  ");
                    e.printStackTrace();
                }
            }
        }.start();


    }

    public interface SendCallBack {

        void send(boolean success);

    }
}
