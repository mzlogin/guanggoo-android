package org.mazhuang.guanggoo.util;

import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * @author mazhuang
 * @date 2017/12/24
 */

public class DimensUtil {
    public static float getDensity(WindowManager wm) {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }
}
