package org.mazhuang.guanggoo.topicdetail.viewimage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.chrisbanes.photoview.PhotoView;
import org.mazhuang.guanggoo.GlideApp;
import org.mazhuang.guanggoo.R;
import org.mazhuang.guanggoo.base.BaseFragment;

/**
 *
 * @author mazhuang
 * @date 2017/11/18
 */

public class ViewImageFragment extends BaseFragment<ViewImageContract.Presenter> implements ViewImageContract.View {

    @BindView(R.id.image) protected PhotoView mImageView;

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initParams();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_view_image, container, false);

        ButterKnife.bind(this, root);

        if (mPresenter == null) {
            mPresenter = new ViewImagePresenter(this);
        }

        initPhotoView();

        loadImage();

        return root;
    }

    private void initPhotoView() {
    }

    protected void loadImage() {
        GlideApp.with(this)
                .load(mUrl)
                .into(mImageView);
    }

    @Override
    public String getTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return getString(R.string.view_image);
        } else {
            return mTitle;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_image, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                boolean isAllPermissionsGranted = true;
                for (String permission : PERMISSIONS) {
                    if (ActivityCompat.checkSelfPermission(getContext().getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                        isAllPermissionsGranted = false;
                        break;
                    }
                }

                if (!isAllPermissionsGranted) {
                    requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
                } else {
                    mPresenter.saveImage(mImageView);
                }

                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveImageSucceed() {
        if (getContext() == null) {
            return;
        }

        toast("保存成功");
    }

    @Override
    public void onSaveImageFailed(String msg) {
        if (getContext() == null) {
            return;
        }

        toast(msg);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            boolean isAllPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        isAllPermissionsGranted = false;
                        break;
                    }
                }
            } else {
                isAllPermissionsGranted = false;
            }

            if (isAllPermissionsGranted) {
                mPresenter.saveImage(mImageView);
                return;
            }

            toast("保存图片需要开启读写文件权限，请检查权限设置");

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
