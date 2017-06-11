package com.test.blur.edit;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
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

    private SeekBar mEditSeekBar;

    private EditContract.Presenter mPresenter;

    private Bitmap mOriginalBitmap;

    private Bitmap mBlurBitmap;

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
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.start();
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
        mEditImageView.setImageURI(uri);
        BitmapDrawable drawable = (BitmapDrawable) mEditImageView.getDrawable();
        mOriginalBitmap = drawable.getBitmap();
    }

    @Override
    public void setBlurRadius(int radius) {
        mBlurBitmap = blurImage(mOriginalBitmap, radius);
        mEditImageView.setImageBitmap(mBlurBitmap);
    }

    @Nullable
    @Override
    public Bitmap getBlurImage() {
        return mBlurBitmap;
    }

    @Override
    public void showSaveSuccess(String path) {
        Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
    }

    private Bitmap blurImage(Bitmap bitmap, int radius) {
        Bitmap output = bitmap.copy(bitmap.getConfig(), true);
        RenderScript rs = RenderScript.create(getActivity());
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, output);
        script.setRadius(radius);
        script.setInput(allIn);
        script.forEach(allOut);
        allOut.copyTo(output);
        rs.destroy();
        return output;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mPresenter.setSeekProgress(progress + MIN_BLUR_RADIUS);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
