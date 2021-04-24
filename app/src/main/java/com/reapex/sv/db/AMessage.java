package com.reapex.sv.db;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.reapex.sv.utils.TimeUtil;

import java.util.Date;

// message 固定语境，固定头像，可以用resource。
@Entity
public class AMessage {
    @PrimaryKey(autoGenerate = true)
    private int id;                 // 0 排序甚好

    private int msgAvatarResource;  // 1 四个语境四个icon
    private boolean isSend;         // 2 true for sending
    private String content;         // 3 content
    private String senderId;        // 4 601 602 603 604 for 语境. 800 for 系统提示
    private String senderName;      // 5 家里，外面，嘈杂，一般

    private long timestamp;
    private String messageId;       // 系统提示:800系列
    private int toUserAvatarResource;
    private int status;
    private String messageType;
    private String createTime;
    private String fromUserAvatar;
    private String toUserId;
    private String toUserName;
    private String toUserAvatar;
    private String messageBody;
    private String imageUrl;
    private String targetType;
    private String groupId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getIsSend() {
        return isSend;
    }

    public void setIsSend(Boolean isSend) {
        this.isSend = isSend;
    }

    public int getMsgAvatarResource() {
        return msgAvatarResource;
    }

    public void setMsgAvatarResource(int msgAvatarResource) {
        this.msgAvatarResource = msgAvatarResource;
    }

    public int getToUserAvatarResource() {
        return toUserAvatarResource;
    }

    public void setToUserAvatarResource(int toUserAvatarResource) {
        this.toUserAvatarResource = toUserAvatarResource;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getFromUserAvatar() {
        return fromUserAvatar;
    }

    public void setFromUserAvatar(String fromUserAvatar) {
        this.fromUserAvatar = fromUserAvatar;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getToUserAvatar() {
        return toUserAvatar;
    }

    public void setToUserAvatar(String toUserAvatar) {
        this.toUserAvatar = toUserAvatar;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public AMessage() {
    }

    @Ignore
    public AMessage(String content, String senderId, String senderName, int msgAvatarResource, boolean isSend) {
        this.content = content;
        this.senderId = senderId;
        this.senderName = senderName;
        this.isSend = isSend;
        this.msgAvatarResource = msgAvatarResource;

        this.fromUserAvatar = "content://media/external/images/media/2122745";
        this.createTime = TimeUtil.getTimeStringAutoShort2(new Date().getTime(), true);
        this.timestamp = new Date().getTime();

        this.messageId = "messageId";
        this.messageType = "TEXT";
        this.toUserId = "888";
        this.toUserName = "你说";
        this.toUserAvatar = "toUserAvatar";
        this.toUserAvatarResource = 1;
        this.status = 1;
        this.messageBody = "messageBody";
        this.imageUrl = "imageUrl";
        this.targetType = "Target Type";
        this.groupId = "Group ID";
    }
}