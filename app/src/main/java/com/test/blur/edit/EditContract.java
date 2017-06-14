package com.test.blur.edit;

import android.graphics.Bitmap;
import android.net.Uri;

import com.test.blur.BasePresenter;
import com.test.blur.BaseView;
import com.test.blur.data.ImageInfo;

/**
 * Created by zhaolin on 17-6-11.
 */

public interface EditContract {

    interface Presenter extends BasePresenter {

        void setSeekProgress(int progress);

        void saveImage();

        void showImage(ImageInfo imageInfo);

    }

    interface View extends BaseView<Presenter> {

        void showImage(Uri uri);

        void setBlurRadius(int radius);

        Bitmap getBlurImage();

        void showSaveTip();

        void showSaveSuccess(String path);

        void showNotEditTip();
    }
}
