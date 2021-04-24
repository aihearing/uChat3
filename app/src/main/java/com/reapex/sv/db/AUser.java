package com.reapex.sv.db;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.reapex.sv.R;

@Entity
public class AUser{
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String userPhone;           // 1 same userPhone will be replaced, so could re-register.
    private String userPassword;        // 2 password
    private String userNickName;        // 3 NickName
    private String userAvatarUri;       // 4 uri. user wants to pick up their own picture. cannot use r.mipmpa here.

    private String userWxId;            // 听友号 will be assigned randomly when register.

    private String userGender;             // 可以在用户中心中修改
    private String userRegion;          // 可以在用户中心中修改
    private String userSignature;       // 可以在用户中心中修改

    private int userAvatarResource;       // 2 R.mipmap.default_user_avatar
    private String userType;
    private String userId;
    private String userImPassword;
    private String userHeader;
    private String userEmail;
    private String userIsEmailLinked;

    private String userQqId;
    private String userQqPassword;
    private String userIsQqLinked;

    private String userQrCode;
    private String isFriend;

    private String userWxIdModifyFlag;
    private String userLastestCirclePhotos;

    /**
     * 联系人来源
     */
    private String userContactFrom;

    // 联系人相关
    private String userContactMobiles;
    private String userContactAlias;
    private String userContactDesc;

    /**
     * 联系人权限相关
     */
    private String userContactPrivacy;
    private String userContactHideMyPosts;
    private String userContactHideHisPosts;

    /**
     * 是否星标好友
     */
    private String isStarred;

    /**
     * 是否在黑名单中
     */
    private String isBlocked;

    /**
     * 所有标签(json格式)
     */
    private String userContactTags;
    private String userTags;

    //////////////////////////
    public String getUserNickName() {        return userNickName;    }
    public void setUserNickName(String userNickName) {        this.userNickName = userNickName;    }

    public String getUserPhone() {        return userPhone;    }
    public void setUserPhone(String userPhone) {        this.userPhone = userPhone;    }

    public int getUserAvatarResource() {        return userAvatarResource;    }
    public void setUserAvatarResource(int userAvatarResource){this.userAvatarResource = userAvatarResource;   }

    public String getUserWxId() {        return userWxId;    }
    public void setUserWxId(String userWxId) {        this.userWxId = userWxId;    }

    public String getUserPassword() {        return userPassword;    }
    public void setUserPassword(String userPassword) {        this.userPassword = userPassword;    }

    public String getUserAvatarUri() {        return userAvatarUri;    }
    public void setUserAvatarUri(String userAvatarUri) {        this.userAvatarUri = userAvatarUri;    }

    public String getUserType() {        return userType;    }
    public void setUserType(String userType) {        this.userType = userType;    }
    public String getUserId() {        return userId;    }
    public void   setUserId(String userId) {        this.userId = userId;    }
    public String getUserImPassword() {        return userImPassword;    }
    public void setUserImPassword(String userImPassword) {        this.userImPassword = userImPassword;    }
    public String getUserHeader() {        return userHeader;    }
    public void setUserHeader(String userHeader) {        this.userHeader = userHeader;    }
    public String getUserGender() {        return userGender;    }
    public void setUserGender(String userGender) {        this.userGender = userGender;    }
    public String getUserRegion() {        return userRegion;    }
    public void setUserRegion(String userRegion) {        this.userRegion = userRegion;    }
    public String getUserSignature() {        return userSignature;    }
    public void setUserSignature(String userSignature) {        this.userSignature = userSignature;    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserIsEmailLinked() {
        return userIsEmailLinked;
    }

    public void setUserIsEmailLinked(String userIsEmailLinked) {
        this.userIsEmailLinked = userIsEmailLinked;
    }

    public String getUserQqId() {
        return userQqId;
    }

    public void setUserQqId(String userQqId) {
        this.userQqId = userQqId;
    }

    public String getUserQqPassword() {
        return userQqPassword;
    }

    public void setUserQqPassword(String userQqPassword) {
        this.userQqPassword = userQqPassword;
    }

    public String getUserIsQqLinked() {
        return userIsQqLinked;
    }

    public void setUserIsQqLinked(String userIsQqLinked) {
        this.userIsQqLinked = userIsQqLinked;
    }

    public String getUserQrCode() {
        return userQrCode;
    }

    public void setUserQrCode(String userQrCode) {
        this.userQrCode = userQrCode;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }

    public String getUserWxIdModifyFlag() {
        return userWxIdModifyFlag;
    }

    public void setUserWxIdModifyFlag(String userWxIdModifyFlag) {
        this.userWxIdModifyFlag = userWxIdModifyFlag;
    }

    public String getUserLastestCirclePhotos() {
        return userLastestCirclePhotos;
    }

    public void setUserLastestCirclePhotos(String userLastestCirclePhotos) {
        this.userLastestCirclePhotos = userLastestCirclePhotos;
    }

    public String getUserContactFrom() {
        return userContactFrom;
    }

    public void setUserContactFrom(String userContactFrom) {
        this.userContactFrom = userContactFrom;
    }

    public String getUserContactMobiles() {
        return userContactMobiles;
    }

    public void setUserContactMobiles(String userContactMobiles) {
        this.userContactMobiles = userContactMobiles;
    }

    public String getUserContactAlias() {
        return userContactAlias;
    }

    public void setUserContactAlias(String userContactAlias) {
        this.userContactAlias = userContactAlias;
    }

    public String getUserContactDesc() {
        return userContactDesc;
    }

    public void setUserContactDesc(String userContactDesc) {
        this.userContactDesc = userContactDesc;
    }

    public String getUserContactPrivacy() {
        return userContactPrivacy;
    }

    public void setUserContactPrivacy(String userContactPrivacy) {
        this.userContactPrivacy = userContactPrivacy;
    }

    public String getUserContactHideMyPosts() {
        return userContactHideMyPosts;
    }

    public void setUserContactHideMyPosts(String userContactHideMyPosts) {
        this.userContactHideMyPosts = userContactHideMyPosts;
    }

    public String getUserContactHideHisPosts() {
        return userContactHideHisPosts;
    }

    public void setUserContactHideHisPosts(String userContactHideHisPosts) {
        this.userContactHideHisPosts = userContactHideHisPosts;
    }

    public String getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(String isStarred) {
        this.isStarred = isStarred;
    }

    public String getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(String isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getUserContactTags() {
        return userContactTags;
    }

    public void setUserContactTags(String userContactTags) {
        this.userContactTags = userContactTags;
    }

    public String getUserTags() {
        return userTags;
    }

    public void setUserTags(String userTags) {
        this.userTags = userTags;
    }

    @Ignore
    public AUser() {}

    public AUser(String userPhone, String userNickName, String userPassword, String userAvatarUri, String userWxId) {
        this.userPhone   = userPhone;
        this.userNickName= userNickName;
        this.userPassword= userPassword;
        this.userAvatarUri = userAvatarUri;
        this.userWxId    = userWxId;

        this.userAvatarResource  = R.mipmap.default_user_avatar;
        this.userGender ="男";
        this.userRegion ="中国";
        this.userSignature   ="如果路会通往不知名的地方，你会跟我一起走吗？";
        this.userId      = userPhone;

        this.userImPassword= "userIM Password";
        this.isFriend   ="Y";
        this.isStarred  ="Y";
        this.isBlocked  ="N";
        this.userType   ="REG";
        this.userHeader ="Header";
        this.userQrCode ="QR Code";
        this.userEmail  ="860563368@qq.com";
        this.userIsEmailLinked="Y";
        this.userQqId   ="860563368";
        this.userQqPassword="Y";;
        this.userIsQqLinked="Y";;
        this.userWxIdModifyFlag="Y";;
        this.userLastestCirclePhotos="Y";;
        this.userContactFrom="SV public";;
        this.userContactMobiles="Y";;
        this.userContactAlias="None";;
        this.userContactDesc="contact desc";;
        this.userContactPrivacy="Y";
        this.userContactHideMyPosts="N";
        this.userContactHideHisPosts="N";
        this.userContactTags="Friend";
        this.userTags="Friend";
    }
}