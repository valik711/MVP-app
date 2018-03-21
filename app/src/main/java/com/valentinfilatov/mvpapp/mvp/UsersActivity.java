package com.valentinfilatov.mvpapp.mvp;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import com.valentinfilatov.mvpapp.R;
import com.valentinfilatov.mvpapp.common.User;
import com.valentinfilatov.mvpapp.common.UserAdapter;
import com.valentinfilatov.mvpapp.database.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UsersActivity extends AppCompatActivity {

    private UserAdapter userAdapter;

    @BindView(R.id.name) protected EditText editTextName;
    @BindView(R.id.age) protected EditText editTextAge;
    private ProgressDialog progressDialog;


    private UsersPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        init();
    }

    private void init() {

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        userAdapter = new UserAdapter();

        RecyclerView userList = (RecyclerView) findViewById(R.id.list);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(userAdapter);


        DbHelper dbHelper = new DbHelper(this);
        UsersModel usersModel = new UsersModel(dbHelper);
        presenter = new UsersPresenter(usersModel);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    public PlayerData getUserData() {
        PlayerData userData = new PlayerData();
        userData.setName(editTextName.getText().toString());
        try {
            userData.setAge(Integer.parseInt(editTextAge.getText().toString()));
        } catch (NumberFormatException e){
            userData.setAge(0);
        }
        return userData;
    }

    public void showUsers(List<User> users) {
        userAdapter.setData(users);
    }

    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.please_wait));
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @OnClick(R.id.add) public void onAddClicked(){
        presenter.add();
    }

    @OnClick(R.id.clear) public void onClearClicked(){
        presenter.clear();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
