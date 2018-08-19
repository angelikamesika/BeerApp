package com.example.angelika.beerapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.angelika.beerapp.utils.FileUtils;

/**
 * Created by Angelika on 02.08.2018.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";

    private static final String BASE_TYPE_INTEGER = "integer primary key autoincrement";
    private static final String TYPE_INTEGER = "integer";
    private static final String TYPE_TIMESTAMP = "timestamp";
    private static final String TYPE_VARCHAR = "nvarchar";
    private static final String TYPE_REAL = "real";

    // ======================= tables
    public static final String TABLE_CITIES = "cities";

    // ============================ fields
    public static final String CITY_NAME = "city";
    public static final String CITY_NAME_ASCII = "city_ascii";
    public static final String CITY_LAT = "lat";
    public static final String CITY_LNG = "lng";
    public static final String CITY_COUNTRY = "country";
    public static final String CITY_ISO2 = "iso2";
    public static final String CITY_ISO3 = "iso3";


//    city	The name of the city/town as a Unicode string (e.g. Bogotá)
//    city_ascii	city as an ASCII string (e.g. Bogota). Left blank if ASCII representation is not possible.
//    lat	The latitude of the city/town.
//    lng	The longitude of the city/town.
//    country	The name of the city/town's country.
//    iso2	The alpha-2 iso code of the country.
//    iso3	The alpha-3 iso code of the country.
//            admin

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, dbFile(context), null, DATABASE_VERSION);
    }

    private static String dbFile(Context aContext) {
        String dir = FileUtils.basePath(aContext) + "/db/";
        FileUtils.createDirectory(dir);
        return dir + "restaurantapp.db";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTables(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int aOldVersion, int aNewVersion) {
        Log.d(TAG, "onUpgrade() from " + aOldVersion + " to " + aNewVersion);

    }

    private void createTables(SQLiteDatabase aDB) {
        createCityTable(aDB);
    }

    private void executeSql(SQLiteDatabase aDB, String aSSQL) {
        try {
            aDB.execSQL(aSSQL);
        } catch (Exception e) {
            Log.e(TAG, "executeSql exception: " + e.getMessage());
        }
    }

    private void createCityTable(SQLiteDatabase aDB) {
        String sql = "CREATE TABLE " + TABLE_CITIES + " (" +
                BaseColumns._ID + " " + BASE_TYPE_INTEGER + "," +
                CITY_NAME + " " + TYPE_VARCHAR + "," +
                CITY_NAME_ASCII + " " + TYPE_VARCHAR + "," +
                CITY_LAT + " " + TYPE_VARCHAR + "," +
                CITY_LNG + " " + TYPE_VARCHAR + "," +
                CITY_COUNTRY + " " + TYPE_VARCHAR +
                CITY_ISO2 + " " + TYPE_VARCHAR +
                CITY_ISO3 + " " + TYPE_VARCHAR +
                ")" +
                ";";
        executeSql(aDB, sql);

        sql = "CREATE INDEX " + CITY_NAME + "_" + TABLE_CITIES + " ON " +
                TABLE_CITIES + "(" + CITY_NAME + ");";
        executeSql(aDB, sql);
    }
}
