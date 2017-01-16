package com.zhaohe.zhundao.asynctask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.GridLayout;

import com.zhaohe.app.camera.Camera;
import com.zhaohe.app.commons.http.HttpUtil;
import com.zhaohe.app.utils.JSONUtils;
import com.zhaohe.zhundao.constant.Constant;

import java.util.HashMap;
import java.util.Map;


/**
 *@Description:删除照片
 *@Author:邹苏启
 *@Since:2015-1-4下午2:30:02  
 */
public class AsyncImageDeleteTask extends AsyncTask<String, Integer, String> {

    private GridLayout glCamara;
    private View       addView;
    private String     imageId;
    private View       delView;

    public AsyncImageDeleteTask(GridLayout gl, View addView, View delView, String ivId) {
        this.glCamara = gl;
        this.addView = addView;
        this.imageId = ivId;
        this.delView = delView;
    }

    @Override
    protected String doInBackground(String... params){
        String path = Constant.HOST + Constant.Url.IMG_DELETED;
        Map<String, String> map = new HashMap<String, String> ();
        map.put ("imgid", imageId);
        String result = HttpUtil.sendPOSTRequest (path, map);
        return result;
    }

    @Override
    protected void onPostExecute(String result){
        if (result != null) {
            boolean isSuccess = JSONUtils.parseBoolean (result);
            if (isSuccess) {
                glCamara.removeView (delView);// 删除图片
                int count = Camera.max_select_count - 1;
                if (glCamara.getChildCount () == count && addView.getParent () == null) {
                    glCamara.addView (addView);// 添加一个 addView
                }
            }
        }
    }

}
