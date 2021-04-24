package com.reapex.sv;

/**
 * 常量类
 */
public class Constant {
    public static String PICTURE_DIR = "sdcard/wechat_/pictures/";
    public static final String BASE_URL = "/";

//    public static final String URL = "https://seeingvoice.com";      //中文
    public static final String URL = "https://seeingvoice.gitee.io";      //中文
    public static final String URL_UPGRADE         = URL+"/youchat/";        //关于我们
    public static final String URL_COMPLAIN         = URL+"/contact/";        //关于我们
    public static final String URL_TUTORIAL         = URL+"/youchat/";        //关于我们
    public static final String URL_PRIVACY          = URL+"/privacy/";      //隐私政策
    public static final String URL_USER_AGREEMENT   = URL+"/terms/";        //用户协议

    public static final int NUM_OF_6_SECONDS = 10;

    public static final String IS_NOT_FRIEND = "0";
    public static final String IS_FRIEND = "1";
    public static final String USER_SEX_MALE = "男";
    public static final String USER_SEX_FEMALE = "女";
    public static final String USER_SEX_NOSAY = "保密";

    public static final String MSG_TYPE_TEXT = "text";
    public static final String MSG_TYPE_IMAGE = "image";
    public static final String MSG_TYPE_LOCATION = "location";
    public static final String MSG_TYPE_VOICE = "voice";
    public static final String MSG_TYPE_CUSTOM = "custom";
    public static final String MSG_TYPE_SYSTEM = "eventNotification";

    public static final String TARGET_TYPE_SINGLE = "single";
    public static final String TARGET_TYPE_GROUP = "group";
    public static final String TARGET_TYPE_CHATROOM = "chatroom";

    public static final int DEFAULT_PAGE_SIZE = 10;
    // 好友来源
    public static final String FRIENDS_SOURCE_BY_PHONE = "1";
    public static final String FRIENDS_SOURCE_BY_WX_ID = "2";
    public static final String FRIENDS_SOURCE_BY_PEOPLE_NEARBY = "3";
    public static final String FRIENDS_SOURCE_BY_CONTACT = "4";
    public static final String CONTACTS_FROM_PHONE = "1";
    public static final String CONTACTS_FROM_WX_ID = "2";
    public static final String CONTACTS_FROM_PEOPLE_NEARBY = "3";
    public static final String CONTACTS_FROM_CONTACT = "4";
    public static final String PRIVACY_CHATS_MOMENTS_WERUN_ETC = "0";
    public static final String PRIVACY_CHATS_ONLY = "1";
    public static final String SHOW_MY_POSTS = "0";
    public static final String HIDE_MY_POSTS = "1";
    public static final String SHOW_HIS_POSTS = "0";
    public static final String HIDE_HIS_POSTS = "1";
    public static final String CONTACT_IS_NOT_STARRED = "0";
    public static final String CONTACT_IS_STARRED = "1";
    public static final String CONTACT_IS_NOT_BLOCKED = "0";
    public static final String CONTACT_IS_BLOCKED = "1";
    public static final String STAR_FRIEND = "星标朋友";
    public static final String USER_WX_ID_MODIFY_FLAG_TRUE = "1";

    public static final String AREA_TYPE_PROVINCE = "1";
    public static final String AREA_TYPE_CITY = "2";
    public static final String AREA_TYPE_DISTRICT = "3";
    public static final String LOCATION_TYPE_AREA = "0";
    public static final String LOCATION_TYPE_MSG = "1";
    public static final String DEFAULT_POST_CODE = "000000";

    // 登录方式
    public static final String LOGIN_TYPE_PHONE_AND_PASSWORD = "0";
    public static final String LOGIN_TYPE_PHONE_AND_VERIFICATION_CODE = "1";
    public static final String LOGIN_TYPE_OTHER_ACCOUNTS_AND_PASSWORD = "2";
    public static final String VERIFICATION_CODE_SERVICE_TYPE_LOGIN = "0";
    public static final String QQ_ID_NOT_LINK = "0";
    public static final String QQ_ID_LINKED = "1";
    public static final String EMAIL_NOT_LINK = "0";
    public static final String EMAIL_NOT_VERIFIED = "1";
    public static final String EMAIL_VERIFIED = "2";

    /**
     * 热词阈值
     */
    public static final Integer HOT_SEARCH_THRESHOLD = 8;

    /**
     * 普通注册用户
     */
    public static final String USER_TYPE_REG = "REG";

    /**
     * 微信团队
     */
    public static final String USER_TYPE_WEIXIN = "WEIXIN";

    /**
     * 文件传输助手
     */
    public static final String USER_TYPE_FILEHELPER = "FILEHELPER";

    // SharedPreferences key
    /**
     * 已选标签
     */
    public static final String SP_KEY_TAG_SELECTED = "tag_selected";

    public static String getWxId() {
        int i = (int) (Math.random() * 114);
        String[] wxId={"896336",                "896333",                "893999",                "893998",                "893996",                "893993",
                "893989",                "893988",                "893986",                "893983",                "893969",                "893968",
                "893966",                "893963",                "893939",                "893938",                "893936",                "893933",
                "893899",                "893898",                "893896",                "893893",                "893889",                "893888",
                "893886",                "893883",                "893869",                "893868",                "893866",                "893863",
                "893839",                "893838",                "893836",                "893833",                "893699",                "893698",                "893696",
                "893693",                "689888",                "689886",                "689883",                "689869",                "689868",                "689866",                "689863",
                "689839",                "689838",                "689836",                "689833",                "689699",                "689698",                "689696",                "689693",
                "689689",                "689688",                "689686",                "689683",                "689669",                "689668",                "689666",                "689663",
                "689639",                "689638",                "689636",                "689633",                "689399",                "689398",                "689396",                "689393",
                "689389",                "689388",                "689386",                "689383",                "689369",                "689368",                "689366",
                "339996",                "339993",                "339989",                "339988",               "339986",                "339983",                "339969",
                "339968",                "339966",                "339963",                "339939",                "339938",                "339936",                "339933",                "339899",
                "339898",                "339896",                "339893",                "339889",                "339888",                "339886",                "339883",                "339869",
                "339868",                "339866",                "339863",                "339839",                "339838",                "339836",                "339833",
                "339699",                "339698",                "339696",                "339693",                "339689",                "339688",                "339686",                "339683"};
        return wxId[i];
    }
}
