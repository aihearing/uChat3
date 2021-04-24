package com.reapex.sv.asrlong;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.reapex.sv.R;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder0> {

    private final List<String> mData;
    private final LayoutInflater mInflater;

    //1. 接下data，把view指向ChatVIP
    MyRecyclerViewAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);      //用chatVIP的view
        this.mData = data;                                  //用chatVIP的data
    }

    //2. 在ChatVIP中画item view，把item view放入view holder //inflate the raw layout using XML when necessary
    @Override
    public int getItemViewType(int position) {
        int viewType = 1; //Default is 1
        if (position == 0) viewType = 0; //if zero, it will be a header view
        return viewType;
    }

    @Override
    public ViewHolder0 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType==0) {
            view = mInflater.inflate(R.layout.a_item_received_text, parent, false);
        }else{
            view = mInflater.inflate(R.layout.a_item_sent_text, parent, false);
        }
        return new ViewHolder0(view);
    }

    //3. binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder0 holder, int position) {
        String animal = mData.get(position);
        holder.myTextView.setText(animal);
    }

    //4. stores and recycles views as they are scrolled off screen
    public class ViewHolder0 extends RecyclerView.ViewHolder{
        TextView myTextView;
        ViewHolder0(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tv_chat_content);
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

}