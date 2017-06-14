package com.test.blur.edit;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.test.blur.R;

/**
 * Created by zhaolin on 17-6-11.
 */

public class EditFragment extends Fragment implements EditContract.View, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "EditFragment";

    private static final int MIN_BLUR_RADIUS = 1;

    private static final int MAX_BLUR_RADIUS = 24;

    private ImageView mEditImageView;

    private EditContract.Presenter mPresenter;

    private Bitmap mScaleBitmap;

    private Bitmap mBlurBitmap;

    private float mScale = 1f;

    private int mCurrentBlurLevel = 0;

    private Toast mSavingToast;

    private SeekBar mEditSeekBar;

    private TextView mTipTextView;

    public static EditFragment newInstance() {
        return new EditFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragmment_edit, container, false);
        mEditImageView = (ImageView) root.findViewById(R.id.edit_image_view);
        mEditSeekBar = (SeekBar) root.findViewById(R.id.edit_seek_bar);
        mEditSeekBar.setMax(MAX_BLUR_RADIUS);
        mEditSeekBar.setOnSeekBarChangeListener(this);
        mTipTextView = (TextView) root.findViewById(R.id.edit_tip_text);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentBlurLevel > 1) {
            setBlurRadius(mCurrentBlurLevel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(EditContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showImage(Uri uri) {
        mEditImageView.setVisibility(View.VISIBLE);
        mEditSeekBar.setVisibility(View.VISIBLE);
        mTipTextView.setVisibility(View.VISIBLE);
        mEditImageView.setImageURI(uri);
        BitmapDrawable drawable = (BitmapDrawable) mEditImageView.getDrawable();
        Bitmap originalBitmap = drawable.getBitmap();
        getScale(originalBitmap);

        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        mScaleBitmap = ThumbnailUtils.extractThumbnail(originalBitmap, (int)(originalWidth * mScale),
                (int)(originalHeight * mScale));
        mBlurBitmap = Bitmap.createBitmap(mScaleBitmap.getWidth(), mScaleBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
    }

    @Override
    public void setBlurRadius(int radius) {
        blurImage(mScaleBitmap, radius);
        mEditImageView.setImageBitmap(mBlurBitmap);
    }

    @Nullable
    @Override
    public Bitmap getBlurImage() {
        return mBlurBitmap;
    }

    @Override
    public void showSaveTip() {
        if (mSavingToast == null) {
            mSavingToast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.edit_saving),
                    Toast.LENGTH_SHORT);
        }
        mSavingToast.show();
    }

    @Override
    public void showSaveSuccess(String path) {
        if (mSavingToast != null) {
            mSavingToast.cancel();
        }
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.edit_saved) + path,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotEditTip() {
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.edit_not_edit),
                Toast.LENGTH_SHORT).show();
    }

    private void blurImage(Bitmap bitmap, int radius) {
        long start = System.currentTimeMillis();
        RenderScript rs = RenderScript.create(getActivity());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, mBlurBitmap);
        script.setRadius(radius);
        script.setInput(allIn);
        script.forEach(allOut);
        allOut.copyTo(mBlurBitmap);
        rs.destroy();
        long end = System.currentTimeMillis();
        Log.d(TAG, "blurImage: " + (end - start));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mCurrentBlurLevel = progress + MIN_BLUR_RADIUS;
        mPresenter.setSeekProgress(mCurrentBlurLevel);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void getScale(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int[] size = getScreenSize();
        if (height <= size[1]) {
            return;
        }
        mScale = size[1] * 1.0f / height;
    }

    private int[] getScreenSize() {
        int[] size = new int[2];

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        size[0] = metrics.widthPixels;
        size[1] = metrics.heightPixels;

        return size;
    }
}
