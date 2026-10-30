package org.mazhuang.guanggoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;
import org.mazhuang.guanggoo.data.entity.UploadImageResponse;

/**
 * 校验路过图床（Chevereto）上传接口返回的解析。
 */
public class UploadImageResponseTest {

    @Test
    public void parseCheveretoResponse() {
        String json = "{"
                + "\"status_code\":200,"
                + "\"status_txt\":\"OK\","
                + "\"data\":{"
                + "  \"id\":\"AbCdE\","
                + "  \"url\":\"https://imgchr.com/i/AbCdE\","
                + "  \"display_url\":\"https://imgchr.com/i/AbCdE\","
                + "  \"image\":{\"url\":\"https://s1.ax1x.com/2024/01/01/AbCdE.jpg\"},"
                + "  \"width\":100,"
                + "  \"height\":100"
                + "}}";

        UploadImageResponse response = new Gson().fromJson(json, UploadImageResponse.class);

        assertTrue(response.isSuccess());
        assertEquals("https://s1.ax1x.com/2024/01/01/AbCdE.jpg", response.getDirectImageUrl());
    }

    @Test
    public void parseLegacyImageResponse() {
        String json = "{"
                + "\"status_code\":200,"
                + "\"success\":{\"message\":\"image uploaded\",\"code\":200},"
                + "\"image\":{\"url\":\"https://example.com/images/2024/01/01/example.jpg\","
                + "\"url_viewer\":\"https://example.com/image/L\"}"
                + "}";

        UploadImageResponse response = new Gson().fromJson(json, UploadImageResponse.class);

        assertTrue(response.isSuccess());
        assertEquals("https://example.com/images/2024/01/01/example.jpg",
                response.getDirectImageUrl());
    }

    @Test
    public void parseErrorResponse() {
        String json = "{"
                + "\"status_code\":400,"
                + "\"status_txt\":\"Bad Request\","
                + "\"error\":{\"message\":\"Invalid API v1 key.\",\"code\":100,\"context\":\"Exception\"}"
                + "}";

        UploadImageResponse response = new Gson().fromJson(json, UploadImageResponse.class);

        assertFalse(response.isSuccess());
        assertEquals("Invalid API v1 key.", response.getErrorMessage());
    }
}
