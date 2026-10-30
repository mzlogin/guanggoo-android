package org.mazhuang.guanggoo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;
import org.mazhuang.guanggoo.data.entity.UpdateInfo;

/**
 * 校验升级检测接口的 JSON 契约。
 */
public class UpdateInfoTest {

    @Test
    public void parseFullUpdateInfo() {
        String json = "{\n"
                + "  \"versionCode\": 181,\n"
                + "  \"versionName\": \"2.3.181\",\n"
                + "  \"downloadUrl\": \"https://example.com/releases/guanggoo_github_v2.3.181_release.apk\",\n"
                + "  \"changeLog\": \"1. 修复若干已知问题\\n2. 优化页面加载速度\",\n"
                + "  \"md5\": \"3f2c1a9e4b6d8c0e2f4a6b8c0d2e4f6a\",\n"
                + "  \"forceUpdate\": true\n"
                + "}";

        UpdateInfo info = new Gson().fromJson(json, UpdateInfo.class);

        assertEquals(181, info.versionCode);
        assertEquals("2.3.181", info.versionName);
        assertEquals("https://example.com/releases/guanggoo_github_v2.3.181_release.apk",
                info.downloadUrl);
        assertEquals("1. 修复若干已知问题\n2. 优化页面加载速度", info.changeLog);
        assertEquals("3f2c1a9e4b6d8c0e2f4a6b8c0d2e4f6a", info.md5);
        assertTrue(info.forceUpdate);
    }

    @Test
    public void parseMinimalUpdateInfo() {
        String json = "{\n"
                + "  \"versionCode\": 182,\n"
                + "  \"downloadUrl\": \"https://example.com/releases/app.apk\"\n"
                + "}";

        UpdateInfo info = new Gson().fromJson(json, UpdateInfo.class);

        assertEquals(182, info.versionCode);
        assertEquals("https://example.com/releases/app.apk", info.downloadUrl);
        assertNull(info.versionName);
        assertNull(info.changeLog);
        assertNull(info.md5);
        assertFalse(info.forceUpdate);
    }
}
