package com.jaysen.wallstreet;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.jaysen.wallstreet.util.Message;

import java.util.ArrayList;

/**
 * Project:   WallStreet
 * className: StockRecyclerAdapter
 * Created on 2016/7/13
 *
 * @author jaysen.lin@foxmail.com
 */
public class StockRecyclerAdapter extends RecyclerView.Adapter<StockVH> {
    private ArrayList<Message> mDatasets;

    @Override
    public StockVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.list_item_layout, null);
        return new StockVH(itemView);
    }

    @Override
    public void onBindViewHolder(StockVH holder, int position) {
        //bind data
        holder.bindItem(mDatasets.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatasets == null ? 0 : mDatasets.size();
    }

    public void setmDatasets(ArrayList<Message> datasets) {
        mDatasets = datasets;
        notifyDataSetChanged();
    }
}
