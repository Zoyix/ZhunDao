package com.zhaohe.zhundao.constant;

import android.util.SparseBooleanArray;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/11/30 10:21
 */
public class Constant {
    public static String APP_ID = "wxfe2a9da163481ba9";
    public static String HOST = "http://open.zhundao.net"; // 主线路
    public static String HOST_1 = "http://open.zhundao.com.cn"; // 备用

    public static String HOST_MOBLIE = "http://m.zhundao.net";
    public static String HOST_MSG = "http://sms.zhundao.com.cn";

    //    public static String       ACCESSKEY;
//    //是否登录
//    public static boolean       ISLOGIN=false;
    public static String HEADURL = new String();

    public static String AppKey = "QWEDCXZAS";

    public static final int REQUEST_CODE_PERMISSION = 10086;
    public static SparseBooleanArray booleanArray_msg;//提交授权身份证信息

    /**
     * 图片上传的 fileName
     */
    public static final String IMAGEFILE = "imageFile";

    public static final class Url {
        //        登录获取Accesskey
        public static final String Login = "/api/PerBase/GetAccessKey";
        //        登录获取多点签到Accesskey
        public static final String LoginMulti = "/api/PerBase/VerifyCheckInAdmin";
        //        获取微信登录权限
        public static final String GetAccessToken = "/oauth/appcallback";
        //发送微信信息给服务器
        public static final String SentWxUserInf = "/oauth/CreateUserFromAndroid";

        //        获取活动列表
        public static final String GetActivityList = "/api/PerActivity/PostActivityList";
        //        更新或者添加活动
        public static final String UpdateOrAddActivity = "/api/PerActivity/UpdateOrAddActivity";
        //        获取验证码
        public static final String GetCode = "/api/PerBase/SendICode";
        //        获用户信息
        public static final String GetUserInf = "/api/PerBase/GetUserInfo";
        //                 绑定手机
        public static final String BondPhone = "/api/PerBase/BindPhone";
        //        获取报名人员列表
        public static final String PostActivityListed = "/api/PerActivity/PostActivityListed";
        //        获取签到列表
        public static final String GetSignList = "/api/CheckIn/PostCheckIn";
        //获取签到人员列表
        public static final String PostCheckInList = "/api/CheckIn/PostCheckInList";
        //        扫码
        public static final String AddCheckInListByQrcode = "/api/CheckIn/AddCheckInListByQrcode";
        //更新签到列表
        public static final String UpdateCheckIn = "/api/CheckIn/UpdateCheckIn";
        //        上传签到状态
        public static final String BatchCheckIn = "/api/CheckIn/BatchCheckIn";
        //        修改用户密码
        public static final String UpdatePassWord = "/api/PerBase/UpdatePassWord";
        public static final String PostActivityOptions = "/api/PerActivity/PostActivityOptions";//获取选项
        public static final String UpdateOrAddOption = "/api/PerActivity/UpdateOrAddOption";//修改增加选项
        public static final String GetMybeaconList = "/api/Game/GetMybeaconList";//获取beacon列表
        public static final String UpdateBeacon = "/api/Game/UpdateBeacon";//更新Beacon
        public static final String DeteleActivity = "/api/PerActivity/DeteleActivity";//删除活动
        //        终止活动
        public static final String UnDueActivity = "/api/PerActivity/UnDueActivity";

        //邀请函
        public static final String InvitationUrl = "/api/GetInvitation";
//        管理员手机代签

        public static final String AddCheckInListByPhone = "/api/CheckIn/AddCheckInListByPhone";
        //分享活动
        public static final String ShareUrl = "m.zhundao.net/event/";
        //        绑定摇一摇设备
        public static final String AddBeacon = "/api/Game/UpdateBeacon";
        //        添加签到
        public static final String AddCheckIn = "/api/CheckIn/AddCheckIn";
        //        修改签到
        public static final String UpdateCheckInTypeName = "/api/CheckIn/UpdateCheckInTypeName";
        public static final String DeleteCheckIn = "/api/CheckIn/DeleteCheckIn";
        public static final String PostContact = "/api/Contact/PostContact";//获取全部联系人
        public static final String PostContactGroup = "/api/Contact/PostContactGroup";//获取全部群组
        public static final String UpdateOrAddContact = "/api/Contact/UpdateOrAddContact";//添加或修改联系人
        public static final String UpdateOrAddContactGroup = "/api/Contact/UpdateOrAddContactGroup";//添加或修改分组
        public static final String DeleteContactGroup = "/api/Contact/DeleteContactGroup";//删除分组
        public static final String UploadFile = "/OAuth/UploadFile";//上传图片
        public static final String DeleteContact = "/api/Contact/DeleteContact";//删除联系人
        public static final String SendActivityListByEmail = "/api/PerActivity/SendActivityListByEmail";//发送报名名单到分组
        public static final String SendCheckInListByEmail = "/api/CheckIn/SendCheckInListByEmail";//发送签到名单到分组

        public static final String UpdateActivityList = "/api/PerActivity/UpdateActivityList";//修改报名人员信息
        public static final String DeleteActivityList = "/api/PerActivity/DeleteActivityList";//删除报名人员
        public static final String AddActivityList = "/api/PerActivity/AddActivityList";//添加报名人员
        public static final String GetNoticeList = "/api/ZDInfo/GetNoticeList";//准到通知
        public static final String PayOffLine = "/api/PerActivity/PayOffLine";//修改报名为线下支付

        public static final String VerifyPhone = "/api/PerBase/VerifyPhone";//验证手机号码是否已经被注册
        public static final String SendVcode = "/api/PerBase/SendVcode";//发送手机注册验证码
        public static final String VerifyPhoneAndCode = "/api/PerBase/VerifyPhoneAndCode";//验证手机号码和验证码是否匹配正确
        public static final String RegisterByPhone = "/api/PerBase/RegisterByPhone";//手机注册账号
        public static final String UpdatePassWordByPhone = "/api/PerBase/UpdatePassWordByPhone";//修改密码
        public static final String UpdateUserInfo = "/api/PerBase/UpdateUserInfo";//修改用户信息
        public static final String PstAuthentication = "/api/PerBase/PstAuthentication";//提交授权身份证信息
        public static final String Installapp = "/api/App/InstallMessageApp";//开通应用（短信）
        public static final String adminInfo = "/api/sms/adminInfo";//获取短信信息（短信）
        public static final String BatchSendSms = "/api/Core/BatchSendSms";//发送短信（短信）
        public static final String TopUpSMS = "/api/PerBase/TopUpSMS";//开通应用（短信）




        public static final String Device = "Android";


        public static final String IMG_DOWNLOAD = "";
        public static final String IMG_DELETED = "";


    }

    public static final class FileDir {

        /**
         * 媒体路径
         */
        public static final String MEDIA = "/topinfo/media/";
        /**
         * 图片路径
         */
        public static final String IMG = "/topinfo/img/";
        /**
         * 文件路径
         */
        public static final String FILE = "/topinfo/file/";
    }

}
