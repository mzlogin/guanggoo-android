package org.mazhuang.guanggoo.util;

import org.mazhuang.guanggoo.App;
import org.mazhuang.guanggoo.R;

/**
 * @author mazhuang
 * @date 2018/12/23
 */
public class ConfigUtil {
    private ConfigUtil() {}

    public static boolean isDebug() {
        return "debug".equalsIgnoreCase(App.getInstance().getString(R.string.build_config_type));
    }
}
