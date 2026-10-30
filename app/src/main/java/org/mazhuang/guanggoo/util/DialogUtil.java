package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.view.ContextThemeWrapper;

import androidx.appcompat.app.AlertDialog;

import org.mazhuang.guanggoo.R;

/**
 * 对话框工具。
 *
 * <p>应用主题的 colorAccent 为白色，直接使用会导按钮文字白底白字不可见，
 * 统一套用 AppTheme.AlertDialog（colorAccent 为蓝色）。</p>
 *
 * @author mazhuang
 */
public class DialogUtil {

    private DialogUtil() {
    }

    public static AlertDialog.Builder builder(Context context) {
        return new AlertDialog.Builder(
                new ContextThemeWrapper(context, R.style.AppTheme_AlertDialog));
    }
}
