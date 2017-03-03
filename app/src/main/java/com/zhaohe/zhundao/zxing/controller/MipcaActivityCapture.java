package com.zhaohe.zhundao.zxing.controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.zhaohe.app.utils.NetworkUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncSignScanPhone;
import com.zhaohe.zhundao.bean.dao.MySignListupBean;
import com.zhaohe.zhundao.dao.MySignupListDao;
import com.zhaohe.zhundao.zxing.camera.CameraManager;
import com.zhaohe.zhundao.zxing.decoding.CaptureActivityHandler;
import com.zhaohe.zhundao.zxing.decoding.InactivityTimer;
import com.zhaohe.zhundao.zxing.view.ViewfinderView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Vector;


/**
 * @Description:初始化 摄像机
 * @Author:杨攀
 * @Since:2014年5月30日下午2:45:30
 */
public class MipcaActivityCapture extends Activity implements Callback,OnClickListener {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private String CheckInID;
    private TextView tv_sign_scan_phone,tv_sign_scan_scan;
    private ImageView iv_sign_scan_scan,    iv_sign_scan_phone;
    private LinearLayout ll_sign_scan;
    private EditText et_sign_scan;
    private Button btn_sign_scan_scan;
private Handler mHandler;
    public static final int MESSAGE_SIGN_SCAN_PHONE=100;
    private MySignupListDao dao;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_capture);
        // ViewUtil.addTopView(getApplicationContext(), this, R.string.scan_card);
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        Button mButtonBack = (Button) findViewById(R.id.button_back);
        mButtonBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                MipcaActivityCapture.this.finish();
            }
        });
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        initIntent();
        initView();
        initHandler();

    }

    private void initIntent() {
        Intent intent = getIntent();
        CheckInID = intent.getStringExtra("CheckInID");
    }

    private void initView() {
        tv_sign_scan_phone= (TextView) findViewById(R.id.tv_sign_scan_phone);
        tv_sign_scan_scan= (TextView) findViewById(R.id.tv_sign_scan_scan);
        tv_sign_scan_scan.setTextColor(getResources().getColor(R.color.status_color_green));

        iv_sign_scan_phone= (ImageView) findViewById(R.id.iv_sign_scan_phone);
        iv_sign_scan_phone.setOnClickListener(this);
        iv_sign_scan_scan= (ImageView) findViewById(R.id.iv_sign_scan_scan);
        iv_sign_scan_scan.setOnClickListener(this);
        iv_sign_scan_scan.setImageResource(R.mipmap.sign_scan_scan_pressed);
        ll_sign_scan= (LinearLayout) findViewById(R.id.ll_sign_scan);
        et_sign_scan= (EditText) findViewById(R.id.et_sign_scan);
        et_sign_scan.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        btn_sign_scan_scan= (Button) findViewById(R.id.btn_sign_scan_scan);
        btn_sign_scan_scan.setOnClickListener(this);
        dao=new MySignupListDao(this);
    }
    private  void SignScanPhone(String phone){
        AsyncSignScanPhone async = new AsyncSignScanPhone(this, mHandler, MESSAGE_SIGN_SCAN_PHONE, phone,CheckInID);
        async.execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        // characterSet = null;
        characterSet = "ISO-8859-1"; // 修改 中文乱码

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        // 添加 停止预览等
        CameraManager.get().stopPreview();
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText();
        if (resultString.equals("")) {
            Toast.makeText(MipcaActivityCapture.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        } else {

            String UTF_Str = "";
            String GB_Str = "";
            boolean is_cN = false;

            try {
                UTF_Str = new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
                is_cN = IsChineseOrNot.isChineseCharacter(UTF_Str);
                //防止有人特意使用乱码来生成二维码来判断的情况  
                boolean b = IsChineseOrNot.isSpecialCharacter(resultString);
                if (b) {
                    is_cN = true;
                }
                if (!is_cN) {
                    GB_Str = new String(resultString.getBytes("ISO-8859-1"), "GB2312");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            resultIntent.putExtra("CheckInID", CheckInID);

            //bundle.putString ("result", resultString);
            if (is_cN) {
                bundle.putString("result", UTF_Str);
            } else {
                bundle.putString("result", GB_Str);
            }
            // bundle.putParcelable ("bitmap", barcode);
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
        }
        MipcaActivityCapture.this.finish();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }


    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {

        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View view) {
switch (view.getId()){

    case R.id.iv_sign_scan_scan:
        iv_sign_scan_scan.setImageResource(R.mipmap.sign_scan_scan_pressed);
        iv_sign_scan_phone.setImageResource(R.mipmap.sign_scan_phone);
        viewfinderView.setVisibility(View.VISIBLE);
        ll_sign_scan.setVisibility(View.GONE);
        tv_sign_scan_scan.setTextColor(getResources().getColor(R.color.status_color_green));
        tv_sign_scan_phone.setTextColor(getResources().getColor(R.color.white));


        break;
    case R.id.iv_sign_scan_phone:
        iv_sign_scan_scan.setImageResource(R.mipmap.sign_scan_scan);
        iv_sign_scan_phone.setImageResource(R.mipmap.sign_scan_phone_pressed);
        viewfinderView.setVisibility(View.GONE);
        ll_sign_scan.setVisibility(View.VISIBLE);
        tv_sign_scan_scan.setTextColor(getResources().getColor(R.color.white));
        tv_sign_scan_phone.setTextColor(getResources().getColor(R.color.status_color_green));
        break;

    case R.id.btn_sign_scan_scan:
String mPhone=et_sign_scan.getText().toString();
        if (mPhone.length()!=11){
            ToastUtil.makeText(this,"手机号不正确,请检查后重试");
            return;
        }
        if (NetworkUtils.checkNetState(this)) {
            SignScanPhone(mPhone);
        }
        else{
SignScanPhoneOffLine(mPhone);
        }
        break;
}
    }
    public void resultDialog(String status,String message){
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(status)
                .setMessage(message)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                    }
                })

                .setCancelable(true)
                .show();
    }

    private void SignScanPhoneOffLine(String result) {
        List<MySignListupBean> list = dao.queryListByPhoneAndCheckInID(result, CheckInID);
        List<MySignListupBean> list2 = dao.queryListStatusByPhone(result, CheckInID, "true");
        if (list.size() == 0) {
//                            ToastUtil.makeText(getActivity(), "扫码失败，该凭证码有误");
            resultDialog("签到失败！","该手机号不存在报名记录！");
        } else if (list2.size() == 1) {
            MySignListupBean bean = (MySignListupBean) list.get(0);
            String Name = bean.getName();
            String Phone = bean.getPhone();
            String AdminRemark = bean.getAdminRemark();
            String FeeName = bean.getFeeName();
            String Fee = bean.getFee();
            String FeeStr = FeeName + "：" + Fee;
            if (FeeName == null) {
                FeeStr = "";
            }
            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                            ToastUtil.makeText(getActivity(), "该用户已经签到！" + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
            resultDialog("该用户已经签到！","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                    "备注：" + AdminRemark + "\n" + FeeStr);
        } else {
            MySignListupBean bean = new MySignListupBean();
            bean.setPhone(result);
            bean.setStatus("true");
            bean.setUpdateStatus("true");
            bean.setCheckInID(CheckInID);
            dao.updateByPhone(bean);
            MySignListupBean bean2 = (MySignListupBean) list.get(0);
            String Name = bean2.getName();
            String Phone = bean2.getPhone();
            String AdminRemark = bean2.getAdminRemark();
            String FeeName = bean2.getFeeName();
            String Fee = bean2.getFee();
            String FeeStr = FeeName + "：" + Fee;
            if (FeeName == null) {
                FeeStr = "";
            }
            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                            ToastUtil.makeText(getActivity(), "扫码成功" + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
            resultDialog("扫码成功！","姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                    "备注：" + AdminRemark + "\n" + FeeStr);

        }
    }

    private void initHandler() {
        mHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_SIGN_SCAN_PHONE:
                        String result3 = (String) msg.obj;
                        JSONObject jsonObj = JSON.parseObject(result3);
                        String message = jsonObj.getString("Msg");
                        if (jsonObj.getString("Res").equals("0")) {
                            JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));
                            String Name = jsonObject2.getString("Name");
                            String Phone = jsonObject2.getString("Phone");
                            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                            String AdminRemark = jsonObject2.getString("AdminRemark");
                            if (AdminRemark == null) {
                                AdminRemark = "无";
                            }
                            String FeeName = jsonObject2.getString("FeeName");
                            String Fee = jsonObject2.getString("Fee");
                            String FeeStr = FeeName + "：" + Fee;
                            if (FeeName == null) {
                                FeeStr = "";
                            }
//                            ToastUtil.makeText(getActivity(), message + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
                            resultDialog(message,"姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
                                    "备注：" + AdminRemark + "\n" + FeeStr);

                        }

//                        else  if (jsonObj.getString("Res").equals("1")) {
//                            JSONObject jsonObject2 = JSON.parseObject(jsonObj.getString("Data"));
//                            String Name = jsonObject2.getString("Name");
//                            String Phone = jsonObject2.getString("Phone");
//                            String newPhone = Phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//                            String AdminRemark = jsonObject2.getString("AdminRemark");
//                            if (AdminRemark == null) {
//                                AdminRemark = "无";
//                            }
//                            String FeeName = jsonObject2.getString("FeeName");
//                            String Fee = jsonObject2.getString("Fee");
//                            String FeeStr = FeeName + "：" + Fee;
//                            if (FeeName == null) {
//                                FeeStr = "";
//                            }
////                            ToastUtil.makeText(getActivity(), message + "\n" + "姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
////                                    "备注：" + AdminRemark + "\n" + FeeStr);
//                            resultDialog(message,"姓名：" + Name + "\n" + "电话：" + newPhone + "\n" +
//                                    "备注：" + AdminRemark + "\n" + FeeStr);
//                        }
                    else
                        {
                            resultDialog("签到失败！",message);
//                            ToastUtil.makeText(getActivity(), message);
                        }
                            ;


                break;

                default:
                break;
            }
        }
    };


}

}

class IsChineseOrNot {

    public static final boolean isChineseCharacter(String chineseStr) {
        char[] charArray = chineseStr.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            // 是否是Unicode编码,除了"�"这个字符.这个字符要另外处理
            if ((charArray[i] >= '\u0000' && charArray[i] < '\uFFFD') || ((charArray[i] > '\uFFFD' && charArray[i] < '\uFFFF'))) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    public static final boolean isSpecialCharacter(String str) {
        // 是"�"这个特殊字符的乱码情况
        if (str.contains("ï¿½")) {
            return true;
        }
        return false;
    }

}