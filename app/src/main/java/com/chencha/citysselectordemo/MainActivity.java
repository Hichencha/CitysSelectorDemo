package com.chencha.citysselectordemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chencha.citysselectordemo.adapter.AddressSearchAdapter;
import com.chencha.citysselectordemo.adapter.CityItemAdapter;
import com.chencha.citysselectordemo.adapter.CitySortAdapter;
import com.chencha.citysselectordemo.adapter.SelectCityAdapter;
import com.chencha.citysselectordemo.bean.CityInfo;
import com.chencha.citysselectordemo.bean.RegionInfo;
import com.chencha.citysselectordemo.bean.SortModel;
import com.chencha.citysselectordemo.utils.CharacterParser;
import com.chencha.citysselectordemo.utils.ClearEditText;
import com.chencha.citysselectordemo.utils.OnItemClickListener;
import com.chencha.citysselectordemo.utils.PinyinComparator;
import com.chencha.citysselectordemo.view.SideBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ClearEditText filterEdit;
    private ListView countryLvcountry;
    private TextView dialog;
    private SideBar sidrbar;

    private LinearLayout mLayoutContent, mLinearCity;
    private LinearLayout currentLocation;
    private TextView tvCurrent;
    private RecyclerView mCountyItemList, mNearestItemList, mHotList;
    private LinearLayout nearestLocation;
    private LinearLayout hotCityLocation;
    private TextView mSelectselCounty;


    private CitySortAdapter mCitySortAdapter;
    private CityItemAdapter mCityItemAdapter;
    private AddressSearchAdapter mSearchAdapter; //搜索
    private SelectCityAdapter mSelectCityAdapter;   //选择的城市  对应的  区县


    private List<RegionInfo> mRegionInfoList;  //存储所有省市区   列表
    private List<RegionInfo> mHotCitys;//热门城市列表
    private List<String> provinces;
    private List<RegionInfo.AreasBean> mAreasBeanList;
    //城市
    List<CityInfo.CitiesBean.CityBean> mCityBeanList = new ArrayList<CityInfo.CitiesBean.CityBean>();
    //区县
    List<CityInfo.CitiesBean.CityBean.AreasBean.AreaBean> mAreaBeanList = new ArrayList<CityInfo.CitiesBean.CityBean.AreasBean.AreaBean>();
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;
    private RecyclerView rvResult;

    private String flag;
    private FrameLayout frameLayout;
    private String cityName, mSelectCityName, mCityCounty;

    private String capitalSelect, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

    }

    private void initView() {
        //添加 headview
        View mHeadView = View.inflate(this, R.layout.head_city_layout, null);
        mLayoutContent = (LinearLayout) mHeadView.findViewById(R.id.head_city);
        currentLocation = (LinearLayout) mHeadView.findViewById(R.id.current_location);
        tvCurrent = (TextView) mHeadView.findViewById(R.id.tvCurrent);
        mCountyItemList = (RecyclerView) mHeadView.findViewById(R.id.name_item);
        mLinearCity = (LinearLayout) mHeadView.findViewById(R.id.city_list);

        mNearestItemList = (RecyclerView) mHeadView.findViewById(R.id.nearest_name_item);
        mHotList = (RecyclerView) mHeadView.findViewById(R.id.hot_name_item);
        nearestLocation = (LinearLayout) mHeadView.findViewById(R.id.nearest_location);
        hotCityLocation = (LinearLayout) mHeadView.findViewById(R.id.hot_city_location);

        filterEdit = (ClearEditText) findViewById(R.id.filter_edit);
        countryLvcountry = (ListView) findViewById(R.id.country_lvcountry);
        dialog = (TextView) findViewById(R.id.dialog);
        sidrbar = (SideBar) findViewById(R.id.sidrbar);
        rvResult = (RecyclerView) findViewById(R.id.rv_result);
        frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mSelectselCounty = (TextView) mHeadView.findViewById(R.id.selsect_county_district);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            cityName = bundle.getString("city");
            mCityCounty = bundle.getString("LocationCity");
        }
        if (!TextUtils.isEmpty(mCityCounty)) {
            tvCurrent.setText("您正在看：" + cityName + mCityCounty);
        } else {
            tvCurrent.setText("您正在看：" + cityName);
        }

        //实例化 汉字转拼音
        characterParser = characterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        countryLvcountry.addHeaderView(mHeadView);

        sidrbar.setTextView(dialog);
        sidrbar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                if (s.equals("定位")) {
                    countryLvcountry.setSelection(0);
                }
                int position = mCitySortAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    countryLvcountry.setSelection(position);
                }
            }
        });


    }

    //数据
    private void initData() {
        Type mType = new TypeToken<List<CityInfo>>() {
        }.getType();
        final List<CityInfo> beanList = new Gson().fromJson(getAssetsData("address.json"), mType);
        mRegionInfoList = new ArrayList<RegionInfo>();
        List<RegionInfo> regionInfos = new ArrayList<RegionInfo>();
        for (CityInfo cityInfo : beanList) {
            RegionInfo regionInfo = new RegionInfo();
            String capital = cityInfo.getSsqname();
            if (capital.equals("北京") || capital.equals("天津") || capital.equals("上海") || capital.equals("重庆")) {
                //省会名称   ---> 只保存  直辖市
                regionInfo.setName(cityInfo.getSsqname() + "市");
                regionInfos.add(regionInfo);
            } else {
                //其他省  是保存  市
                CityInfo.CitiesBean citiesBean = cityInfo.getCities();
                List<CityInfo.CitiesBean.CityBean> cityBeen = citiesBean.getCity();
                for (CityInfo.CitiesBean.CityBean cityBean : cityBeen) {
                    RegionInfo regionInfo1 = new RegionInfo();
                    regionInfo1.setId(Integer.parseInt(cityBean.getSsqid()));
                    regionInfo1.setName(cityBean.getSsqname());
                    regionInfos.add(regionInfo1);
                }
            }
        }
        mRegionInfoList.addAll(regionInfos);

        Log.d("city", String.valueOf(mRegionInfoList));
        //转化数据
        SourceDateList = filledData(mRegionInfoList);
        // 排序
        Collections.sort(SourceDateList, pinyinComparator);
        mCitySortAdapter = new CitySortAdapter(this, SourceDateList);
        countryLvcountry.setAdapter(mCitySortAdapter);


        //热门城市
        mHotCitys = new ArrayList<>();
        mHotCitys.add(new RegionInfo(1, 1, "北京"));
        mHotCitys.add(new RegionInfo(2, 2, "无锡"));
        mHotCitys.add(new RegionInfo(3, 2, "南通"));
        mHotCitys.add(new RegionInfo(2, 1, "杭州"));
        mHotCitys.add(new RegionInfo(1, 1, "广州"));
        mHotCitys.add(new RegionInfo(3, 3, "宁波"));
        mHotCitys.add(new RegionInfo(1, 1, "天津"));
        mHotCitys.add(new RegionInfo(3, 3, "嘉兴"));

        mHotList.setLayoutManager(new GridLayoutManager(this, 3));
        mCityItemAdapter = new CityItemAdapter(this, mHotCitys);
        mHotList.setAdapter(mCityItemAdapter);
        mCityItemAdapter.notifyDataSetChanged();

        //热门城市
        mCityItemAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                cityName = mHotCitys.get(position).getName() + "市";
                tvCurrent.setText("您正在看：" + cityName);
//                SPUtils.put("city_county","");
//                SPUtils.put("cityName", cityName);
                frameLayout.setVisibility(View.VISIBLE);
                rvResult.setVisibility(View.GONE);
                mLinearCity.setVisibility(View.GONE);
                filterEdit.setText("");
                hideSoftInput(filterEdit.getWindowToken());
                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, FistMainActivity.class);
                bundle.putString("city", cityName);
                bundle.putString("LocationCity", "");
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {

            }
        });


        provinces = new ArrayList<String>();
        for (RegionInfo info : mRegionInfoList) {
            provinces.add(info.getName().trim());
        }

        countryLvcountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                hideSoftInput(filterEdit.getWindowToken());
//                Intent data = new Intent();
//                data.putExtra("cityName", ((SortModel) mCitySortAdapter.getItem(position - 1)).getName());
                //设置选择的城市
                cityName = ((SortModel) mCitySortAdapter.getItem(position - 1)).getName();
//                SPUtils.put("cityName", cityName);
//                SPUtils.put("city_county","");
                tvCurrent.setText("您正在看：" + cityName);
                //选择城市  默认关闭  区县列表
                if (mLinearCity.getVisibility() == View.VISIBLE) {
                    mLinearCity.setVisibility(View.GONE);
                }

            }
        });

        //搜索
        filterEdit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                if (s.length() > 0) {
                    frameLayout.setVisibility(View.GONE);
                    rvResult.setVisibility(View.VISIBLE);
                    mLinearCity.setVisibility(View.GONE);
                } else {
                    frameLayout.setVisibility(View.VISIBLE);
                    rvResult.setVisibility(View.GONE);
                    mLinearCity.setVisibility(View.GONE);

                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }
            }
        });

        mSelectselCounty.setOnClickListener(this);


        mSelectselCounty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityName != null) {
                    if (mLinearCity.getVisibility() == View.GONE) {
                        mLinearCity.setVisibility(View.VISIBLE);
                    } else {
                        mLinearCity.setVisibility(View.GONE);
                    }
                    List<RegionInfo.AreasBean> areasBeen = new ArrayList<RegionInfo.AreasBean>();
                    mAreasBeanList = new ArrayList<RegionInfo.AreasBean>();
                    areasBeen.clear();
                    mAreasBeanList.clear();
                    mCityBeanList.clear();  //城市
                    mAreaBeanList.clear();  //区县

                    //城市  直辖市
                    if (cityName.equals("北京市") || cityName.equals("天津市") || cityName.equals("上海市") || cityName.equals("重庆市")) {
                        //拿取相对应  的城市  县区数据
                        for (CityInfo cityInfo : beanList) {
                            capitalSelect = cityInfo.getSsqname();
                            mSelectCityName = capitalSelect + "市";
                            // 直辖市   ---> 保存 区，县
                            if (cityName.equals(mSelectCityName)) {
                                CityInfo.CitiesBean citiesBeen = cityInfo.getCities();
                                List<CityInfo.CitiesBean.CityBean> cityBeanListNew = new ArrayList<CityInfo.CitiesBean.CityBean>();
                                List<CityInfo.CitiesBean.CityBean> cityBeanList = citiesBeen.getCity();   //城市
                                for (CityInfo.CitiesBean.CityBean cityBean : cityBeanList) {
                                    CityInfo.CitiesBean.CityBean.AreasBean areasBean = cityBean.getAreas();
                                    List<CityInfo.CitiesBean.CityBean.AreasBean.AreaBean> areaBeanList = areasBean.getArea();
                                    for (CityInfo.CitiesBean.CityBean.AreasBean.AreaBean arealist : areaBeanList) {
                                        RegionInfo.AreasBean regionInfoCity = new RegionInfo.AreasBean();
                                        regionInfoCity.setSsqid(arealist.getSsqid());
                                        regionInfoCity.setSsqname(arealist.getSsqname());
                                        areasBeen.add(regionInfoCity);
                                    }
                                }
                                mAreasBeanList.addAll(areasBeen);
                                initCityData(mAreasBeanList);
                            }
                        }
                    } else {
                        mAreasBeanList.clear();
                        for (CityInfo cityInfo : beanList) {
                            CityInfo.CitiesBean citiesBeen = cityInfo.getCities();
                            List<CityInfo.CitiesBean.CityBean> cityBeanListNew = new ArrayList<CityInfo.CitiesBean.CityBean>();
                            List<CityInfo.CitiesBean.CityBean> cityBeanList = citiesBeen.getCity();   //城市
                            for (CityInfo.CitiesBean.CityBean cityBean1 : cityBeanList) {
                                city = cityBean1.getSsqname();
                                if (cityName.equals(city)) {
                                    CityInfo.CitiesBean.CityBean.AreasBean areasBeanList = cityBean1.getAreas();
                                    List<CityInfo.CitiesBean.CityBean.AreasBean.AreaBean> areaBeanList = areasBeanList.getArea();
                                    for (CityInfo.CitiesBean.CityBean.AreasBean.AreaBean arealist : areaBeanList) {
                                        RegionInfo.AreasBean regionInfoOtherCity = new RegionInfo.AreasBean();
                                        regionInfoOtherCity.setSsqid(arealist.getSsqid());
                                        regionInfoOtherCity.setSsqname(arealist.getSsqname());
                                        areasBeen.add(regionInfoOtherCity);
                                    }
                                    mAreasBeanList.addAll(areasBeen);
                                    initCityData(mAreasBeanList);
                                }
                            }
                        }
                    }
                } else {
                    Toast.makeText(MainActivity.this, "请先选择城市", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    /**
     * 选择城市   相对应区县
     *
     * @param areasBeanList
     */

    private void initCityData(final List<RegionInfo.AreasBean> areasBeanList) {
        mCountyItemList.setLayoutManager(new GridLayoutManager(this, 3));
        mSelectCityAdapter = new SelectCityAdapter(this, areasBeanList);
        mCountyItemList.setAdapter(mSelectCityAdapter);
        mSelectCityAdapter.notifyDataSetChanged();

        mSelectCityAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String mDistrict_county = areasBeanList.get(position).getSsqname();
                tvCurrent.setText("您正在看：" + cityName + mDistrict_county);
                //需求 返回首页  --->首页数据展示  区县  -->选择裂变展示  城市
//                SPUtils.put("cityName", cityName);
//                SPUtils.put("city_county", mDistrict_county);  //区县  首页显示

                Bundle bundle = new Bundle();
                Intent intent = new Intent(MainActivity.this, FistMainActivity.class);
                bundle.putString("city", cityName);
                bundle.putString("LocationCity", mDistrict_county);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view) {

            }
        });
    }


    /**
     * 为listView 填充数据
     *
     * @param regionInfoList
     * @return
     */
    private List<SortModel> filledData(List<RegionInfo> regionInfoList) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < regionInfoList.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(regionInfoList.get(i).getName());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(regionInfoList.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
            countryLvcountry.setSelection(0);
            flag = "one";
        } else {
            if (!provinces.contains(filterStr)) {
                filterDateList.clear();
                for (SortModel sortModel : SourceDateList) {
                    String name = sortModel.getName();
                    if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add(sortModel);
                    }
                }
                flag = "two";
            }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        if (flag.equals("one")) {
            mCitySortAdapter.updateListView(filterDateList);
        } else if (flag.equals("two")) {
            rvResult.setLayoutManager(new LinearLayoutManager(this));
            DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            rvResult.addItemDecoration(itemDecoration);
            mSearchAdapter = new AddressSearchAdapter(this, filterDateList);
            rvResult.setAdapter(mSearchAdapter);
            mSearchAdapter.notifyDataSetChanged();


            final List<SortModel> finalFilterDateList = filterDateList;
            mSearchAdapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    cityName = finalFilterDateList.get(position).getName();
                    Log.d("aaaa", cityName);
//                    SPUtils.put("cityName", cityName);
//                    SPUtils.put("city_county","");
                    tvCurrent.setText("您正在看：" + cityName);
                    frameLayout.setVisibility(View.VISIBLE);
                    rvResult.setVisibility(View.GONE);
                    mLinearCity.setVisibility(View.GONE);
                    filterEdit.setText("");
                    hideSoftInput(filterEdit.getWindowToken());

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(MainActivity.this, FistMainActivity.class);
                    bundle.putString("city", cityName);
                    bundle.putString("LocationCity", "");
                    intent.putExtras(bundle);
                    startActivity(intent);

                }

                @Override
                public void onItemLongClick(View view) {

                }
            });

        }

    }


    private String getAssetsData(String path) {
        String result = "";
        try {
            //获取输入流
            InputStream mAssets = this.getAssets().open(path);
            //获取文件的字节数
            int lenght = mAssets.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据写入到字节数组中
            mAssets.read(buffer);
            mAssets.close();
            result = new String(buffer);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("fuck", e.getMessage());
            return result;
        }
    }


    /**
     * ru软键盘
     *
     * @param token
     */
    protected void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onClick(View v) {

    }
}
