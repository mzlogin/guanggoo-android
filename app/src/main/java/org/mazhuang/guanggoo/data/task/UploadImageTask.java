package org.mazhuang.guanggoo.data.task;

import android.text.TextUtils;

import com.google.gson.Gson;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.mazhuang.guanggoo.data.OnResponseListener;
import org.mazhuang.guanggoo.data.entity.UploadImageResponse;
import org.mazhuang.guanggoo.util.ConstantUtil;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
public class UploadImageTask extends BaseTask<String> {

    public static final String URL = "https://sm.ms/api/upload";
    public static final String KEY = "smfile";

    private InputStream mStream;

    public UploadImageTask(InputStream stream, OnResponseListener<String> listener) {
        super(listener);
        mStream = stream;
    }

    @Override
    public void run() {
        String errorMsg;
        try {
            Connection.Response response = doPostFileRequest(URL, KEY, mStream);
            if (response != null && response.statusCode() == ConstantUtil.HTTP_STATUS_200) {
                Gson gson = new Gson();
                UploadImageResponse entity = gson.fromJson(response.body(), UploadImageResponse.class);
                if (UploadImageResponse.CODE_SUCCESS.equals(entity.getCode())) {
                    if (entity.getData() != null && !TextUtils.isEmpty(entity.getData().getUrl())) {
                        successOnUI(entity.getData().getUrl());
                        return;
                    }
                    errorMsg = "图片上传返回数据有误";
                } else {
                    errorMsg = entity.getMsg();
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
        return Jsoup.connect(url)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .timeout(12000)
                .data(key, "filename", inputStream)
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
