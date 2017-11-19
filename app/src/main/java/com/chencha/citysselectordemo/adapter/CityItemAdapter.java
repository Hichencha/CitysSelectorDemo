package com.chencha.citysselectordemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chencha.citysselectordemo.MainActivity;
import com.chencha.citysselectordemo.R;
import com.chencha.citysselectordemo.bean.RegionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc:  热门城市 最近访问
 * Author: chenhca
 * Date: 17/11/13
 */

public class CityItemAdapter extends RecyclerView.Adapter<CityItemAdapter.Viewholder> {

    private LayoutInflater inflater;
    private Context mContext;
    private List<RegionInfo> mInfoList = new ArrayList<>();

    public CityItemAdapter(MainActivity mainActivity, List<RegionInfo> hotCitys) {
        this.mContext = mainActivity;
        this.mInfoList = hotCitys;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = inflater.inflate(R.layout.item_name_city, parent, false);
        return new Viewholder(mView);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
          String   mCityName = mInfoList.get(position).getName();
        holder.mTitle.setText(mCityName);
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView  mTitle;
        public Viewholder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.id_tv_cityname);
        }
    }
}
