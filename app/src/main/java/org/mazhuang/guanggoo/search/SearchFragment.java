package org.mazhuang.guanggoo.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;
import org.mazhuang.guanggoo.data.entity.UserProfile;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.UrlUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 统一搜索页：搜索帖子与搜索用户。
 *
 * @author mazhuang
 */
public class SearchFragment extends BaseFragment<SearchContract.Presenter> implements SearchContract.View {

    /**
     * 与布局中的单选按钮一一对应。
     */
    private static final int[] SCOPE_BUTTON_IDS = {
            R.id.scope_all, R.id.scope_title, R.id.scope_topic, R.id.scope_reply
    };
    private static final String[] SEARCH_SCOPES = {"all", "title", "topic", "reply"};

    @BindView(R.id.keyword) EditText mKeywordEditText;
    @BindView(R.id.scope_group) RadioGroup mScopeGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        ButterKnife.bind(this, root);

        if (mPresenter == null) {
            mPresenter = new SearchPresenter(this);
        }

        initParams();

        initViews();

        return root;
    }

    private void initViews() {
        mKeywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchTopics();
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick(R.id.search_topics)
    public void onSearchTopics() {
        String keyword = getKeyword();
        if (TextUtils.isEmpty(keyword)) {
            toast(getString(R.string.please_input_search_keyword));
            return;
        }
        if (mListener == null) {
            return;
        }

        String url = ConstantUtil.SEARCH_RESULT_URL
                + "?q=" + UrlUtil.urlEncode(keyword)
                + "&scope=" + getSelectedScope();
        mListener.openPage(url, getString(R.string.search_results));
    }

    @OnClick(R.id.search_user)
    public void onSearchUser() {
        String keyword = getKeyword();
        if (TextUtils.isEmpty(keyword)) {
            toast(getString(R.string.please_input_user_id));
            return;
        }

        String url = String.format(ConstantUtil.USER_PROFILE_BASE_URL, keyword);
        mPresenter.getUserProfile(url);
    }

    private String getSelectedScope() {
        int checkedId = mScopeGroup.getCheckedRadioButtonId();
        for (int i = 0; i < SCOPE_BUTTON_IDS.length; i++) {
            if (SCOPE_BUTTON_IDS[i] == checkedId) {
                return SEARCH_SCOPES[i];
            }
        }
        return SEARCH_SCOPES[0];
    }

    private String getKeyword() {
        Editable rawKeyword = mKeywordEditText.getText();
        return rawKeyword == null ? "" : rawKeyword.toString().trim();
    }

    @Override
    public void onGetUserProfileSucceed(UserProfile userProfile) {
        if (getContext() == null || mListener == null) {
            return;
        }
        mListener.openPage(userProfile.getUrl(), null);
    }

    @Override
    public void onGetUserProfileFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        }
        return getString(R.string.search);
    }
}
