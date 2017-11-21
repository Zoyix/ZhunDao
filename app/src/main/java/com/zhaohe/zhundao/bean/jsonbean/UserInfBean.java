package com.zhaohe.zhundao.bean.jsonbean;

/**
 * @Description:
 * @Author:邹苏隆
 * @Since:2017/11/15 11:00
 */
public class UserInfBean {

    /**
     * Res : 0
     * Msg : success
     * Data : {"TrueName":"邹苏隆","NickName":"邹苏隆","HeadImgurl":"https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJXo3VsMzMCbYvgMSRNhFrzLuMZIcXUZT89zIFX3oMg76Roxmp6NLianTHnrsLsCD2M9iaKmlFic3tKQ/0","Sex":1,"Country":"中国","Province":"浙江","City":"丽水","Address":null,"GradeId":10,"Company":"准到1","Industry":"计算机3","Duty":"安卓开发3","Mobile":"18967092210","IDcard":null,"Email":"545800663@qq.com1","PassWord":"2692D43D91BDD1EB45441708D6315DC1","Openid":"o-_vcsidcWyzyZowujNwery-w6Lk","QQid":null,"Alipayid":null,"InviteID":0,"LastTime":"2016-11-15 14:48:35","Groupid":117,"Unionid":"oX9XjjiR5v0zI4o-cG6N1ZaRqPns","Remark":null,"Subscribe":0,"VipTime":null,"Balance":0,"FactorageRate":0,"PayPassWord":null,"AuthenticationId":2503,"Authentication":{"Name":"邹苏隆","IDCard":"332522199411180355","Mobile":"18967092210","IdCardFront":"https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105214313|","IdCardBack":"https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105216970|","Status":0,"UserId":44948,"ID":2503,"AddTime":"2017-11-15 10:52:20","IsDeleted":false},"Config":null,"ID":44948,"AddTime":"2016-11-15 14:48:35","IsDeleted":false}
     * Url :
     */

    private int Res;
    private String Msg;
    private DataBean Data;
    private String Url;

    public int getRes() {
        return Res;
    }

    public void setRes(int Res) {
        this.Res = Res;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String Url) {
        this.Url = Url;
    }

    public static class DataBean {
        /**
         * TrueName : 邹苏隆
         * NickName : 邹苏隆
         * HeadImgurl : https://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTJXo3VsMzMCbYvgMSRNhFrzLuMZIcXUZT89zIFX3oMg76Roxmp6NLianTHnrsLsCD2M9iaKmlFic3tKQ/0
         * Sex : 1
         * Country : 中国
         * Province : 浙江
         * City : 丽水
         * Address : null
         * GradeId : 10
         * Company : 准到1
         * Industry : 计算机3
         * Duty : 安卓开发3
         * Mobile : 18967092210
         * IDcard : null
         * Email : 545800663@qq.com1
         * PassWord : 2692D43D91BDD1EB45441708D6315DC1
         * Openid : o-_vcsidcWyzyZowujNwery-w6Lk
         * QQid : null
         * Alipayid : null
         * InviteID : 0
         * LastTime : 2016-11-15 14:48:35
         * Groupid : 117
         * Unionid : oX9XjjiR5v0zI4o-cG6N1ZaRqPns
         * Remark : null
         * Subscribe : 0
         * VipTime : null
         * Balance : 0.0
         * FactorageRate : 0.0
         * PayPassWord : null
         * AuthenticationId : 2503
         * Authentication : {"Name":"邹苏隆","IDCard":"332522199411180355","Mobile":"18967092210","IdCardFront":"https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105214313|","IdCardBack":"https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105216970|","Status":0,"UserId":44948,"ID":2503,"AddTime":"2017-11-15 10:52:20","IsDeleted":false}
         * Config : null
         * ID : 44948
         * AddTime : 2016-11-15 14:48:35
         * IsDeleted : false
         */

        private String TrueName;
        private String NickName;
        private String HeadImgurl;
        private int Sex;
        private String Country;
        private String Province;
        private String City;
        private Object Address;
        private int GradeId;
        private String Company;
        private String Industry;
        private String Duty;
        private String Mobile;
        private Object IDcard;
        private String Email;
        private String PassWord;
        private String Openid;
        private Object QQid;
        private Object Alipayid;
        private int InviteID;
        private String LastTime;
        private int Groupid;
        private String Unionid;
        private Object Remark;
        private int Subscribe;
        private Object VipTime;
        private double Balance;
        private double FactorageRate;
        private Object PayPassWord;
        private int AuthenticationId;
        private AuthenticationBean Authentication;
        private Object Config;
        private int ID;
        private String AddTime;
        private boolean IsDeleted;

        public String getTrueName() {
            return TrueName;
        }

        public void setTrueName(String TrueName) {
            this.TrueName = TrueName;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getHeadImgurl() {
            return HeadImgurl;
        }

        public void setHeadImgurl(String HeadImgurl) {
            this.HeadImgurl = HeadImgurl;
        }

        public int getSex() {
            return Sex;
        }

        public void setSex(int Sex) {
            this.Sex = Sex;
        }

        public String getCountry() {
            return Country;
        }

        public void setCountry(String Country) {
            this.Country = Country;
        }

        public String getProvince() {
            return Province;
        }

        public void setProvince(String Province) {
            this.Province = Province;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String City) {
            this.City = City;
        }

        public Object getAddress() {
            return Address;
        }

        public void setAddress(Object Address) {
            this.Address = Address;
        }

        public int getGradeId() {
            return GradeId;
        }

        public void setGradeId(int GradeId) {
            this.GradeId = GradeId;
        }

        public String getCompany() {
            return Company;
        }

        public void setCompany(String Company) {
            this.Company = Company;
        }

        public String getIndustry() {
            return Industry;
        }

        public void setIndustry(String Industry) {
            this.Industry = Industry;
        }

        public String getDuty() {
            return Duty;
        }

        public void setDuty(String Duty) {
            this.Duty = Duty;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public Object getIDcard() {
            return IDcard;
        }

        public void setIDcard(Object IDcard) {
            this.IDcard = IDcard;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getPassWord() {
            return PassWord;
        }

        public void setPassWord(String PassWord) {
            this.PassWord = PassWord;
        }

        public String getOpenid() {
            return Openid;
        }

        public void setOpenid(String Openid) {
            this.Openid = Openid;
        }

        public Object getQQid() {
            return QQid;
        }

        public void setQQid(Object QQid) {
            this.QQid = QQid;
        }

        public Object getAlipayid() {
            return Alipayid;
        }

        public void setAlipayid(Object Alipayid) {
            this.Alipayid = Alipayid;
        }

        public int getInviteID() {
            return InviteID;
        }

        public void setInviteID(int InviteID) {
            this.InviteID = InviteID;
        }

        public String getLastTime() {
            return LastTime;
        }

        public void setLastTime(String LastTime) {
            this.LastTime = LastTime;
        }

        public int getGroupid() {
            return Groupid;
        }

        public void setGroupid(int Groupid) {
            this.Groupid = Groupid;
        }

        public String getUnionid() {
            return Unionid;
        }

        public void setUnionid(String Unionid) {
            this.Unionid = Unionid;
        }

        public Object getRemark() {
            return Remark;
        }

        public void setRemark(Object Remark) {
            this.Remark = Remark;
        }

        public int getSubscribe() {
            return Subscribe;
        }

        public void setSubscribe(int Subscribe) {
            this.Subscribe = Subscribe;
        }

        public Object getVipTime() {
            return VipTime;
        }

        public void setVipTime(Object VipTime) {
            this.VipTime = VipTime;
        }

        public double getBalance() {
            return Balance;
        }

        public void setBalance(double Balance) {
            this.Balance = Balance;
        }

        public double getFactorageRate() {
            return FactorageRate;
        }

        public void setFactorageRate(double FactorageRate) {
            this.FactorageRate = FactorageRate;
        }

        public Object getPayPassWord() {
            return PayPassWord;
        }

        public void setPayPassWord(Object PayPassWord) {
            this.PayPassWord = PayPassWord;
        }

        public int getAuthenticationId() {
            return AuthenticationId;
        }

        public void setAuthenticationId(int AuthenticationId) {
            this.AuthenticationId = AuthenticationId;
        }

        public AuthenticationBean getAuthentication() {
            return Authentication;
        }

        public void setAuthentication(AuthenticationBean Authentication) {
            this.Authentication = Authentication;
        }

        public Object getConfig() {
            return Config;
        }

        public void setConfig(Object Config) {
            this.Config = Config;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }

        public boolean isIsDeleted() {
            return IsDeleted;
        }

        public void setIsDeleted(boolean IsDeleted) {
            this.IsDeleted = IsDeleted;
        }

        public static class AuthenticationBean {
            /**
             * Name : 邹苏隆
             * IDCard : 332522199411180355
             * Mobile : 18967092210
             * IdCardFront : https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105214313|
             * IdCardBack : https://joinheadoss.oss-cn-hangzhou.aliyuncs.com/zhundao/20171115105216970|
             * Status : 0
             * UserId : 44948
             * ID : 2503
             * AddTime : 2017-11-15 10:52:20
             * IsDeleted : false
             */

            private String Name;
            private String IDCard;
            private String Mobile;
            private String IdCardFront;
            private String IdCardBack;
            private int Status;
            private int UserId;
            private int ID;
            private String AddTime;
            private boolean IsDeleted;

            public String getName() {
                return Name;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public String getIDCard() {
                return IDCard;
            }

            public void setIDCard(String IDCard) {
                this.IDCard = IDCard;
            }

            public String getMobile() {
                return Mobile;
            }

            public void setMobile(String Mobile) {
                this.Mobile = Mobile;
            }

            public String getIdCardFront() {
                return IdCardFront;
            }

            public void setIdCardFront(String IdCardFront) {
                this.IdCardFront = IdCardFront;
            }

            public String getIdCardBack() {
                return IdCardBack;
            }

            public void setIdCardBack(String IdCardBack) {
                this.IdCardBack = IdCardBack;
            }

            public int getStatus() {
                return Status;
            }

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public int getID() {
                return ID;
            }

            public void setID(int ID) {
                this.ID = ID;
            }

            public String getAddTime() {
                return AddTime;
            }

            public void setAddTime(String AddTime) {
                this.AddTime = AddTime;
            }

            public boolean isIsDeleted() {
                return IsDeleted;
            }

            public void setIsDeleted(boolean IsDeleted) {
                this.IsDeleted = IsDeleted;
            }
        }
    }
}
