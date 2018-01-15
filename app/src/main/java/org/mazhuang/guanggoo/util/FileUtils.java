package org.mazhuang.guanggoo.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author mazhuang
 * @date 2017/11/18
 * code from https://www.ctolib.com/topics-110262.html
 */

public class FileUtils {
    public static void saveImage(final Context context, final Bitmap bmp , final SaveResultCallback saveResultCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(Environment.getExternalStorageDirectory(), "out_photo");
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                //设置以当前时间格式为图片名称
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
                String fileName = df.format(new Date()) + ".png";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess();
                } catch (FileNotFoundException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                } catch (IOException e) {
                    saveResultCallback.onSavedFailed();
                    e.printStackTrace();
                }

                //保存图片后发送广播通知更新数据库
                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            }
        }).start();
    }

    public interface SaveResultCallback{
        /**
         * 保存成功
         */
        void onSavedSuccess();

        /**
         * 保存失败
         */
        void onSavedFailed();
    }
}
