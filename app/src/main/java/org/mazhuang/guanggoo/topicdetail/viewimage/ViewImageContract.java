package org.mazhuang.guanggoo.topicdetail.viewimage;

import com.bm.library.PhotoView;

import org.mazhuang.guanggoo.base.BasePresenter;
import org.mazhuang.guanggoo.base.BaseView;

/**
 * Created by mazhuang on 2017/11/18.
 */

public interface ViewImageContract {
    interface Presenter extends BasePresenter {
        void saveImage(PhotoView photoViewTemp);
    }

    interface View extends BaseView<Presenter> {
        void onSaveImageSucceed();
        void onSaveImageFailed(String msg);
    }
}
