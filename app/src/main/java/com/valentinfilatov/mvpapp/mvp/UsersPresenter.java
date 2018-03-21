package com.valentinfilatov.mvpapp.mvp;

import android.content.ContentValues;
import android.text.TextUtils;

import java.util.List;

import com.valentinfilatov.mvpapp.R;
import com.valentinfilatov.mvpapp.common.User;
import com.valentinfilatov.mvpapp.common.UserTable;

public class UsersPresenter {

    private UsersActivity view;
    private final UsersModel model;

    public UsersPresenter(UsersModel model) {
        this.model = model;
    }

    public void attachView(UsersActivity usersActivity) {
        view = usersActivity;
    }

    public void detachView() {
        view = null;
    }


    public void viewIsReady() {
        loadUsers();
    }

    public void loadUsers() {
        model.loadUsers(new UsersModel.LoadUserCallback() {
            @Override
            public void onLoad(List<User> users) {
                view.showUsers(users);
            }
        });
    }

    public void add() {
        PlayerData userData = view.getUserData();
        if (TextUtils.isEmpty(userData.getName()) || userData.getAge() == 0) {
            view.showToast(R.string.empty_values);
            return;
        }

        ContentValues cv = new ContentValues(2);
        cv.put(UserTable.COLUMN.NAME, userData.getName());
        cv.put(UserTable.COLUMN.AGE, userData.getAge());
        view.showProgress();
        model.addUser(cv, new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

    public void clear() {
        view.showProgress();
        model.clearUsers(new UsersModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadUsers();
            }
        });
    }

}
