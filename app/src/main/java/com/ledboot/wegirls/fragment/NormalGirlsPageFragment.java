package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.ledboot.wegirls.Boot;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.bean.Girl;
import com.ledboot.wegirls.request.GoJsonRequest;
import com.ledboot.wegirls.request.GoRequestError;
import com.ledboot.wegirls.utils.Constant;
import com.ledboot.wegirls.utils.DateStyle;
import com.ledboot.wegirls.utils.DateUtil;
import com.ledboot.wegirls.utils.Debuger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class NormalGirlsPageFragment extends BaseFragment {

    public static String TAG = NormalGirlsPageFragment.class.getSimpleName();

    public static final int GIRL_TYPE_NORMAL=1;
    public static final int GIRL_TYPE_TAO_MODEL =2;

    private int type;

    public static final int SEX_GIRLS_PAGESIZE=20;

    public static final int SPAN_COUNT=2;

    SwipeRefreshLayout swiper;
    RecyclerView recycler;

    List<Girl> list;

    GirlsRecyclerAdapter recyclerAdapter;

    public static NormalGirlsPageFragment newInstance(){
        Bundle args = new Bundle();
        NormalGirlsPageFragment fragment = new NormalGirlsPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.girls_page_frament,container,false);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        swiper = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext,SPAN_COUNT);
        recycler.setLayoutManager(gridLayoutManager);

        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 这句话是为了，第一次进入页面的时候显示加载进度条s
        swiper.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

    }

    private void initData(){
        list = new ArrayList<>();
        recyclerAdapter = new GirlsRecyclerAdapter(list);
        recycler.setAdapter(recyclerAdapter);
        syncData();
    }


    private void setListener(){
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                syncData();
            }
        });
    }

    private void syncData(){
        swiper.setRefreshing(true);
        StringBuffer buffer = new StringBuffer(Constant.NORMAL_GIRL_URL);
        buffer.append("showapi_appid="+Boot.YI_YUAN_APPID);
        buffer.append("&showapi_sign=" + Boot.YI_YUAN_SECRET);
        buffer.append("&showapi_timestamp=" + DateUtil.toString(System.currentTimeMillis(), DateStyle.YYYMMddHHmmss));
        buffer.append("&num=" + SEX_GIRLS_PAGESIZE);
        buffer.append("&page=" + 1);
        GoJsonRequest request = new GoJsonRequest(buffer.toString(), Request.Method.GET) {
            @Override
            protected void onJsonResponse(JSONObject response) {
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(response.toString());
                swiper.setRefreshing(false);
                buildData(object);
            }

            @Override
            protected void onJsonError(GoRequestError err) {
                swiper.setRefreshing(false);
            }
        };
        request.perform();
    }

    private void buildData(com.alibaba.fastjson.JSONObject object){
        if(0 == object.getIntValue("showapi_res_code") ){
            com.alibaba.fastjson.JSONObject jsonObject = object.getJSONObject("showapi_res_body");
            for(int i =0;i<SEX_GIRLS_PAGESIZE-2;i++){
                com.alibaba.fastjson.JSONObject one = jsonObject.getJSONObject(String.valueOf(i));
                Girl girl = new Girl(one.getString("description"),one.getString("picUrl"),one.getString("url"));
                list.add(girl);
            }
            recyclerAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(mContext,"数据请求出错！",Toast.LENGTH_SHORT).show();
            Debuger.logD(TAG,object.getString("showapi_res_error"));
        }
    }



    class GirlsRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

        List<Girl> mList;

        public GirlsRecyclerAdapter(List<Girl> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.girls_item,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Girl girl = mList.get(position);
            Glide.with(mContext).load(girl.getCoverUrl()).centerCrop().into(holder.cover);
            holder.des.setText(girl.getDes());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView des;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView)itemView.findViewById(R.id.cover);
            des = (TextView) itemView.findViewById(R.id.des);
        }
    }
}
