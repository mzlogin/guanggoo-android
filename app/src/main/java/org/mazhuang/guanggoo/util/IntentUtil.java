package org.mazhuang.guanggoo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import org.mazhuang.guanggoo.R;

/**
 * 跳转外部应用（如浏览器）的封装，避免设备上没有可处理的应用时崩溃。
 *
 * @author mazhuang
 */
public class IntentUtil {

    private IntentUtil() {
    }

    /**
     * 用系统默认应用打开链接。
     *
     * @param context 上下文
     * @param url     链接地址
     * @return 是否成功唤起
     */
    public static boolean openUrl(Context context, String url) {
        if (context == null || TextUtils.isEmpty(url)) {
            return false;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Toast.makeText(context.getApplicationContext(),
                        R.string.error_happened, Toast.LENGTH_SHORT).show();
            } catch (Exception ignored) {
                // 连提示都失败时只能放弃
            }
            return false;
        }
    }
}
