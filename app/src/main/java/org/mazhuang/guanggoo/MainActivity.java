package org.mazhuang.guanggoo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.base.FragmentCallBack;
import org.mazhuang.guanggoo.data.AuthInfoManager;
import org.mazhuang.guanggoo.router.FragmentFactory;
import org.mazhuang.guanggoo.router.annotations.FinishWhenCovered;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements FragmentCallBack, NavigationView.OnNavigationItemSelectedListener {

    private static String sStackName = MainActivity.class.getName();

    private long mLastBackPressTime;

    private ImageView mAvatarImageView;
    private TextView mUsernameTextView;
    private ImageView mLogoutImageView;
    @BindView(R.id.progress) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        initViews();

        initLoginInUserInfo();
    }

    private void initLoginInUserInfo() {
        if (AuthInfoManager.getInstance().isLoginIn()) {
            Glide.with(this)
                    .load(AuthInfoManager.getInstance().getAvatar())
                    .centerCrop()
                    .crossFade()
                    .into(mAvatarImageView);

            mUsernameTextView.setText(AuthInfoManager.getInstance().getUsername());
            mLogoutImageView.setVisibility(View.VISIBLE);
        }
    }

    private void initViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mAvatarImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        mUsernameTextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.username);
        mLogoutImageView = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.logout);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUserProfile();
            }
        };
        mAvatarImageView.setOnClickListener(listener);
        mUsernameTextView.setOnClickListener(listener);
        mLogoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginStatusChanged(false);
            }
        });

        openPage(ConstantUtil.BASE_URL, getString(R.string.default_order_topics));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean canGoBack = (getSupportFragmentManager().getBackStackEntryCount() > 1);
                if (canGoBack) {
                    onBackPressed();
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fragment = getCurrentFragment();
                if (fragment instanceof BaseFragment) {
                    BaseFragment baseFragment = (BaseFragment) fragment;
                    setTitle(baseFragment.getTitle());
                    boolean canGoBack = (getSupportFragmentManager().getBackStackEntryCount() > 1);
                    if (canGoBack) {
                        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    } else {
                        toolbar.setNavigationIcon(R.drawable.ic_menu);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                }
            }
        });
    }

    private void openUserProfile() {
        String username = AuthInfoManager.getInstance().getUsername();
        String url = ConstantUtil.USER_PROFILE_SELF_FAKE_URL;
        if (!TextUtils.isEmpty(username)) {
            url = String.format(ConstantUtil.USER_PROFILE_BASE_URL, username);
        }

        openPage(url, getString(R.string.profile));
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

            if (isLoading()) {
                stopLoading();
                return;
            }

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
    public boolean isLoading() {
        return mProgressBar.getVisibility() == View.VISIBLE;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case R.id.nav_user_profile:
                openUserProfile();
                break;

            case R.id.nav_favors:
                openPage(String.format(ConstantUtil.USER_FAVORS_BASE_URL, AuthInfoManager.getInstance().getUsername()), getString(R.string.my_favors));
                break;

            case R.id.nav_nodes:
                openPage(ConstantUtil.NODES_CLOUD_URL, getString(R.string.nodes_list));
                break;

            case R.id.nav_beginner_guide:
                openPage(ConstantUtil.BEGINNER_GUIDE_URL, getString(R.string.beginner_guide));
                break;

            case R.id.nav_about:
                openPage(ConstantUtil.ABOUT_URL, getString(R.string.about));
                break;

            default:
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @Override
    public void openPage(String url, String title) {
        if (!TextUtils.isEmpty(url)) {
            Fragment fragment = getCurrentFragment();
            if (fragment instanceof BaseFragment) {
                if (url.equals(((BaseFragment) fragment).getUrl())) {
                    return;
                }

                if (fragment.getClass().isAnnotationPresent(FinishWhenCovered.class)) {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }

        BaseFragment fragment = FragmentFactory.getFragmentByUrl(url);
        if (fragment == null) {
            Toast.makeText(this, getString(R.string.error_happened), Toast.LENGTH_SHORT).show();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(BaseFragment.KEY_URL, url);
        bundle.putString(BaseFragment.KEY_TITLE, title);
        fragment.setArguments(bundle);
        if (fragment.isClearTop()) {
            setOnlyFragmentToStack(getSupportFragmentManager(), fragment);
        } else {
            addFragmentToStack(getSupportFragmentManager(), fragment);
        }
    }

    @Override
    public void onLoginStatusChanged(boolean isLogin) {
        if (isLogin) {
            initLoginInUserInfo();
        } else {
            AuthInfoManager.getInstance().clearAuthInfo();
            mAvatarImageView.setImageResource(R.drawable.m_default);
            mUsernameTextView.setText(R.string.not_logged_in);
            mLogoutImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void startLoading() {
        if (mProgressBar.getVisibility() != View.VISIBLE) {
            mProgressBar.setActivated(true);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void stopLoading() {
        if (mProgressBar.getVisibility() != View.GONE) {
            mProgressBar.setActivated(false);
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
