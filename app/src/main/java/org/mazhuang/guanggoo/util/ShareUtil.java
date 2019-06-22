package org.mazhuang.guanggoo.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.mazhuang.guanggoo.R;

/**
 * @author mazhuang
 * @date 2019-06-22
 */
public class ShareUtil {
    private ShareUtil() {}

    public static void shareLink(@NonNull Activity activity, String url) {
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(activity, R.string.cannot_share, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, url);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.share_to)));
        } else {
            Toast.makeText(activity, R.string.cannot_share, Toast.LENGTH_SHORT).show();
        }
    }
}
