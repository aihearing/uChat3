package com.reapex.sv.asrshort;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cxd.chatview.moudle.ChatView;
import com.reapex.sv.R;
import com.reapex.sv.MySP;
import com.reapex.sv.db.AMessage;
import com.reapex.sv.utils.TimestampUtil;

import java.util.ArrayList;
import java.util.List;

public class MyListViewAdapter extends BaseAdapter {
    private static final int MESSAGE_TYPE_SENT_TEXT = 0;
    private static final int MESSAGE_TYPE_RECV_TEXT = 1;

    private Context mContext;
    private LayoutInflater inflater;
    Boolean isSender;

    List<AMessage> aMessageList = new ArrayList<AMessage>();

    public MyListViewAdapter(Context context, List<AMessage> messageList) {
        mContext = context;
        inflater = LayoutInflater.from(context);
        MySP.getInstance().init(context);
        this.aMessageList = messageList;
    }

    public void setData(List<AMessage> messageList) {
        this.aMessageList = messageList;
    }

    @Override
    public int getCount() {
        return aMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return aMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        // Define a way to determine which layout to use, here it's just evens and odds.
        AMessage aMessage = aMessageList.get(position);
        isSender =  aMessage.getIsSend();
        return isSender ? MESSAGE_TYPE_SENT_TEXT : MESSAGE_TYPE_RECV_TEXT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;   // Count of different layouts (Change according to your requirment)
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final AMessage aMessage = aMessageList.get(position);
        ViewHolder viewHolder;
        isSender =  aMessage.getIsSend();
        if (convertView == null) {
            viewHolder = new ViewHolder();
            int i = 1;
            if (isSender){
                convertView = inflater.inflate(R.layout.a_item_sent_text, null);
            }else{
                convertView = inflater.inflate(R.layout.a_item_received_text, null);
            }
            viewHolder.mTimeStampTv = convertView.findViewById(R.id.tv_timestamp);
            viewHolder.mContentTv = convertView.findViewById(R.id.tv_chat_content);
            viewHolder.mAvatarSdv = convertView.findViewById(R.id.iv_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        handleTextMessage(aMessage, viewHolder, position);
        return convertView;
    }

    class ViewHolder {
        // text
        TextView mTimeStampTv;
        TextView mContentTv;
        ImageView mAvatarSdv;
        ProgressBar mSendingPb;
        ImageView mStatusIv;

        RelativeLayout mMessageRl;
        // image
        ImageView mImageContentSdv;
        // 聊天内容
        ChatView mChatContentCv;

    }


    /**
     * 处理文字消息
     */
    private void handleTextMessage(final AMessage message, ViewHolder viewHolder, final int position) {

        viewHolder.mTimeStampTv.setText(TimestampUtil.getTimePoint(message.getTimestamp()));
        viewHolder.mContentTv.setText(message.getContent());
        viewHolder.mAvatarSdv.setImageResource(message.getMsgAvatarResource());

        if (position > 1) {
            AMessage aLastMessage = aMessageList.get(position - 1);
            if (message.getTimestamp() - aLastMessage.getTimestamp() < 1 * 60 * 1000) {
                viewHolder.mTimeStampTv.setVisibility(View.GONE);
            }
        }
    }
}
