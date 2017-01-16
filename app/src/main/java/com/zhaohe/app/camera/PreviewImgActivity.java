package com.zhaohe.app.camera;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhaohe.zhundao.R;
import com.zhaohe.zhundao.constant.Constant;


/**
 *@Description: 预览 图片
 *@Author:杨攀
 *@Since:2014年8月9日下午5:46:09
 */
public class PreviewImgActivity extends Activity {

     
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        // 设置无标题
        requestWindowFeature (Window.FEATURE_NO_TITLE);
        setContentView (R.layout.activity_previewimg);

        ImageView imgView = (ImageView) this.findViewById (R.id.large_image);
        String imgpath = this.getIntent ().getStringExtra ("imgpath");// 图片路径
        
        if (imgpath.indexOf ("/") >= 0) { //“/”开头，表示图片路径, 在手机上存在的图片
            imgView.setImageURI (Uri.parse (imgpath));
        } else {
            StringBuffer imageUrl = new StringBuffer ();
            imageUrl.append (Constant.HOST).append (Constant.Url.IMG_DOWNLOAD).append ("?isthumb=false&imgid=").append (imgpath);
            //网络请求图片
            Picasso.with(this).load(imageUrl.toString ()).placeholder(R.mipmap.default_error).error(R.mipmap.default_error).noFade().into(imgView);
        }
    }
     

    @Override
    protected void onDestroy(){
        super.onDestroy ();
    }
}
