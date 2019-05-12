package org.mazhuang.guanggoo.newtopic;

import com.vdurmont.emoji.EmojiParser;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.data.NetworkTaskScheduler;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.task.NewTopicTask;
import org.mazhuang.guanggoo.data.task.UploadImageTask;

import java.io.InputStream;

/**
 *
 * @author mazhuang
 * @date 2017/10/10
 */

public class NewTopicPresenter implements NewTopicContract.Presenter {

    private NewTopicContract.View mView;

    public NewTopicPresenter(NewTopicContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void newTopic(String title, String content) {
        mView.startLoading();

        title = EmojiParser.parseToAliases(title);
        content = EmojiParser.parseToAliases(content);

        NetworkTaskScheduler.getInstance().execute(new NewTopicTask(mView.getUrl(),
                title,
                content,
                new OnResponseListener<String>() {
                    @Override
                    public void onSucceed(String data) {
                        mView.stopLoading();
                        mView.onNewTopicSucceed();
                    }

                    @Override
                    public void onFailed(String msg) {
                        mView.stopLoading();
                        mView.onNewTopicFailed(msg);
                    }
        }));
    }

    @Override
    public void uploadImage(InputStream inputStream) {
        mView.startLoading();

        NetworkTaskScheduler.getInstance().execute(new UploadImageTask(inputStream, new OnResponseListener<String>() {
            @Override
            public void onSucceed(String url) {
                mView.stopLoading();
                mView.onUploadImageSucceed(url);
            }

            @Override
            public void onFailed(String msg) {
                mView.stopLoading();
                mView.onUploadImageFailed(msg);
            }
        }));
    }
}
