package com.test.blur.choose;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.blur.R;
import com.test.blur.edit.EditActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.List;

import permissions.dispatcher.NeedsPermission;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhaolin on 17-6-11.
 */

public class ChooseFragment extends Fragment implements ChooseContract.View {

    private static final int REQUEST_CODE_CHOOSE = 23;

    private ChooseContract.Presenter mPresenter;

    public static ChooseFragment newInstance() {
        return new ChooseFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose, container, false);
        View content = rootView.findViewById(R.id.choose_container);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(getActivity())
                        .choose(MimeType.allOf())
                        .countable(true)
                        .maxSelectable(1)
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new PicassoEngine())
                        .forResult(REQUEST_CODE_CHOOSE);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void setPresenter(ChooseContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> selected = Matisse.obtainResult(data);
            if (selected != null && selected.size() > 0) {
                Uri uri = selected.get(0);
                Intent intent = new Intent(getActivity(), EditActivity.class);
                intent.putExtra("extra", uri);
                startActivity(intent);
            }
        }
    }
}
