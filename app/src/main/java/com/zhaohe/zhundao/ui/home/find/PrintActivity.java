package com.zhaohe.zhundao.ui.home.find;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.zhaohe.app.utils.SPUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.gprinter.PrinterConnectDialog;
import com.zhaohe.zhundao.ui.ToolBarActivity;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gprinter.service.GpPrintService.CONNECT_STATUS;
import static com.zhaohe.zhundao.gprinter.ListViewAdapter.DEBUG_TAG;

public class PrintActivity extends ToolBarActivity {

    @BindView(R.id.sw_print)
    Switch swPrint;
    @BindView(R.id.sp_size)
    Spinner spSize;
    @BindView(R.id.sp_model)
    Spinner spModel;
    @BindView(R.id.sp_type)
    Spinner spType;
    @BindView(R.id.tv_bluetooth)
    TextView tvBluetooth;
    @BindView(R.id.btn_bluetooth_submit)
    Button btnBluetoothSubmit;
    @BindView(R.id.et_x)
    EditText etX;
    @BindView(R.id.et_y)
    EditText etY;
    @BindView(R.id.btn_print_test)
    Button btnPrintTest;
    private GpService mGpService;
    private PrinterServiceConnection conn = null;
    private int mPrinterIndex = 0;
    private int mTotalCopies = 0;
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    private static final int REQUEST_PRINT_LABEL = 0xfd;
    private static final int REQUEST_PRINT_RECEIPT = 0xfc;
    private String Vcode="q298387",name="测试",Remark="无";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print);
        initToolBarNew("打印配置",R.layout.activity_print);
        ButterKnife.bind(this);
//        initActionBar();
        initView();
        connection();
        registerReceriver();

    }

    @OnClick(R.id.btn_print_test)
    public void onViewClicked() {

        print();
    }
    private void print() {
        try {

            mGpService.queryPrinterStatus(0, 1000, REQUEST_PRINT_LABEL);

        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
            mBroadcastReceiver = null;
        }
    }

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.i(DEBUG_TAG, "onServiceDisconnected() called");
            mGpService = null;
        }


        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    private void initView() {
        swPrint.setChecked((Boolean) SPUtils.get(this, "is_print", false));
        spType.setSelection((Integer) SPUtils.get(this, "type", 0));
        spSize.setSelection((Integer) SPUtils.get(this, "size", 0));
        spModel.setSelection((Integer) SPUtils.get(this, "model", 0));
        etX.setText(SPUtils.get(this, "x", 0)+"");
        etY.setText(SPUtils.get(this, "y", 0)+"");

    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Log.i(DEBUG_TAG, "connection");
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //当点击不同的menu item 是执行不同的操作
        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_bluetooth_submit, R.id.tv_bluetooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bluetooth_submit:

                SPUtils.put(this, "model", spModel.getSelectedItemPosition());
                SPUtils.put(this, "size", spSize.getSelectedItemPosition());
                SPUtils.put(this, "type", spType.getSelectedItemPosition());
                SPUtils.put(this, "is_print", swPrint.isChecked());

                System.out.println(SPUtils.get(this, "size", 0));
                System.out.println(SPUtils.get(this, "model", 0));
                System.out.println(SPUtils.get(this, "type", 0));
                System.out.println(SPUtils.get(this, "is_print", false));
                if(!isNumeric(etX.getText().toString()))
                {
                    ToastUtil.makeText(this, "x坐标有误，请重新输入");
                    return;

                }if(!isNumeric(etY.getText().toString()))
            {
                ToastUtil.makeText(this, "y坐标有误，请重新输入");
                return;

            }

                SPUtils.put(this, "x", Integer.parseInt(etX.getText().toString()));
                SPUtils.put(this, "y", Integer.parseInt(etY.getText().toString()));
                ToastUtil.makeText(this, "打印配置保存成功");


                break;
            case R.id.tv_bluetooth:
                Intent intent = getIntent();
                boolean[] state = getConnectState();
                intent.putExtra(CONNECT_STATUS, state);
                intent.setClass(this, PrinterConnectDialog.class);
                this.startActivity(intent);
                break;
        }
    }
    public boolean isNumeric(String str){
//        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
//        Matcher isNum = pattern.matcher(str);
//        if( !isNum.matches() ){
//            return false;
//        }
//        return true;
        if (str.equals("")||str.equals("-")){
            return false;
        }
        else return true;
    }
    private void registerReceriver() {
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        /**
         * 票据模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus()，在打印完成后会接收到
         * action为GpCom.ACTION_DEVICE_STATUS的广播，特别用于连续打印，
         * 可参照该sample中的sendReceiptWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_RECEIPT_RESPONSE));
        /**
         * 标签模式下，可注册该广播，在需要打印内容的最后加入addQueryPrinterStatus(RESPONSE_MODE mode)
         * ，在打印完成后会接收到，action为GpCom.ACTION_LABEL_RESPONSE的广播，特别用于连续打印，
         * 可参照该sample中的sendLabelWithResponse方法与广播中的处理
         **/
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_LABEL_RESPONSE));
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d("TAG", action);
            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机正常";
                    } else {
                        str = "打印机 ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "脱机";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }

                    Toast.makeText(getApplicationContext(), "打印机：" + mPrinterIndex + " 状态：" + str, Toast.LENGTH_SHORT)
                            .show();
                } else if (requestCode == REQUEST_PRINT_LABEL) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        switch (SPUtils.get(getApplicationContext(),"model",0)+""){
                            case "0":
                                sendLabelVocde();
                                break;
                            case "1":
                                sendLabelVocdeAndName();
                                break;
                            case "2":
                                sendLabelNameAndRemark();
                                break;

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_PRINT_RECEIPT) {
                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    if (status == GpCom.STATE_NO_ERR) {
                        sendReceipt();
                    } else {
                        Toast.makeText(getApplicationContext(), "query printer status error", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (action.equals(GpCom.ACTION_RECEIPT_RESPONSE)) {
                if (--mTotalCopies > 0) {
                    sendReceiptWithResponse();
                }
            } else if (action.equals(GpCom.ACTION_LABEL_RESPONSE)) {
                byte[] data = intent.getByteArrayExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE);
                int cnt = intent.getIntExtra(GpCom.EXTRA_PRINTER_LABEL_RESPONSE_CNT, 1);
                String d = new String(data, 0, cnt);
                /**
                 * 这里的d的内容根据RESPONSE_MODE去判断返回的内容去判断是否成功，具体可以查看标签编程手册SET
                 * RESPONSE指令
                 * 该sample中实现的是发一张就返回一次,这里返回的是{00,00001}。这里的对应{Status,######,ID}
                 * 所以我们需要取出STATUS
                 */
                Log.d("LABEL RESPONSE", d);

                if (--mTotalCopies > 0 && d.charAt(1) == 0x00) {
                    sendLabelWithResponse();
                }
            }
        }
    };
    void sendReceipt() {

        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("Print text\n"); // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        // esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

		/* 打印图片 */
//        esc.addText("Print bitmap!\n"); // 打印文字
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        esc.addRastBitImage(b, ic_launcher, 0); // 打印图片

		/* 打印一维条码 */
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
        esc.addText("Print QRcode\n"); // 打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendReceiptWithResponse() {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("Sample\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("Print text\n"); // 打印文字
        esc.addText("Welcome to use SMARNET printer!\n"); // 打印文字

		/* 打印繁体中文 需要打印机支持繁体字库 */
        String message = "佳博智匯票據打印機\n";
        // esc.addText(message,"BIG5");
        esc.addText(message, "GB2312");
        esc.addPrintAndLineFeed();

		/* 绝对位置 具体详细信息请查看GP58编程手册 */
        esc.addText("智汇");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 6);
        esc.addText("网络");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("设备");
        esc.addPrintAndLineFeed();

		/* 打印图片 */
        // esc.addText("Print bitmap!\n"); // 打印文字
        // Bitmap b = BitmapFactory.decodeResource(getResources(),
        // R.drawable.gprinter);
        // esc.addRastBitImage(b, ic_launcher, 0); // 打印图片

		/* 打印一维条码 */
        esc.addText("Print code128\n"); // 打印文字
        esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        esc.addSetBarcodeWidth((byte) 1); // 设置条码单元宽度为1
        esc.addCODE128(esc.genCodeB("SMARNET")); // 打印Code128码
        esc.addPrintAndLineFeed();

		/*
		 * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
		 */
        esc.addText("Print QRcode\n"); // 打印文字
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); // 设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);// 设置qrcode模块大小
        esc.addStoreQRCodeData("www.smarnet.cc");// 设置qrcode内容
        esc.addPrintQRCode();// 打印QRCode
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印左对齐
        esc.addText("Completed!\r\n"); // 打印结束
        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 8);

        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(mPrinterIndex, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void sendLabelVocdeAndName() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区


        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addQRCode(55, 60, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,160 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;
            case "1":
                tsc.addQRCode(35, 40, LabelCommand.EEC.LEVEL_L, 6, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,180 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;
            case "2":

                tsc.addQRCode(10, 30, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));
                tsc.addText(55,210 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        (String) SPUtils.get(this,"print_name","测试"));
                break;

        }


//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    void sendLabelNameAndRemark() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区


        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addText(30,40 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1, "姓名："+ (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(30,100 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                        "备注："+(String) SPUtils.get(this,"print_AdminRemark","无"));
                break;
            case "1":
                tsc.addText(20,30 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                        (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(20,110 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_2, LabelCommand.FONTMUL.MUL_2,
                        (String) SPUtils.get(this,"print_AdminRemark","无"));
                break;
            case "2":

                tsc.addText(10,20 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                        (String) SPUtils.get(this,"print_name","测试"));
                tsc.addText(10,130 , LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_3, LabelCommand.FONTMUL.MUL_3,
                        (String) SPUtils.get(this,"print_AdminRemark","无"));
                break;

        }


//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    void sendLabelVocde() {
        LabelCommand tsc = new LabelCommand();
//        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference((Integer) SPUtils.get(this, "x", 0), (Integer) SPUtils.get(this, "y", 0));// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        switch (SPUtils.get(this,"size",0)+""){
            case"0":
                tsc.addQRCode(55, 80, LabelCommand.EEC.LEVEL_L, 4, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;
            case "1":
                tsc.addQRCode(35, 50, LabelCommand.EEC.LEVEL_L, 6, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;
            case "2":
                tsc.addQRCode(10, 30, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, (String) SPUtils.get(this,"print_Vcode","q298387"));

                break;

        }



//		tsc.addQRCode(17, 20, LabelCommand.EEC.LEVEL_L, 8, LabelCommand.ROTATION.ROTATION_0, " 55555");

//        tsc.addText(20, 30, FONTTYPE.KOREAN, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "조선말");
//        tsc.addText(100, 30, FONTTYPE.SIMPLIFIED_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "简体字");
//        tsc.addText(180, 30, FONTTYPE.TRADITIONAL_CHINESE, ROTATION.ROTATION_0, FONTMUL.MUL_1, FONTMUL.MUL_1,
//                "繁體字");

        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 60, BITMAP_MODE.OVERWRITE, b.getWidth(), b);
//	    //绘制二维码
//        tsc.addQRCode(105, 75, EEC.LEVEL_L, 5, ROTATION.ROTATION_0, " www.smarnet.cc");
//        // 绘制一维条码
//        tsc.add1DBarcode(50, 350, BARCODETYPE.CODE128, 100, READABEL.EANBEL, ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
//		tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    void sendLabelWithResponse() {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(60, 60); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(0); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.BACKWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        // 开启带Response的打印，用于连续打印
        tsc.addQueryPrinterStatus(LabelCommand.RESPONSE_MODE.ON);
        tsc.addReference(0, 0);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(20, 20, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "Welcome to use SMARNET printer!");
        // 绘制图片
//        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.gprinter);
//        tsc.addBitmap(20, 50, BITMAP_MODE.OVERWRITE, b.getWidth(), b);

        tsc.addQRCode(250, 80, LabelCommand.EEC.LEVEL_L, 5, LabelCommand.ROTATION.ROTATION_0, " www.smarnet.cc");
        // 绘制一维条码
        tsc.add1DBarcode(20, 250, LabelCommand.BARCODETYPE.CODE128, 100, LabelCommand.READABEL.EANBEL, LabelCommand.ROTATION.ROTATION_0, "SMARNET");
        tsc.addPrint(1, 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);


        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return state;
    }
}
