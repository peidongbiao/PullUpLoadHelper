package com.pei.app;

import android.content.Context;
import android.util.Log;
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

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "Adapter";

    private Context mContext;
    private List<Data> mData;

    public Adapter(Context context) {
        this(context, null);
    }

    public Adapter(Context context, List<Data> data) {
        mContext = context;
        mData = data == null ? new ArrayList<Data>() : data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Data data = mData.get(position);
        viewHolder.mTextView.setText(data.getText());

        Log.d(TAG, "onBindViewHolder: " + position + ", " + data.getText());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void refresh(List<Data> data) {
        this.mData.clear();
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    public void addItems(List<Data> data) {
        int index = mData.size();
        addItems(index, data);
    }

    public void addItems(int index, List<Data> data) {
        mData.addAll(index, data);
        this.notifyItemRangeInserted(index, data.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.text);
        }
    }
}