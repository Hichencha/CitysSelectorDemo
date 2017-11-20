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
import com.chencha.citysselectordemo.bean.RegionInfo;
import com.chencha.citysselectordemo.utils.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:
 * Author:
 * Date: 17/11/17
 */

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context mContext;
    private List<RegionInfo.AreasBean> mInfoList = new ArrayList<RegionInfo.AreasBean>();

    private OnItemClickListener mOnItemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public SelectCityAdapter(MainActivity mainActivity, List<RegionInfo.AreasBean> areasBeanList) {
        this.mContext = mainActivity;
        this.mInfoList = areasBeanList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = inflater.inflate(R.layout.item_name_city, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String mCityName = mInfoList.get(position).getSsqname();
        holder.mTitle.setText(mCityName);
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        LinearLayout mItem;
        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.id_tv_cityname);
            mItem = (LinearLayout) itemView.findViewById(R.id.item);
        }
    }
}
