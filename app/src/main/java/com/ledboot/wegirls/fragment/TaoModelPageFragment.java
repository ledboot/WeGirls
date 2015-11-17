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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.ledboot.wegirls.Boot;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.bean.Girl;
import com.ledboot.wegirls.bean.TaoModel;
import com.ledboot.wegirls.request.GoJsonRequest;
import com.ledboot.wegirls.request.GoRequestError;
import com.ledboot.wegirls.utils.Constant;
import com.ledboot.wegirls.utils.DateStyle;
import com.ledboot.wegirls.utils.DateUtil;
import com.ledboot.wegirls.utils.Debuger;
import com.ledboot.wegirls.widget.recyclerview.InfiniteScrollListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2015/11/9.
 */
public class TaoModelPageFragment extends BaseFragment {

    public static String TAG = TaoModelPageFragment.class.getSimpleName();

    SwipeRefreshLayout swiper;
    RecyclerView recycler;
    private static final int PAGE_SIZE =20;
    private TaoRecyclerAdapter recyclerAdapter;
    private List<TaoModel> list;
    private int currentPage = 1;
    InfiniteScrollListener infiniteScrollListener;
    LinearLayoutManager linearLayoutManager;

    public static TaoModelPageFragment newInstance(){
        Bundle args = new Bundle();
        TaoModelPageFragment  fragment = new TaoModelPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Debuger.logD(TAG,"onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Debuger.logD(TAG,"onCreateView()");
        View view = inflater.inflate(R.layout.girls_page_frament,null);
        initView(view);
        initData();
        setListener();
        return view;
    }

    private void initView(View view){
        swiper = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        recycler.setLayoutManager(linearLayoutManager);

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
        infiniteScrollListener = new InfiniteScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                ++currentPage;
                syncData(false);
            }
        };
        list = new ArrayList<>();
        recyclerAdapter = new TaoRecyclerAdapter(list);
        recycler.setAdapter(recyclerAdapter);
        recycler.addOnScrollListener(infiniteScrollListener);
        syncData(true);
    }

    private void setListener(){
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                syncData(true);
            }
        });
    }

    private void syncData(final boolean refresh){
        if(refresh){
            swiper.setRefreshing(true);
        }else{
            infiniteScrollListener.setLoad(true);
        }
        StringBuffer buffer = new StringBuffer(Constant.TAO_MODEL_URL);
        buffer.append("showapi_appid="+ Boot.YI_YUAN_APPID);
        buffer.append("&showapi_sign=" + Boot.YI_YUAN_SECRET);
        buffer.append("&showapi_timestamp=" + DateUtil.toString(System.currentTimeMillis(), DateStyle.YYYMMddHHmmss));
        buffer.append("&type=");
        buffer.append("&page=" + currentPage);
        GoJsonRequest request = new GoJsonRequest(buffer.toString(), Request.Method.GET) {
            @Override
            protected void onJsonResponse(JSONObject response) {
                com.alibaba.fastjson.JSONObject object = com.alibaba.fastjson.JSONObject.parseObject(response.toString());
                if(refresh){
                    swiper.setRefreshing(false);
                }else{
                    infiniteScrollListener.setLoad(false);
                }
                buildData(object,refresh);
            }

            @Override
            protected void onJsonError(GoRequestError err) {
                swiper.setRefreshing(false);
            }
        };
        request.perform();
    }

    private void buildData(com.alibaba.fastjson.JSONObject object,boolean refresh){
        if(0 == object.getIntValue("showapi_res_code") ){
            com.alibaba.fastjson.JSONObject jsonObject = object.getJSONObject("showapi_res_body").getJSONObject("pagebean");
            JSONArray array = jsonObject.getJSONArray("contentlist");
            if(refresh){
                list.clear();
            }
            for(int i =0;i<array.size();i++){
                com.alibaba.fastjson.JSONObject item = array.getJSONObject(i);
                String avatarUrl = item.getString("avatarUrl");
                String city= item.getString("city");
                String height = item.getString("height");
                String weight= item.getString("weight");
                JSONArray imgArr = item.getJSONArray("imgList");
                List<String> imgList = new ArrayList<>();
                for(Object o : imgArr){
                    imgList.add(o.toString());
                }
                String realName= item.getString("realName");
                String userId= item.getString("userId");
                String link= item.getString("link");
                String totalFanNum= item.getString("totalFanNum");
                TaoModel model = new TaoModel(avatarUrl,city,height,weight,realName,userId,link,totalFanNum);
                model.setImgList(imgList);
                list.add(model);
            }
            recyclerAdapter.notifyDataSetChanged();
        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "数据请求出错！", Toast.LENGTH_SHORT).show();
                }
            });
            Debuger.logD(TAG, object.getString("showapi_res_error"));
        }
    }

    class TaoRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

        List<TaoModel> mList;

        public TaoRecyclerAdapter(List<TaoModel> list) {
            mList = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.tao_girls_item,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TaoModel girl = mList.get(position);
            Glide.with(mContext).load(girl.getAvatarUrl()).into(holder.cover);
            holder.name.setText(girl.getRealName());
            holder.city.setText(girl.getCity());
            String heightAndWeight = String.format("%s cm/%s kg",girl.getHeight(),girl.getWeight());
            holder.heightAndWeight.setText(heightAndWeight);
            holder.favour.setText(girl.getTotalFanNum());
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cover;
        TextView name;
        TextView city;
        TextView heightAndWeight;
        TextView favour;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView)itemView.findViewById(R.id.cover);
            name = (TextView) itemView.findViewById(R.id.name);
            city = (TextView) itemView.findViewById(R.id.city);
            heightAndWeight = (TextView) itemView.findViewById(R.id.heightAndweight);
            favour = (TextView) itemView.findViewById(R.id.favour);
        }
    }
}
