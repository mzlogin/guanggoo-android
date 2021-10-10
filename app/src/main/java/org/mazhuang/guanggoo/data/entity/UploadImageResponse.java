package org.mazhuang.guanggoo.data.entity;

import lombok.Data;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
@Data
public class UploadImageResponse {
    public static final String CODE_SUCCESS = "200";

    private String status;
    private boolean success;
    private Image data;

    @Data
    public static class Image {
        private String url;
    }
}
