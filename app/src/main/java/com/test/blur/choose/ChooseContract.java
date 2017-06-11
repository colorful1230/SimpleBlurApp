package com.test.blur.choose;

import com.test.blur.BasePresenter;
import com.test.blur.BaseView;

/**
 * Created by zhaolin on 17-6-11.
 */

public interface ChooseContract {

    interface Presenter extends BasePresenter {

    }

    interface View extends BaseView<Presenter> {

    }
}
