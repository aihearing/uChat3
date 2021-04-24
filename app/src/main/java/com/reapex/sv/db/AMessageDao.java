package com.reapex.sv.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AMessageDao {
    @Insert
    void insert(AMessage aMessage);

    @Query("SELECT * FROM AMessage WHERE senderId =:fromUserId")
    List<AMessage> getMessageListByUserId(String fromUserId);

    @Query("SELECT * FROM AMessage")
    List<AMessage> getAllMessage();

    @Query("SELECT * FROM AMessage WHERE MessageId =:messageId")
    List<AMessage> getMessageListByMessageId(String messageId);

    /*
    public void saveMessageByImMessage(cn.jpush.im.android.api.model.Message imMessage, String userId) {
        UserInfo fromUserInfo = imMessage.getFromUser();
        if (fromUserInfo.getUserName().equals(userId)) {
            return;
        }
        Message message = new Message();
        message.setCreateTime(TimeUtil.getTimeStringAutoShort2(new Date().getTime(), true));

        // 消息发送者信息
        message.setFromUserId(fromUserInfo.getUserName());
        message.setFromUserName(fromUserInfo.getNickname());
        message.setFromUserAvatar(fromUserInfo.getAvatar());

        // 群发 or 单发
        if (imMessage.getTargetType().equals(ConversationType.single)) {
            message.setTargetType(Constant.TARGET_TYPE_SINGLE);

        } else {
            message.setTargetType(Constant.TARGET_TYPE_GROUP);
            GroupInfo groupInfo = (GroupInfo) imMessage.getTargetInfo();
            message.setGroupId(String.valueOf(groupInfo.getGroupID()));
        }

        // 消息接收者信息
        message.setToUserId(userId);

        // 消息类型
        message.setMessageType(JimUtil.getMessageType(imMessage));
        message.setTimestamp(new Date().getTime());

        if (Constant.MSG_TYPE_TEXT.equals(message.getMessageType())) {
            // 文字
            TextContent messageContent = (TextContent) imMessage.getContent();
            message.setContent(messageContent.getText());
        } else if (Constant.MSG_TYPE_IMAGE.equals(message.getMessageType())) {
            // 图片
            ImageContent imageContent = ((ImageContent) imMessage.getContent());
            String imageUrl = imageContent.getLocalThumbnailPath();
            message.setImageUrl(imageUrl);
        } else if (Constant.MSG_TYPE_LOCATION.equals(message.getMessageType())) {
            // 位置
            Map<String, String> messageMap = JSON.parseObject(imMessage.getContent().toJson(), Map.class);
            Map<String, Object> messageBodyMap = JSON.parseObject(messageMap.get("text"), Map.class);
            message.setMessageBody(JSON.toJSONString(messageBodyMap));
        }
        Message.save(message);
    }*/
}
