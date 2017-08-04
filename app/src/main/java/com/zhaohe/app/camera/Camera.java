package com.zhaohe.app.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zhaohe.app.camera.multimgselector.MultiImageSelectorActivity;
import com.zhaohe.app.commons.dialog.DialogUtils;
import com.zhaohe.app.commons.http.service.FormFile;
import com.zhaohe.app.utils.DensityUtil;
import com.zhaohe.app.utils.FileUtils;
import com.zhaohe.app.utils.ImageUtils;
import com.zhaohe.app.utils.MD5;
import com.zhaohe.app.utils.ProgressDialogUtils;
import com.zhaohe.app.utils.StringUtils;
import com.zhaohe.app.utils.ToastUtil;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.asynctask.AsyncImageDeleteTask;
import com.zhaohe.zhundao.asynctask.photoUpload.AsyncPhotoUpload;
import com.zhaohe.zhundao.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:相机 拍照 和 选照片  组件
 * @Author:杨攀
 * @Since:2015年11月3日下午4:55:10
 */
public class Camera {

    private static final String TAB = "Camera";
    private  ArrayList<String> upload=new ArrayList<>();
   private Dialog dialog;
private int uploadSize=-1;
    private GridLayout gl_camera;
    //图片标题
    private String title;


    /**
     * @Fields saveDir : 存放照片的文件夹
     */
    public static String SAVEDIR = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FileDir.IMG;

    /**
     * @Fields BUNDLE_IMGTHUM_PATH : BUNDLE
     */
    public static String BUNDLE_IMGTHUM_PATH = "bundle_imgthum_path";
    public static String ADD_CAMERA = "add_camera";
    /**
     * @Fields REQUEST_IMAGE : 拍照请求码
     */
    private static final int REQUEST_IMAGE = 2000;

    private static final int COMPRESSION_SUCCESS = 1;

    private static final int COMPRESSION_FAIL = -1;
    public static final int MESSAGE_UPLOAD_PHOTO = 95;


    /**
     * @Fields square_size : 缩略图的图片的宽度
     */
    private int square_size = 1000;

    /**
     * @Fields quality : 缩略图的图片质量
     */
    private int quality = 90;

    private ImageView addImgView;
    /**
     * @Fields MAX_SELECT_COUNT : 最大照片数
     */
    public static int max_select_count = 9;
private Handler uploadHandler;
    private Activity mActivity;
    private Fragment mFragment;
    private GridLayout mGl_camera;
    private ImgViewOnClickListener onClickListener;
    private ImgViewOnLongClickListener longClickListener;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == COMPRESSION_SUCCESS && msg.obj != null) {
                Bundle bundle = msg.getData();
                String path = bundle.getString(BUNDLE_IMGTHUM_PATH);
                uploadPhoto( getFormFile(path));


                // 显示图片
                showImage2View(path, (Bitmap) msg.obj);

            }
//            else {
//                Toast.makeText(mActivity, R.string.camera_photo_compression_image, Toast.LENGTH_SHORT)
//                        .show();
//            }
            if (msg.what==MESSAGE_UPLOAD_PHOTO){
                String result = (String) msg.obj;
                JSONObject jsonObj = JSON.parseObject(result);
                String Url=jsonObj.getString("url");
                String message = jsonObj.getString("Res");
                upload.add(Url);
//                if (uploadSize!=-1){
//                    uploadSize--;
//                }
//                if (uploadSize==0&&uploadHandler!=null&&dialog!=null){
//                    dialog.dismiss();
//                    ToastUtil.makeText(mActivity,"上传图片成功");
//                }
                ToastUtil.print("还需上传数量"+--uploadSize);
                if (uploadSize==0){
                    dialog.dismiss();
                     msg = uploadHandler.obtainMessage(1000);
                    uploadHandler.sendMessage(msg);

                }
            }

        }
    };

    /**
     * <p>Description: </p> activity 中，拍照的构造函数
     *
     * @param activity
     * @param gl_camera 显示照片小图的 gridview
     */
    public Camera(Activity activity, GridLayout gl_camera) {
        this(activity, gl_camera, false);
    }

    /**
     * <p>Description: </p> fragment 中，拍照的构造函数
     *
     * @param fragment
     * @param gl_camera 显示照片小图的 gridview
     */
    public Camera(Fragment fragment, GridLayout gl_camera) {
        this.mFragment = fragment;
        this.mActivity = fragment.getActivity();
        this.mGl_camera = gl_camera;

        init(false);
    }

    /**
     * <p>Description: </p>
     *
     * @param activity  当前的activty
     * @param gl_camera 显示照片小图的 gridview
     */
    public Camera(Activity activity, GridLayout gl_camera, boolean isShow  ) {
        this.mActivity = activity;
        this.mGl_camera = gl_camera;
        init(isShow);
    }
//设置选择的图片数量
    public Camera(Activity activity, GridLayout gl_camera, boolean isShow ,int max_num ) {
        this.mActivity = activity;
        this.mGl_camera = gl_camera;
        max_select_count=max_num;
        init(isShow);
    }

    public Camera(Activity activity, GridLayout gl_camera, boolean isShow ,int max_num ,Handler handler,String title) {
        this.mActivity = activity;
        this.mGl_camera = gl_camera;
        max_select_count=max_num;
        init(isShow);
        uploadHandler=handler;
        this.title=title;
    }
    public   ArrayList<String> getUploadUrl() {
        // 照片上传
        return upload;
    }
    public  void clearUpload(){
        upload.clear();
    }

    /**
     * @param isShow - true：只是显示照片， false: 需要拍照
     * @Description: 初始化
     * @Author:杨攀
     * @Since: 2015年11月17日上午9:14:07
     */
    private void init(boolean isShow) {

        setGridlayoutColumnCount(mActivity);
        onClickListener = new ImgViewOnClickListener();
        if (!isShow) {//如果要拍照
            // 初始化数据
            createFileDir();
//            AddViewOnClickListener listener = new AddViewOnClickListener();

            longClickListener = new ImgViewOnLongClickListener();
            // 显示添加 “+” View
            AddViewOnTouchClickListener listener =new AddViewOnTouchClickListener();
            addImgView = showAddView(mActivity, listener);
            mGl_camera.addView(addImgView);
        }
    }

    /**
     * @param activity
     * @Description: 设置每行显示的图片数量
     * @Author:杨攀
     * @Since: 2015年11月4日下午3:35:23
     */
    private void setGridlayoutColumnCount(Activity activity) {
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        // 每行个数
        mGl_camera.setColumnCount(width / (ImageUtils.getImageWidth(activity) + DensityUtil.dip2px(activity, ImageUtils.GRIDLAYOUT_PADDING_DP)));
    }

    /**
     * @Description: “+” View 点击，选择照片或拍照
     * @Author:杨攀
     * @Since:2015年11月4日上午11:03:38
     */

    private final class AddViewOnTouchClickListener implements View.OnTouchListener{

        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {

            int selectCount = 0;
            for (int i = 0; i < mGl_camera.getChildCount(); i++) {
                View view = mGl_camera.getChildAt(i);
                if (!ADD_CAMERA.equals(view.getTag())) {
                    selectCount++;
                }
            }
            selectImage(selectCount);
            return false;
        }
    }


    private final class AddViewOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            // 判断已经选择的照片数量
            int selectCount = 0;
            for (int i = 0; i < mGl_camera.getChildCount(); i++) {
                View view = mGl_camera.getChildAt(i);
                if (!ADD_CAMERA.equals(view.getTag())) {
                    selectCount++;
                }
            }
            selectImage(selectCount);
        }
    }

    /**
     * @Description: 长 按 删除
     * @Author:杨攀
     * @Since:2015年11月4日下午3:36:42
     */
    private final class ImgViewOnLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(final View view) {

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(R.string.app_dialog_title);
            builder.setMessage(R.string.camera_photo_delete_image);
            builder.setPositiveButton(R.string.app_dialog_ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String imgpath = view.getTag(R.id.a) + "";
                    int position= (int) view.getTag(R.id.b);
                    upload.remove(position);
                    Message msg = uploadHandler.obtainMessage(999);
                    msg.obj=title;
                    uploadHandler.sendMessage(msg);
                    if (imgpath.indexOf("/") >= 0) {// 本地图片
                        mGl_camera.removeView(view);// 删除图片
                        int count = max_select_count - 1;
                        if (mGl_camera.getChildCount() == count && addImgView.getParent() == null) {
                            // 添加一个 addView
                            mGl_camera.addView(addImgView);
                        }
                    } else {
                        AsyncImageDeleteTask asyncImageDeleteTask = new AsyncImageDeleteTask(mGl_camera, addImgView, view, imgpath);
                        asyncImageDeleteTask.execute();
                    }
                }
            });
            builder.setNegativeButton(R.string.app_dialog_cancel, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

            return false;
        }
    }

    /**
     * @Description: 点击查看大图
     * @Author:杨攀
     * @Since:2015年11月4日下午3:37:35
     */
    public final class ImgViewOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("imgpath", v.getTag(R.id.a) + "");
            intent.setClass(mActivity, PreviewImgActivity.class);
            mActivity.startActivity(intent);
        }

    }

    /**
     * @param activity
     * @param listener
     * @return
     * @Description: 显示 “+” View, 选择照片或拍照
     * @Author:杨攀
     * @Since: 2015年11月4日下午2:15:32
     */
    private ImageView showAddView(Activity activity, OnClickListener listener) {
        ImageView imgView = createView(activity);
        imgView.setTag(ADD_CAMERA);
        imgView.setOnClickListener(listener);
        imgView.setBackgroundResource(R.drawable.ic_add_camera);
        return imgView;
    }
    private ImageView showAddView(Activity activity, View.OnTouchListener listener) {
        ImageView imgView = createView(activity);
        imgView.setTag(ADD_CAMERA);
        imgView.setOnTouchListener(listener);
        imgView.setBackgroundResource(R.drawable.ic_add_camera);
        return imgView;
    }
    /**
     * @param path
     * @param bitmap
     * @param
     * @Description: 创建ImageView用于 显示选择了的 图片
     * @Author:杨攀
     * @Since: 2015年11月4日下午2:39:15
     */
    private ImageView createImageView(String path, Bitmap bitmap ,int position) {
        ImageView imgView = createView(mActivity);
        // 长按删除图片
        imgView.setOnLongClickListener(longClickListener);
        imgView.setOnClickListener(onClickListener);
        imgView.setTag(R.id.a,path);
        imgView.setTag(R.id.b,position);
        imgView.setImageBitmap(bitmap);
        return imgView;
    }

    /**
     * @param path
     * @param
     * @Description: 显示图片到 view 上
     * @Author:杨攀
     * @Since: 2015年11月4日下午2:26:26
     */
    private void showImage2View(String path, Bitmap bitmap) {

        int childCount = mGl_camera.getChildCount();
        int position = childCount - 1;

        if (max_select_count <= childCount) {// 超过图片数量，则删除 “+” 的 ImageView
            mGl_camera.removeViewAt(position);
        }
        ImageView imgView = createImageView(path, bitmap,position);
        mGl_camera.addView(imgView, position);
    }
    private void showImageInternetView(final String path) {

        int childCount = mGl_camera.getChildCount();
        final int position = childCount - 1;
        if (max_select_count <= childCount) {// 超过图片数量，则删除 “+” 的 ImageView
            mGl_camera.removeViewAt(position);
        }
        Picasso.with(mActivity)
                .load(path)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ImageView   imgView = createImageView(path, bitmap,position);
                          mGl_camera.addView(imgView, position);

                    }


                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }
    public void showInternet(String path){
        String[] imgurl = path.split("\\|");
        for (int i=0;i<imgurl.length;i++)
        {showImageInternetView(imgurl[i]);
        upload.add(imgurl[i]);
        }

    }


    /**
     * @Description: 检查图片数量，判断是否需要删除 “+” 的 ImageView
     * @Author:杨攀
     * @Since: 2015年11月4日下午3:40:12
     */
    private void checkImgViewCount() {

        int childCount = mGl_camera.getChildCount();
        int imgCount = 0;
        for (int i = 0; i < childCount; i++) {
            View view = mGl_camera.getChildAt(i);
            if (!ADD_CAMERA.equals(view.getTag())) {
                imgCount++;
            }
        }

        // 显示图片的时，如果图片数量大于或等于最大数，则删除“+” 的 ImageView
        if (imgCount >= max_select_count) {
            mGl_camera.removeView(addImgView);
        }
    }

    /**
     * @param activity
     * @return
     * @Description: 生成显示图片的 View
     * @Author:杨攀
     * @Since: 2015年11月4日上午11:16:53
     */
    private ImageView createView(Activity activity) {
        ImageView imgView = new ImageView(activity);
        LayoutParams params = new LayoutParams();
        params.width = ImageUtils.getImageWidth(activity);
        params.height = ImageUtils.getImageHeight(activity);
        params.rightMargin = DensityUtil.dip2px(activity, ImageUtils.GRIDLAYOUT_PADDING_DP);
        imgView.setScaleType(ScaleType.FIT_XY);
        imgView.setLayoutParams(params);
        return imgView;
    }

    /**
     * @Description: 判断路径是否存在, 不存在则创建
     * @Author:杨攀
     * @Since: 2015年11月4日上午9:41:30
     */
    private void createFileDir() {
        File filedir = new File(SAVEDIR);
        if (!filedir.exists()) {
            filedir.mkdirs();
        }
    }

    /**
     * @param selectCount 已经选择/拍照的照片数
     * @Description: 点击选择照片或拍照
     * @Author:杨攀
     * @Since: 2015年11月4日上午10:57:40
     */
    public void selectImage(int selectCount) {

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {// 判断手机是否装载SDCard
            // 可选照片数 = 最大照片数 - 已选照片数
            int extra_select_count = max_select_count - selectCount;

            Intent intent = new Intent(mActivity, MultiImageSelectorActivity.class);
            // 是否显示调用相机拍照
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
            // 最大图片选择数量
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, extra_select_count);
            // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);

            if (mFragment == null) {
                mActivity.startActivityForResult(intent, REQUEST_IMAGE);
            } else {
                mFragment.startActivityForResult(intent, REQUEST_IMAGE);
            }

        } else {
            // Sdcard 不存在
            DialogUtils.showDialogViewFinish(mActivity, R.string.app_sdcardnotexist);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                dialog= ProgressDialogUtils.showProgressDialog(mActivity,"正在上传中","请稍后");
                uploadSize=path.size();
                for (int i = 0; i < path.size(); i++) {
                    // 处理照片 - 压缩
                    System.out.println(path.get(i));
//                    uploadPhoto( getFormFile(path.get(i)));
                    CompressionRunnable runnable = new CompressionRunnable(path.get(i));
                    new Thread(runnable).start();

                }
            }
    }

    }
    public void uploadPhoto(FormFile[] flie) {
        AsyncPhotoUpload async = new AsyncPhotoUpload(mActivity.getApplicationContext(), mHandler, MESSAGE_UPLOAD_PHOTO,flie);
        async.execute();}
    /**
     * @Description: 压缩图片线程
     * @Author:杨攀
     * @Since:2015年11月3日下午5:04:19
     */
    private final class CompressionRunnable implements Runnable {

        private String mImgPath;

        public CompressionRunnable(String imgPath) {
            this.mImgPath = imgPath;
        }

        @Override
        public void run() {

            // 生成显示在 view 的小图
            Bitmap bitmap = ImageUtils.loadImgThumbnail(mImgPath, ImageUtils.getImageWidth(mActivity), ImageUtils.getImageHeight(mActivity));

            String imgthum_path = compression(mImgPath);

            Message msg = new Message();

            if (imgthum_path != null) {
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_IMGTHUM_PATH, imgthum_path);
                msg.setData(bundle);
                msg.obj = bitmap;
                msg.what = COMPRESSION_SUCCESS;
            } else {
                msg.what = COMPRESSION_FAIL;
            }

            mHandler.sendMessage(msg);
        }

    }

    /**
     * @param imgpath - 选择照片或拍照返回的原始图片绝对路径
     * @return - 图片路径，如果异常则返回null
     * @Description: 压缩图片 - 生成要上传的缩略图
     * @Author:杨攀
     * @Since: 2015年11月4日上午10:09:03
     */
    private String compression(String imgpath) {

        // 根据原始图片的路径 生成文件名称
        String fileName = MD5.getMD5(imgpath);
        // 获取文件的扩展名称
        String formatName = FileUtils.getFileFormat(imgpath);

        // 组装文件存放路径
        StringBuffer filePath = new StringBuffer();
        filePath.append(SAVEDIR).append(fileName).append(".").append(formatName);

        File imgPath = new File(filePath.toString());

        try {
            // 判断是否已存在缩略图
            if (!imgPath.exists()) {
                // 压缩上传的图片
                ImageUtils.createImageThumbnail(null, imgpath, filePath.toString(), square_size, quality);
            }

            return filePath.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param imgUuids      多个图片id, 逗号分隔开
     * @param imgThumbUuids 多个缩略图片id, 逗号分隔开
     * @Description: 显示图片
     * @Author:杨攀
     * @Since: 2015年11月4日下午4:02:24
     */
    public void showImage(String imgUuids, String imgThumbUuids) {

        if (StringUtils.isNotEmpty(imgUuids)) {// 如果有图片

            String[] imguuid = imgUuids.split(",");
            String[] imgThumbuuid = imgThumbUuids.split(",");

            StringBuffer imageUrl = new StringBuffer();
            imageUrl.append(Constant.HOST).append(Constant.Url.IMG_DOWNLOAD).append("?isthumb=true&imgid=");
            for (int i = 0; i < imguuid.length; i++) {

                ImageView imageView = createView(mActivity);
                imageView.setTag(imguuid[i]);
                imageView.setOnClickListener(onClickListener);
                mGl_camera.addView(imageView);
                try {
                    String tmpImageUrl = imageUrl + imgThumbuuid[i];

                    Picasso.with(mActivity).load(tmpImageUrl).placeholder(R.mipmap.default_error).error(R.mipmap.default_error)
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            checkImgViewCount();
        }
    }

    public void showImageNew(String imgUuids) {

        if (StringUtils.isNotEmpty(imgUuids)) {// 如果有图片

            String[] imguuid = imgUuids.split("//|");

            StringBuffer imageUrl = new StringBuffer();
            for (int i = 0; i < imguuid.length; i++) {

                ImageView imageView = createView(mActivity);
                imageView.setTag(imguuid[i]);
                imageView.setOnClickListener(onClickListener);
                mGl_camera.addView(imageView);
                try {

                    Picasso.with(mActivity).load(imguuid[i]).placeholder(R.mipmap.default_error).error(R.mipmap.default_error)
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            checkImgViewCount();
        }
    }

    /**
     * @param imgPath
     * @Description: 显示本地图片地址
     * @Author:杨攀
     * @Since: 2015年11月4日下午4:41:26
     */
    private void showImage(String imgPath) {
        if (StringUtils.isNotEmpty(imgPath)) {

            ImageView imageView = createView(mActivity);
            imageView.setTag(imgPath);

            Picasso.with(mActivity).load(new File(imgPath)).placeholder(R.mipmap.default_error).error(R.mipmap.default_error).into(imageView);

            checkImgViewCount();
        }
    }

    /**
     * @return
     * @Description: 获取拍照后的 照片上传 form
     * @Author:杨攀
     * @Since: 2014年10月24日下午2:10:56
     */
    public FormFile[] getFormFile() {
        // 照片上传
        List<FormFile> list = new ArrayList<FormFile>();
        int count = mGl_camera.getChildCount();
        for (int i = 0; i < count; i++) {// + 号的图片不计算在内
            View imgview = mGl_camera.getChildAt(i);
            String filePath = imgview.getTag() + "";
            if (filePath.indexOf("/") >= 0) {// “/”开头，表示图片路径，是新添加的照片，需要上传
                File file = new File(filePath);
                FormFile formfile = new FormFile(file, Constant.IMAGEFILE, filePath, null);
                list.add(formfile);
            }
        }

        FormFile[] files = new FormFile[list.size()];

        return list.toArray(files);
    }

    /**
     * @return
     * @Description: 获取拍照后的 照片上传 form
     * @Author:杨攀
     * @Since: 2014年10月24日下午2:10:56
     */
    public static final FormFile[] getFormFile(String imgPaths) {
        // 照片上传
        List<FormFile> list = new ArrayList<FormFile>();

        if (StringUtils.isNotEmpty(imgPaths)) {
            String[] imgPath = imgPaths.split(",");
            for (int i = 0; i < imgPath.length; i++) {
                String filePath = imgPath[i];
                if (filePath.indexOf("/") >= 0) {// “/”开头，表示图片路径，是新添加的照片，需要上传
                    File file = new File(filePath);
                    FormFile formfile = new FormFile(file, Constant.IMAGEFILE, filePath, null);
                    list.add(formfile);
                }
            }
        }

        FormFile[] files = new FormFile[list.size()];
        return list.toArray(files);
    }




}
