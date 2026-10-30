package org.mazhuang.guanggoo.home;


import android.app.Activity;
import androidx.lifecycle.Observer;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.router.annotations.ClearTop;
import org.mazhuang.guanggoo.router.annotations.StartsWithAppBar;
import org.mazhuang.guanggoo.ui.widget.MaterialBadgeTextView;
import org.mazhuang.guanggoo.ui.widget.MenuItemBadge;
import org.mazhuang.guanggoo.util.ConstantUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author mazhuang
 */
@ClearTop
@StartsWithAppBar
public class HomeFragment extends BaseFragment {

    @BindView(R.id.tab) TabLayout mTabLayout;
    @BindView(R.id.pager) ViewPager mViewPager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);
        
        initViews();

        return root;
    }

    private void initViews() {
        HomePagerAdapter adapter = new HomePagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(adapter.getCount());
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home, menu);

        final Activity activity = getActivity();
        if (activity == null) {
            super.onCreateOptionsMenu(menu, inflater);
            return;
        }

        final MenuItem menuItemNotification = menu.findItem(R.id.action_notifications);
        MenuItemBadge.update(activity, menuItemNotification, new MenuItemBadge.Builder()
                .iconDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_menu_notifications))
                .iconTintColor(Color.WHITE));

        App.getInstance().mGlobal.hasNotifications.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                MaterialBadgeTextView badgeTextView = MenuItemBadge.getBadgeTextView(menuItemNotification);
                if (badgeTextView == null) {
                    return;
                }
                if (aBoolean) {
                    badgeTextView.setHighLightMode(true);
                } else {
                    badgeTextView.clearHighLightMode();
                }
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notifications:
                mListener.openPage(ConstantUtil.NOTIFICATIONS_URL, null);
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
