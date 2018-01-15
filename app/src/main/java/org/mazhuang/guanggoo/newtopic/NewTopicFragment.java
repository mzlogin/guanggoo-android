package org.mazhuang.guanggoo.newtopic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author mazhuang
 * @date 2017/10/10
 */

public class NewTopicFragment extends BaseFragment<NewTopicContract.Presenter> implements NewTopicContract.View {

    @BindView(R.id.title) EditText mTitleEditText;
    @BindView(R.id.content) EditText mContentEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_new_topic, container, false);

        ButterKnife.bind(this, root);

        initParams();

        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.new_topic, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                attemptNewTopic();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptNewTopic() {
        mTitleEditText.setError(null);
        mContentEditText.setError(null);

        String title = mTitleEditText.getText().toString();
        String content = mContentEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            mTitleEditText.setError(getString(R.string.error_field_required));
            focusView = mTitleEditText;
            cancel = true;
        } else if (!isTitleValid(title)) {
            mTitleEditText.setError(getString(R.string.error_invalid_title));
            focusView = mTitleEditText;
            cancel = true;
        } else if (TextUtils.isEmpty(content)) {
            mContentEditText.setError(getString(R.string.error_field_required));
            focusView = mContentEditText;
            cancel = true;
        } else if (!isContentValid(content)) {
            mTitleEditText.setError(getString(R.string.error_invalid_content));
            focusView = mContentEditText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            mPresenter.newTopic(title, content);
        }
    }

    private boolean isContentValid(String content) {
        return (!TextUtils.isEmpty(content) && content.length() >= 3);
    }

    private boolean isTitleValid(String title) {
        return (!TextUtils.isEmpty(title) && title.length() >= 3);
    }

    @Override
    public void onNewTopicSucceed() {
        if (getContext() == null) {
            return;
        }

        getActivity().onBackPressed();
    }

    @Override
    public void onNewTopicFailed(String msg) {
        toast(msg);
    }

    @Override
    public String getTitle() {
        if (!TextUtils.isEmpty(mTitle)) {
            return mTitle;
        } else {
            return getString(R.string.new_topic);
        }
    }
}
