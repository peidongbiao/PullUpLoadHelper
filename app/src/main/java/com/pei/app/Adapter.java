package com.pei.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peidongbiao on 2018/2/9.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<String> mData;

    public Adapter(Context context) {
        this(context,null);
    }

    public Adapter(Context context, List<String> data) {
        mContext = context;
        mData = data == null ? new ArrayList<String>() : data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        String str = mData.get(position);
        viewHolder.mTextView.setText(str);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void refresh(List<String> data){
        this.mData.clear();
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addItems(List<String> data){
        int start = mData.size();
        mData.addAll(data);
        this.notifyItemRangeInserted(start,data.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);
        }
    }
}