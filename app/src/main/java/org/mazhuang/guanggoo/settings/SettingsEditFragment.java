package org.mazhuang.guanggoo.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.util.PrefsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author mazhuang
 */
public class SettingsEditFragment extends BaseFragment {

    public static final String SETTINGS_KEY = "settings_key";
    public static final String SETTINGS_HINT = "settings_hint";

    private String mSettingsKey;
    private String mSettingsHint;

    @BindView(R.id.settings_value) EditText mSettingsValueEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings_edit, container, false);

        ButterKnife.bind(this, root);

        initViews();

        return root;
    }

    @Override
    public void initParams() {
        super.initParams();
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSettingsKey = bundle.getString(SETTINGS_KEY);
            mSettingsHint = bundle.getString(SETTINGS_HINT);
        }
    }

    private void initViews() {
        mSettingsValueEditText.setHint(mSettingsHint);

        String value = PrefsUtil.getString(App.getInstance(), mSettingsKey, "");
        mSettingsValueEditText.setText(value);
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                PrefsUtil.putString(App.getInstance(), mSettingsKey, mSettingsValueEditText.getText().toString());
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
