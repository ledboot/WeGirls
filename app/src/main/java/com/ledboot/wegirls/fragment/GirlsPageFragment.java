package com.ledboot.wegirls.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.ledboot.wegirls.Boot;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.bean.Girl;
import com.ledboot.wegirls.request.GoJsonRequest;
import com.ledboot.wegirls.request.GoRequestError;
import com.ledboot.wegirls.utils.Config;
import com.ledboot.wegirls.utils.DateStyle;
import com.ledboot.wegirls.utils.DateUtil;
import com.ledboot.wegirls.utils.Debuger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/3 0003.
 */
public class GirlsPageFragment extends BaseFragment {

    private int type;

    SwipeRefreshLayout swiper;
    RecyclerView recycler;

    public static GirlsPageFragment newInstance(int position){
        Bundle args = new Bundle();
        GirlsPageFragment fragment = new GirlsPageFragment();
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
        recycler.setLayoutManager(linearLayoutManager);

        swiper.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // 这句话是为了，第一次进入页面的时候显示加载进度条
        swiper.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

    }

    private void initData(){
        List<Girl> list = new ArrayList<>();
        GirlsRecyclerAdapter adapter = new GirlsRecyclerAdapter(list);
        recycler.setAdapter(adapter);
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
        StringBuffer buffer = new StringBuffer("http://route.showapi.com/197-1?");
        buffer.append("showapi_appid="+Boot.YI_YUAN_APPID);
        buffer.append("&showapi_sign=" + Boot.YI_YUAN_SECRET);
        buffer.append("&showapi_timestamp=" + DateUtil.toString(System.currentTimeMillis(), DateStyle.YYYMMddHHmmss));
        buffer.append("&num=" + 10);
        buffer.append("&page=" + 1);
        GoJsonRequest request = new GoJsonRequest(buffer.toString(), Request.Method.GET) {
            @Override
            protected void onJsonResponse(JSONObject response) {
                swiper.setRefreshing(false);
                Debuger.logD(response.toString());
            }

            @Override
            protected void onJsonError(GoRequestError err) {
                swiper.setRefreshing(false);
            }
        };
        request.perform();
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
            Glide.with(mContext).load(girl.getCoverUrl()).into(holder.cover);
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
