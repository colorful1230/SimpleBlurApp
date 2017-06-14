package com.test.blur.edit;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.test.blur.data.ImageInfo;
import com.test.blur.utils.FileUtils;

import permissions.dispatcher.NeedsPermission;

/**
 * Created by zhaolin on 17-6-11.
 */

public class EditPresenter implements EditContract.Presenter {

    private EditContract.View mView;

    private boolean mIsEdit = false;

    public EditPresenter(EditContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void setSeekProgress(int progress) {
        mView.setBlurRadius(progress);
        mIsEdit = progress > 1;
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @Override
    public void saveImage() {
        if (mIsEdit) {
            mView.showSaveTip();
            new SaveImageTask().execute();
        } else {
            mView.showNotEditTip();
        }
    }

    @Override
    public void showImage(ImageInfo imageInfo) {
        mView.showImage(imageInfo.getUri());
    }

    private class SaveImageTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (mView == null) {
                return null;
            }

            Bitmap bitmap = mView.getBlurImage();
            if (bitmap == null) {
                return null;
            }

            String path = FileUtils.saveBitmap(bitmap);
            if (TextUtils.isEmpty(path)) {
                return null;
            }
            return path;

        }

        @Override
        protected void onPostExecute(String s) {
            if (mView != null) {
                mView.showSaveSuccess(s);
            }
        }
    }
}
