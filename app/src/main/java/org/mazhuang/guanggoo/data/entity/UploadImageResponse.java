package org.mazhuang.guanggoo.data.entity;

import lombok.Data;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
@Data
public class UploadImageResponse {
    public static final String CODE_SUCCESS = "success";

    private String code;
    private String msg;
    private Content data;

    @Data
    public static class Content {
        private int width;
        private int height;
        private long size;
        private String url;
        private String delete;
    }
}
