package com.chencha.citysselectordemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chencha.citysselectordemo.MainActivity;
import com.chencha.citysselectordemo.R;
import com.chencha.citysselectordemo.bean.SortModel;
import com.chencha.citysselectordemo.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:    地址搜索
 * Author: chencha
 * Date: 17/11/15
 */

public class AddressSearchAdapter extends RecyclerView.Adapter<AddressSearchAdapter.ViewHolder> {
    protected Context mContext;
    protected LayoutInflater mInflater;
    private List<SortModel> list;
    private OnItemClickListener mOnItemClickListener;

    public  void  setItemClickListener(OnItemClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public AddressSearchAdapter(MainActivity mainActivity, List<SortModel> filterDateList) {
        this.mContext = mainActivity;
        this.list = filterDateList != null ? filterDateList : new ArrayList<SortModel>();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = mInflater.inflate(R.layout.address_item_city_layout, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = list.get(position).getName();
        holder.mCity.setText(name);
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mCity;
        LinearLayout mItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mCity = (TextView) itemView.findViewById(R.id.tv_city);
            mItem = (LinearLayout) itemView.findViewById(R.id.item_city);
        }
    }
}



