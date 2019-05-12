package org.mazhuang.guanggoo.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
public abstract class BaseUploadImageFragment<T> extends BaseFragment<T> {
    protected void chooseImage() {
        CharSequence[] items = {"从相册选择", "拍照", "取消"};
        new AlertDialog.Builder(getActivity())
                .setTitle("选择图片来源")
                .setItems(items, (dialog, which) -> {
                    if ( which == 0 ){
                        try {
                            //选择照片的时候也一样，我们用Action为Intent.ACTION_GET_CONTENT，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, which);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            toast(e.getMessage());
                        }
                    } else if (which == 1) {
                        try {
                            //拍照我们用Action为MediaStore.ACTION_IMAGE_CAPTURE，
                            //有些人使用其他的Action但我发现在有些机子中会出问题，所以优先选择这个
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, which);
                        } catch (Exception e) {
                            e.printStackTrace();
                            toast(e.getMessage());
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (requestCode == 0) {
                // 从相册选择
                ContentResolver resolver = getActivity().getContentResolver();
                // 照片的原始资源地址
                Uri originalUri = data.getData();
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(resolver,
                            originalUri);
                    if (image != null) {
                        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        doUploadImage(is);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 1) {
                // 拍照后
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                    Bitmap image = extras.getParcelable("data");
                    if (image != null) {
                        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                        InputStream is = new ByteArrayInputStream(baos.toByteArray());
                        doUploadImage(is);
                    }
                }
            }
        }
    }

    /**
     * 实际执行图片上传的动作
     * @param inputStream 选中图片的流
     */
    public abstract void doUploadImage(InputStream inputStream);
}
