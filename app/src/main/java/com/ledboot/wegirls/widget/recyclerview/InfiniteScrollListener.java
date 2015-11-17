package com.ledboot.wegirls.widget.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ledboot.wegirls.utils.Debuger;

/**
 * Created by Administrator on 2015/11/17 0017.
 */
public abstract class InfiniteScrollListener extends RecyclerView.OnScrollListener {

    public static String TAG = InfiniteScrollListener.class.getSimpleName();

    private static int LAYOUT_TYPE_LINEAR = 1;
    private static int LAYOUT_TYPE_GRID =2;
    private boolean mLoading = false;
    private static final int VISIBLE_THRESHOLD = 4;


    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private int layoutType;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
    }


    public InfiniteScrollListener(RecyclerView.LayoutManager layoutManager){
        if(layoutManager instanceof GridLayoutManager){
            gridLayoutManager = (GridLayoutManager)layoutManager;
            layoutType = LAYOUT_TYPE_GRID;
        }else if(layoutManager instanceof LinearLayoutManager){
            linearLayoutManager = (LinearLayoutManager)layoutManager;
            layoutType = LAYOUT_TYPE_LINEAR;
        }else{
            new IllegalArgumentException("now only support GridLayoutManager and LinearLayoutManager!");
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = 0;
        int firstVisibleItem =0;
        if(layoutType == LAYOUT_TYPE_GRID){
            totalItemCount = gridLayoutManager.getItemCount();
            firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
        }else if(layoutType == LAYOUT_TYPE_LINEAR){
            totalItemCount = linearLayoutManager.getItemCount();
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
        }
        if(!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem+VISIBLE_THRESHOLD)){
            Debuger.logD(TAG,"onLoadMore()");
            Debuger.logD(TAG,"totalItemCount ="+totalItemCount+",firstVisibleItem="+firstVisibleItem+",visibleItemCount="+visibleItemCount);
            onLoadMore();
        }

    }

    public void setLoad(boolean loading){
        mLoading = loading;
    }


    public abstract void onLoadMore();
}
