package org.mazhuang.guanggoo.topicdetail.viewimage;

import android.graphics.Bitmap;

import com.bm.library.PhotoView;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import org.mazhuang.guanggoo.util.FileUtils;

/**
 *
 * @author mazhuang
 * @date 2017/11/18
 */

public class ViewImagePresenter implements ViewImageContract.Presenter {

    private ViewImageContract.View mView;

    public ViewImagePresenter(ViewImageContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void saveImage(final PhotoView photoViewTemp) {
        if (photoViewTemp != null) {
            GlideBitmapDrawable glideBitmapDrawable = (GlideBitmapDrawable) photoViewTemp.getDrawable();
            if (glideBitmapDrawable == null) {
                return;
            }
            Bitmap bitmap = glideBitmapDrawable.getBitmap();
            if (bitmap == null) {
                return;
            }
            mView.startLoading();
            FileUtils.saveImage(photoViewTemp.getContext(), bitmap, new FileUtils.SaveResultCallback() {
                @Override
                public void onSavedSuccess() {
                    photoViewTemp.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.stopLoading();
                            mView.onSaveImageSucceed();
                        }
                    });
                }

                @Override
                public void onSavedFailed() {
                    photoViewTemp.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.stopLoading();
                            mView.onSaveImageFailed("保存失败");
                        }
                    });
                }
            });
        }
    }
}
