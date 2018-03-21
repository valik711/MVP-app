package com.valentinfilatov.mvpapp.mvp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import com.valentinfilatov.mvpapp.GPSService;
import com.valentinfilatov.mvpapp.MapActivity;
import com.valentinfilatov.mvpapp.R;
import com.valentinfilatov.mvpapp.common.Coordinate;
import com.valentinfilatov.mvpapp.common.CoordinateAdapter;
import com.valentinfilatov.mvpapp.database.DbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoordsActivity extends AppCompatActivity {

    private CoordinateAdapter userAdapter;

    @BindView(R.id.lat) protected EditText etLat;
    @BindView(R.id.lng) protected EditText etLng;
    private ProgressDialog progressDialog;


    private CoordsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        init();
        startService(new Intent(this, GPSService.class));
    }

    private void init() {

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        userAdapter = new CoordinateAdapter();

        RecyclerView userList = (RecyclerView) findViewById(R.id.list);
        userList.setLayoutManager(layoutManager);
        userList.setAdapter(userAdapter);


        DbHelper dbHelper = new DbHelper(this);
        CoordsModel usersModel = new CoordsModel(dbHelper);
        presenter = new CoordsPresenter(usersModel);
        presenter.attachView(this);
        presenter.viewIsReady();
    }

    public CoordData getCoordData() {
        CoordData coordData = new CoordData();
        coordData.setTime((new Date()).getTime());
        try {
            coordData.setLat(Double.parseDouble(etLat.getText().toString()));
            coordData.setLng(Double.parseDouble(etLng.getText().toString()));
        } catch (NumberFormatException e){
            coordData.setTime(0);
        }
        return coordData;
    }

    public void showCoords(List<Coordinate> users) {
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
        stopService(new Intent(this, GPSService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.map:
                startActivity(new Intent(this, MapActivity.class));
                break;
        }
        return true;
    }
}
