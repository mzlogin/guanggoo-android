package org.mazhuang.guanggoo.data.task;

import android.text.TextUtils;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.imgchr.ImgchrAccount;
import org.mazhuang.guanggoo.imgchr.ImgchrClient;

import java.io.InputStream;

/**
 * 通过路过图床（imgchr.com）上传图片。
 *
 * @author mazhuang
 * @date 2019-05-12
 */
public class UploadImageTask extends BaseTask<String> {

    private final InputStream mStream;

    public UploadImageTask(InputStream stream, OnResponseListener<String> listener) {
        super(listener);
        mStream = stream;
    }

    @Override
    public void run() {
        String errorMsg;

        String cookie = ImgchrAccount.getCookie(App.getInstance());
        try {
            ImgchrClient.UploadResult result = ImgchrClient.upload(cookie, mStream);
            if (!TextUtils.isEmpty(cookie) && !TextUtils.isEmpty(result.getCookie())) {
                ImgchrAccount.saveCookie(App.getInstance(), result.getCookie());
            }
            successOnUI(result.getUrl());
            return;
        } catch (ImgchrClient.SessionExpiredException e) {
            ImgchrAccount.clear(App.getInstance());
            errorMsg = errorMessage(e, "图床登录已失效，请重新登录");
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = errorMessage(e, "图片上传失败");
        }

        failedOnUI(errorMsg);
    }
}
