package org.mazhuang.guanggoo.data.task;

import android.text.TextUtils;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UploadImageResponse;
import org.mazhuang.guanggoo.util.ConstantUtil;
import org.mazhuang.guanggoo.util.PrefsUtil;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
public class UploadImageTask extends BaseTask<String> {

    public static final String UPLOAD_URL = "https://api.imgbb.com/1/upload?key=";
    public static final String KEY = "image";

    private InputStream mStream;

    public UploadImageTask(InputStream stream, OnResponseListener<String> listener) {
        super(listener);
        mStream = stream;
    }

    @Override
    public void run() {
        String errorMsg;

        String apiKey = PrefsUtil.getString(App.getInstance(), ConstantUtil.KEY_IMGBB_API_KEY, "");
        if (TextUtils.isEmpty(apiKey)) {
            errorMsg = "请先到设置里添加 IMGBB 图床 API KEY";
            failedOnUI(errorMsg);
            return;
        }

        try {
            Connection.Response response = doPostFileRequest(UPLOAD_URL + apiKey, KEY, mStream);
            if (response != null && response.statusCode() == ConstantUtil.HTTP_STATUS_200) {
                Gson gson = new Gson();
                UploadImageResponse entity = gson.fromJson(response.body(), UploadImageResponse.class);
                if (UploadImageResponse.CODE_SUCCESS.equals(entity.getStatus()) && entity.isSuccess()) {
                    if (entity.getData() != null && !TextUtils.isEmpty(entity.getData().getUrl())) {
                        successOnUI(entity.getData().getUrl());
                        return;
                    }
                    errorMsg = "图片上传返回数据有误";
                } else {
                    errorMsg = "图片上传失败：" + response.body();
                }
            } else {
                errorMsg = "图片上传失败";
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorMsg = e.getMessage();
        }
        failedOnUI(errorMsg);
    }

    public static Connection.Response doPostFileRequest(String url, String key, InputStream inputStream) throws IOException {
        // Https请求
        if (url.startsWith("https")) {
            trustEveryone();
        }

        String filename = UUID.randomUUID() + ".jpg";

        return Jsoup.connect(url)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .timeout(120000)
                .data(key, filename, inputStream)
                .execute();
    }


    /**
     * 解决Https请求,返回404错误
     */
    public static void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
