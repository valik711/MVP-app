package com.valentinfilatov.mvpapp.mvp;

import android.content.ContentValues;

import java.util.List;

import com.valentinfilatov.mvpapp.R;
import com.valentinfilatov.mvpapp.common.Coordinate;
import com.valentinfilatov.mvpapp.common.CoordsTable;

public class CoordsPresenter {

    private CoordsActivity view;
    private final CoordsModel model;

    public CoordsPresenter(CoordsModel model) {
        this.model = model;
    }

    public void attachView(CoordsActivity coordsActivity) {
        view = coordsActivity;
    }

    public void detachView() {
        view = null;
    }


    public void viewIsReady() {
        loadCoords();
    }

    public void loadCoords() {
        model.loadCoords(new CoordsModel.LoadCoordCallback() {
            @Override
            public void onLoad(List<Coordinate> coords) {
                view.showCoords(coords);
            }
        });
    }

    public void add() {
        CoordData coordData = view.getCoordData();
        if (coordData.getTime() == 0) {
            view.showToast(R.string.empty_values);
            return;
        }

        ContentValues cv = new ContentValues(3);
        cv.put(CoordsTable.COLUMN.LAT, coordData.getLat());
        cv.put(CoordsTable.COLUMN.LNG, coordData.getLng());
        cv.put(CoordsTable.COLUMN.DATE, coordData.getTime());
        view.showProgress();
        model.addCoord(cv, new CoordsModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadCoords();
            }
        });
    }

    public void clear() {
        view.showProgress();
        model.clearCoords(new CoordsModel.CompleteCallback() {
            @Override
            public void onComplete() {
                view.hideProgress();
                loadCoords();
            }
        });
    }

}
