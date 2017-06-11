package com.test.blur.choose;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.test.blur.R;
import com.test.blur.utils.ActivityUtils;

/**
 * Created by zhaolin on 17-6-11.
 */

public class MainActivity extends AppCompatActivity {

    private ChooseFragment chooseFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_tool_bar);
        setSupportActionBar(toolbar);

        chooseFragment = (ChooseFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_content_frame);
        if (chooseFragment == null) {
            chooseFragment = ChooseFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), chooseFragment,
                    R.id.main_content_frame);
        }

        new ChoosePresenter(chooseFragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        chooseFragment.onActivityResult(requestCode, resultCode, data);
    }
}
