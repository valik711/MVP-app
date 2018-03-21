package com.valentinfilatov.mvpapp.mvp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.valentinfilatov.mvpapp.common.Coordinate;
import com.valentinfilatov.mvpapp.common.CoordsTable;
import com.valentinfilatov.mvpapp.database.DbHelper;

public class CoordsModel {

    private final DbHelper dbHelper;

    public CoordsModel(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public void loadCoords(LoadCoordCallback callback) {
        LoadCoordsTask loadCoordsTask = new LoadCoordsTask(callback);
        loadCoordsTask.execute();
    }

    public void addCoord(ContentValues contentValues, CompleteCallback callback) {
        AddCoordTask addCoordTask = new AddCoordTask(callback);
        addCoordTask.execute(contentValues);
    }

    public void clearCoords(CompleteCallback completeCallback) {
        ClearCoordsTask clearCoordsTask = new ClearCoordsTask(completeCallback);
        clearCoordsTask.execute();
    }


    interface LoadCoordCallback {
        void onLoad(List<Coordinate> Coords);
    }

    interface CompleteCallback {
        void onComplete();
    }

    class LoadCoordsTask extends AsyncTask<Void, Void, List<Coordinate>> {

        private final LoadCoordCallback callback;

        LoadCoordsTask(LoadCoordCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<Coordinate> doInBackground(Void... params) {
            List<Coordinate> Coords = new LinkedList<>();
            Cursor cursor = dbHelper.getReadableDatabase().query(CoordsTable.TABLE, null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                Coordinate Coord = new Coordinate();
                Coord.setId(cursor.getLong(cursor.getColumnIndex(CoordsTable.COLUMN.ID)));
                Coord.setLat(cursor.getDouble(cursor.getColumnIndex(CoordsTable.COLUMN.LAT)));
                Coord.setLng(cursor.getDouble(cursor.getColumnIndex(CoordsTable.COLUMN.LNG)));
                Coord.setDate(cursor.getInt(cursor.getColumnIndex(CoordsTable.COLUMN.DATE)));
                Coords.add(Coord);
            }
            cursor.close();
            return Coords;
        }

        @Override
        protected void onPostExecute(List<Coordinate> Coords) {
            if (callback != null) {
                callback.onLoad(Coords);
            }
        }
    }

    class AddCoordTask extends AsyncTask<ContentValues, Void, Void> {

        private final CompleteCallback callback;

        AddCoordTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(ContentValues... params) {
            ContentValues cvCoord = params[0];
            dbHelper.getWritableDatabase().insert(CoordsTable.TABLE, null, cvCoord);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    class ClearCoordsTask extends AsyncTask<Void, Void, Void> {

        private final CompleteCallback callback;

        ClearCoordsTask(CompleteCallback callback) {
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            dbHelper.getWritableDatabase().delete(CoordsTable.TABLE, null, null);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (callback != null) {
                callback.onComplete();
            }
        }
    }


}
