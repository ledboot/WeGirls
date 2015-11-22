package com.ledboot.wegirls.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ledboot.wegirls.R;
import com.ledboot.wegirls.widget.SwipeableLayout;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/18.
 */
public class ModelGalleryActivity extends BaseActivity implements SwipeableLayout.OnLayoutCloseListener{


    SwipeableLayout swipeableLayout;
    ViewPager viewPager;
    android.support.v4.view.PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_gallery);
        initView();
        initData();

    }

    private void initView(){
        swipeableLayout = (SwipeableLayout) findViewById(R.id.swipableLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        swipeableLayout.setOnLayoutCloseListener(this);
    }

    private void initData(){
        ArrayList<String> list = getIntent().getStringArrayListExtra("picList");
        adapter = new PagerAdapter(this,list);
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onLayoutClose() {
        onBackPressed();
    }

    class PagerAdapter extends android.support.v4.view.PagerAdapter{

        private Context mContext;
        private ArrayList<String> mList;
        LayoutInflater mLayoutInflater;


        public PagerAdapter(Context context,ArrayList<String> list) {
            this.mContext = context;
            this.mList = list;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == (View)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.pager_item, container, false);
            ImageView imageView = (ImageView)view.findViewById(R.id.image);
            String url = mList.get(position);
            Glide.with(mContext).load(url).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
