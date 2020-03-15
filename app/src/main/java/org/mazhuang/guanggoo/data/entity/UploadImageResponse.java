package org.mazhuang.guanggoo.data.entity;

import lombok.Data;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
@Data
public class UploadImageResponse {
    public static final String CODE_SUCCESS = "200";

    private String code;
    private String msg;
    private Content data;

    @Data
    public static class Content {
        private UrlList url;
    }

    @Data
    public static class UrlList {
        private String distribute;
        private String ali;
        private String juejin;
        private String huluxia;
        private String imgbb;
    }
}
