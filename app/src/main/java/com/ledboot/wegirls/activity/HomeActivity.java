package com.ledboot.wegirls.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ledboot.wegirls.R;
import com.ledboot.wegirls.event.LoginEvent;
import com.ledboot.wegirls.fragment.Beauty;
import com.ledboot.wegirls.utils.Debuger;
import com.ledboot.wegirls.utils.UiHelper;

import de.greenrobot.event.EventBus;

public class HomeActivity extends BaseActivity {

    public static String TAG = HomeActivity.class.getSimpleName();

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FloatingActionButton fab;
    FrameLayout contentFrame;
    NavigationView nvView;
    ActionBarDrawerToggle drawerToggle;

    Fragment currentFragment;
    View navHeader;

    ImageView userIcon;
    TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contentFrame = (FrameLayout) findViewById(R.id.content_frame);
        nvView = (NavigationView) findViewById(R.id.nvView);

        navHeader = nvView.inflateHeaderView(R.layout.nav_header);

        navHeader.setClickable(true);
        userIcon = (ImageView) navHeader.findViewById(R.id.user_icon);
        userName = (TextView) navHeader.findViewById(R.id.user_name);

        drawerToggle = setupDrawerToggle();
        setSupportActionBar(toolbar);
        setupDrawerContent(nvView);

    }

    private void initData() {
        setTitle(R.string.drawer_menu_weigirls);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new Beauty();
        transaction.replace(R.id.content_frame, currentFragment);
        transaction.commit();
    }

    private void setListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        navHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                UiHelper.showLogin(mContext, AccountActivity.EXTRA_LOGIN);
            }
        });

        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    public void onEventMainThread(LoginEvent event) {
        Debuger.logD(TAG,"来数据了");
        userName.setText(event.getUser().getUsername());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return false;
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        setTitle(menuItem.getTitle());
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = Beauty.class;
                break;
            default:
                fragmentClass = Beauty.class;
                break;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment.getClass().getName().equals(currentFragment.getClass().getName())) {
            return;
        }
        currentFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }
}
