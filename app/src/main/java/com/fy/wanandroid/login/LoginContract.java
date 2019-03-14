package com.fy.wanandroid.login;

import com.fy.baselibrary.base.mvp.IView;
import com.fy.wanandroid.entity.LoginBean;

/**
 * describe： todo 描述</br>
 * Created by fangs on 2019/1/23 17:32.
 */
public interface LoginContract {

    interface LoginView extends IView {
        void loginSuccess(LoginBean login);
    }

}
