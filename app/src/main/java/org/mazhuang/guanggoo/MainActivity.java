package org.mazhuang.guanggoo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.Topic;
import org.mazhuang.guanggoo.data.task.AuthCheckTask;
import org.mazhuang.guanggoo.login.LoginFragment;
import org.mazhuang.guanggoo.login.LoginPresenter;
import org.mazhuang.guanggoo.topiclist.TopicListFragment;
import org.mazhuang.guanggoo.topiclist.TopicListPresenter;
import org.mazhuang.guanggoo.util.ConstantUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TopicListFragment.OnListFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener {

    private static String sStackName = MainActivity.class.getName();

    private long mLastBackPressTime;

    private ImageView mAvatarImageView;
    private TextView mUsernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initLoginedInUserInfo();
    }

    private void initLoginedInUserInfo() {
        if (AuthInfoManager.getInstance().isLoginedIn()) {
            Glide.with(this)
                    .load(AuthInfoManager.getInstance().getAvatar())
                    .centerCrop()
                    .crossFade()
                    .into(mAvatarImageView);

            mUsernameTextView.setText(AuthInfoManager.getInstance().getUsername());
        }
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAvatarImageView = navigationView.getHeaderView(0).findViewById(R.id.avatar);
        mUsernameTextView = navigationView.getHeaderView(0).findViewById(R.id.username);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AuthInfoManager.getInstance().isLoginedIn()) {
                    // TODO: 2017/9/17 跳转到个人页面
                } else {
                    drawer.closeDrawer(GravityCompat.START);
                    gotoLoginPage(null);
                }
            }
        };
        mAvatarImageView.setOnClickListener(listener);
        mUsernameTextView.setOnClickListener(listener);

        TopicListFragment fragment = new TopicListFragment();
        new TopicListPresenter(fragment, ConstantUtil.BASE_URL);

        addFragmentToStack(getSupportFragmentManager(), fragment);
    }

    public static void addFragmentToStack(FragmentManager fm, Fragment fragment) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(sStackName);
        ft.commit();
    }

    public static void setOnlyFragmentToStack(FragmentManager fm, Fragment fragment) {
        fm.popBackStackImmediate(sStackName, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        addFragmentToStack(fm, fragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (fragment instanceof BaseFragment) {
                if (((BaseFragment) fragment).onBackPressed()) {
                    return;
                }
            }

            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                long backPressTime = System.nanoTime();
                long ONE_SECOND_NANO = 1000 * 1000 * 1000L;
                if (backPressTime - mLastBackPressTime > ONE_SECOND_NANO) {
                    Toast.makeText(this, getString(R.string.back_to_quit), Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                mLastBackPressTime = backPressTime;
                return;
            }

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_nodes) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gotoLoginPage(String url) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof LoginFragment) {
            return;
        }

        LoginFragment fragment = new LoginFragment();
        new LoginPresenter(fragment);

        if (!TextUtils.isEmpty(url)) {
            Bundle bundle = new Bundle();
            bundle.putString(LoginFragment.KEY_REFERER, url);
            fragment.setArguments(bundle);
        }

        addFragmentToStack(getSupportFragmentManager(), fragment);
    }

    @Override
    public void onListFragmentInteraction(final Topic item) {
        NetworkTaskScheduler.getInstance().execute(new AuthCheckTask(new OnResponseListener<String>() {
            @Override
            public void onSucceed(String data) {
                // TODO: 2017/9/17 打开主题详情页
            }

            @Override
            public void onFailed(String msg) {
                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                LoginFragment fragment = new LoginFragment();
                Bundle bundle = new Bundle();
                bundle.putString(LoginFragment.KEY_REFERER, item.getUrl());
                fragment.setArguments(bundle);
                new LoginPresenter(fragment);
                addFragmentToStack(getSupportFragmentManager(), fragment);
            }
        }));
    }

    @Override
    public void onFragmentInteraction(String refererUrl) {
        initLoginedInUserInfo();

        // TODO: 2017/9/17 根据触发登录的类型和页面，这里设置跳转
    }
}
