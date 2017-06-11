package com.test.blur.choose;

/**
 * Created by zhaolin on 17-6-11.
 */

public class ChoosePresenter implements ChooseContract.Presenter {

    private ChooseContract.View mView;

    public ChoosePresenter(ChooseContract.View mView) {
        this.mView = mView;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
