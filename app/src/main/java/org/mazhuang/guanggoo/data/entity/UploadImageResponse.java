package org.mazhuang.guanggoo.data.entity;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * 路过图床（Chevereto）上传接口返回。
 *
 * @author mazhuang
 * @date 2019-05-12
 */
@Data
public class UploadImageResponse {

    @SerializedName("status_code")
    private int statusCode;

    @SerializedName("status_txt")
    private String statusTxt;

    private UploadData data;

    /**
     * 部分 Chevereto 版本直接返回 image 字段，做兼容处理。
     */
    private ImageInfo image;

    private ErrorInfo error;

    @Data
    public static class UploadData {
        private String id;
        private String url;
        @SerializedName("display_url")
        private String displayUrl;
        private ImageInfo image;
    }

    @Data
    public static class ImageInfo {
        private String url;
        @SerializedName("url_viewer")
        private String urlViewer;
    }

    @Data
    public static class ErrorInfo {
        private String message;
        private int code;
    }

    public boolean isSuccess() {
        return statusCode == 200 && error == null;
    }

    /**
     * 优先取图片直链，其次取展示地址、查看页地址。
     */
    public String getDirectImageUrl() {
        if (data != null) {
            if (data.image != null && !isEmpty(data.image.url)) {
                return data.image.url;
            }
            if (!isEmpty(data.displayUrl)) {
                return data.displayUrl;
            }
            if (!isEmpty(data.url)) {
                return data.url;
            }
        }
        if (image != null) {
            if (!isEmpty(image.url)) {
                return image.url;
            }
            if (!isEmpty(image.urlViewer)) {
                return image.urlViewer;
            }
        }
        return null;
    }

    public String getErrorMessage() {
        if (error != null && !isEmpty(error.message)) {
            return error.message;
        }
        return statusTxt;
    }

    private static boolean isEmpty(String value) {
        return value == null || value.length() == 0;
    }
}
