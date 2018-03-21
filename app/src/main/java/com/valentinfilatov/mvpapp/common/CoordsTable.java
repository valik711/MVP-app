package com.valentinfilatov.mvpapp.common;

public class CoordsTable {

    public static final String TABLE = "coordinates";

    public static class COLUMN {
        public static final String ID = "_id";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String DATE = "date";
    }

    public static final String CREATE_SCRIPT =
            String.format("create table %s ("
                            + "%s integer primary key autoincrement,"
                            + "%s real,"
                            + "%s real,"
                            + "%s integer"
                            + ");",
                    TABLE, COLUMN.ID, COLUMN.LAT, COLUMN.LNG, COLUMN.DATE);

}
