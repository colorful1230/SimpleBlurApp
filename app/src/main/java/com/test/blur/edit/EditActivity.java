package com.test.blur.edit;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.test.blur.R;
import com.test.blur.data.ImageInfo;
import com.test.blur.utils.ActivityUtils;

public class EditActivity extends AppCompatActivity {

    private EditContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initToolbar();

        Intent intent = getIntent();
        Uri uri = null;
        if (intent != null) {
            uri = intent.getParcelableExtra("extra");
        }

        EditFragment editFragment = (EditFragment) getSupportFragmentManager()
                .findFragmentById(R.id.edit_content_frame);
        if (editFragment == null) {
            editFragment = EditFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), editFragment,
                    R.id.edit_content_frame);
        }

        ImageInfo imageInfo = new ImageInfo(uri);
        mPresenter = new EditPresenter(editFragment, imageInfo);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = getStatusBarHeight();
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        lp.topMargin = statusBarHeight;
        toolbar.setLayoutParams(lp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.edit_save_item:
                mPresenter.saveImage();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
           return getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}
