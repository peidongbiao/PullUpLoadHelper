package com.pei.pulluploadhelper;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**可添加header和footer的装饰器
 * Created by pei on 2017/2/6 0006.
 */

public class HeaderFooterRecyclerAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;

    private SparseArray<View> mHeaderViews = new SparseArray<>();
    private SparseArray<View> mFootViews = new SparseArray<>();
    private RecyclerView.Adapter mTargetAdapter;

    public HeaderFooterRecyclerAdapterWrapper(RecyclerView.Adapter targetAdapter) {
        if(targetAdapter == null){
            throw new NullPointerException("target adapter is null");
        }
        mTargetAdapter = targetAdapter;

        targetAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                HeaderFooterRecyclerAdapterWrapper.this.notifyDataSetChanged();
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                HeaderFooterRecyclerAdapterWrapper.this.notifyItemRangeChanged(getHeaderCount() + positionStart, itemCount);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                HeaderFooterRecyclerAdapterWrapper.this.notifyItemRangeChanged(getHeaderCount() + positionStart, itemCount, payload);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                HeaderFooterRecyclerAdapterWrapper.this.notifyItemRangeInserted(getHeaderCount() + positionStart, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                HeaderFooterRecyclerAdapterWrapper.this.notifyItemRangeRemoved(getHeaderCount() + positionStart, itemCount);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                HeaderFooterRecyclerAdapterWrapper.this.notifyItemMoved(getHeaderCount() + fromPosition, getHeaderCount() + toPosition);
            }
        });
    }

    @Override
    public final int getItemViewType(int position) {
        if (isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        } else if (isFooterView(position)) {
            return mFootViews.keyAt(position - getHeaderCount() - getTargetItemCount());
        }
        return mTargetAdapter.getItemViewType(position - getHeaderCount());
    }


    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null) {
            return new HeaderFooterViewHolder(mHeaderViews.get(viewType));
        } else if (mFootViews.get(viewType) != null) {
            return new HeaderFooterViewHolder(mFootViews.get(viewType));
        }
        return mTargetAdapter.onCreateViewHolder(parent,viewType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderView(position) || isFooterView(position)) {
            return;
        }
        mTargetAdapter.onBindViewHolder(holder,position - getHeaderCount());
    }

    private boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterView(int position) {
        return position >= getHeaderCount() + getTargetItemCount();
    }

    public void addHeaderView(View view) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view);
        this.notifyDataSetChanged();
    }

    public void removeHeaderView(View view){
        int index = mHeaderViews.indexOfValue(view);
        mHeaderViews.removeAt(index);
        this.notifyDataSetChanged();
    }

    public void removeHeaderView(int position){
        mHeaderViews.removeAt(position);
        this.notifyDataSetChanged();
    }

    public void addFootView(View view) {
        mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view);
        this.notifyDataSetChanged();
    }

    public void removeFooterView(View view){
        int index = mFootViews.indexOfValue(view);
        mFootViews.removeAt(index);
        this.notifyDataSetChanged();
    }

    public View getHeaderView(int position){
        return mHeaderViews.get(position);
    }

    public View getFooterView(int position){
        return mFootViews.get(position);
    }

    public boolean conatinsHeaderView(View view){
        for (int i = 0; i < mHeaderViews.size(); i++) {
            View header = mHeaderViews.get(i);
            if(header.equals(view)){
                return true;
            }
        }
        return false;
    }

    public boolean containsFooterView(View view){
        for (int i = 0; i < mFootViews.size(); i++) {
            View footer = mFootViews.get(i);
            if(footer.equals(view)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getTargetItemCount();
    }

    private int getTargetItemCount() {
        return mTargetAdapter.getItemCount();
    }

    public int getHeaderCount() {
        return mHeaderViews.size();
    }

    public int getFooterCount() {
        return mFootViews.size();
    }

    /**
     * 处理GridLayoutManager
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int itemType = getItemViewType(position);
                    if (mHeaderViews.get(itemType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFootViews.get(itemType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
        }
    }

    /**
     * 处理StaggeredGridLayoutManager
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    private static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

        public HeaderFooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}