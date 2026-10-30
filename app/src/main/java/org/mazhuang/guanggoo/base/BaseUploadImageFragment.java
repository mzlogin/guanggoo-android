package org.mazhuang.guanggoo.base;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.imgchr.ImgchrAccount;
import org.mazhuang.guanggoo.imgchr.ImgchrLoginActivity;
import org.mazhuang.guanggoo.util.DialogUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author mazhuang
 * @date 2019-05-12
 */
public abstract class BaseUploadImageFragment<T> extends BaseFragment<T> {

    private static final int REQUEST_CODE_LOGIN = 300;

    private byte[] mPendingImageData;

    protected void chooseImage() {
        CharSequence[] items = {"从相册选择", "拍照", "取消"};
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        DialogUtil.builder(activity)
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
                            toast("没有找到可用的相册应用");
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
                            toast("没有找到可用的相机应用");
                        }
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOGIN) {
            if (resultCode == Activity.RESULT_OK && mPendingImageData != null) {
                doUploadImage(new ByteArrayInputStream(mPendingImageData));
            }
            mPendingImageData = null;
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (requestCode == 0) {
                // 从相册选择
                Activity activity = getActivity();
                if (activity == null || data == null) {
                    return;
                }
                ContentResolver resolver = activity.getContentResolver();
                // 照片的原始资源地址
                Uri originalUri = data.getData();
                try {
                    Bitmap image = originalUri == null ? null
                            : MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    if (image != null) {
                        image.compress(Bitmap.CompressFormat.JPEG, 30, baos);
                        prepareUpload(baos.toByteArray());
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
                        prepareUpload(baos.toByteArray());
                    }
                }
            }
        }
    }

    private void prepareUpload(final byte[] imageData) {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        if (ImgchrAccount.isLoggedIn(activity)) {
            doUploadImage(new ByteArrayInputStream(imageData));
            return;
        }

        mPendingImageData = imageData;
        DialogUtil.builder(activity)
                .setTitle(R.string.imgchr_login_prompt_title)
                .setMessage(R.string.imgchr_login_prompt_message)
                .setPositiveButton(R.string.go_login, (dialog, which) -> {
                    if (getActivity() != null) {
                        startActivityForResult(
                                new Intent(getActivity(), ImgchrLoginActivity.class),
                                REQUEST_CODE_LOGIN);
                    }
                })
                .setNegativeButton(R.string.upload_without_login, (dialog, which) -> {
                    mPendingImageData = null;
                    doUploadImage(new ByteArrayInputStream(imageData));
                })
                .setOnCancelListener(dialog -> mPendingImageData = null)
                .show();
    }

    /**
     * 实际执行图片上传的动作
     * @param inputStream 选中图片的流
     */
    public abstract void doUploadImage(InputStream inputStream);
}
