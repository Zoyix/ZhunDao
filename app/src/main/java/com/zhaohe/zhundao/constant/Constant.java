package com.zhaohe.zhundao.constant;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2016/11/30 10:21
 */
public class Constant {
    public static String APP_ID = "wxfe2a9da163481ba9";
    public static String HOST = "http://open.zhundao.net"; // 测试
    public static String HOST_MOBLIE = "http://m.zhundao.net";
    //    public static String       ACCESSKEY;
//    //是否登录
//    public static boolean       ISLOGIN=false;
    public static String HEADURL = new String();

    public static String AppKey = "QWEDCXZAS";
    /**
     * 图片上传的 fileName
     */
    public static final String IMAGEFILE = "imageFile";

    public static final class Url {
        //        登录获取Accesskey
        public static final String Login = "/api/PerBase/GetAccessKey";
        //        获取微信登录权限
        public static final String GetAccessToken = "/oauth/appcallback";
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
        //xml存放位置
        public static final String VERSION = "";
        public static final String PostActivityOptions = "/api/PerActivity/PostActivityOptions";
        public static final String UpdateOrAddOption = "/api/PerActivity/UpdateOrAddOption";
        public static final String GetMybeaconList = "/api/Game/GetMybeaconList";
        public static final String UpdateBeacon = "/api/Game/UpdateBeacon";
        public static final String DeteleActivity="/api/PerActivity/DeteleActivity";
//        终止活动
        public static final String UnDueActivity="/api/PerActivity/UnDueActivity";

//邀请函
        public static final String InvitationUrl = "/api/GetInvitation";
//        管理员手机代签

        public static final String AddCheckInListByPhone= "/api/CheckIn/AddCheckInListByPhone";

        public static final String ShareUrl = "m.zhundao.net/event/";
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
