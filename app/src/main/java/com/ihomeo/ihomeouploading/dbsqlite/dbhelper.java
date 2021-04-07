package com.ihomeo.ihomeouploading.dbsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by mr on 06/07/2016.
 */
public class dbhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "ihomeo_db";
    // Login TABLE

    public static final String TABLE_LOGIN = "LOGIN";
    public static final String COLUMN_ID = "id";//0
    public static final String COLUMN_UserID = "userName";//1
    public static final String COLUMN_Password = "password";//2
    public static final String COLUMN_AppVersion_no = "AppVersion";//3
    public static final String COLUMN_DOCTOR_ID = "DoctorId";//4
    public static final String COLUMN_DOCTOR_NAME = "DoctorName";//5
    public static final String COLUMN_REMEMBER_ME = "RememberMe";//6
    public static final String COLUMN_Installed_date = "InstallDate";//7


    public dbhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("DB", "DB Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_LOGIN
                + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_UserID + " VARCHAR,"
                + COLUMN_Password + " VARCHAR,"
                + COLUMN_AppVersion_no + " VARCHAR,"
                + COLUMN_DOCTOR_ID + " VARCHAR,"
                + COLUMN_DOCTOR_NAME + " VARCHAR,"
                + COLUMN_REMEMBER_ME + " VARCHAR,"
                + COLUMN_Installed_date + " VARCHAR);";
        db.execSQL(sql);
        Log.d("DB", "Table Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_LOGIN;
        db.execSQL(sql);
        onCreate(db);
    }

    // inserting records in Login table


    public void addLogin(dbhelper db, String userName, String password, String AppVersion, String DoctorId, String DoctorName, String RememberMe, String InstallDate) {
// TODO Auto-generated method stub

        ContentValues values = new ContentValues();

        values.put(COLUMN_UserID, userName);
        values.put(COLUMN_Password, password);
        values.put(COLUMN_AppVersion_no, AppVersion);
        values.put(COLUMN_DOCTOR_ID, DoctorId);
        values.put(COLUMN_DOCTOR_NAME, DoctorName);
        values.put(COLUMN_REMEMBER_ME, RememberMe);
        values.put(COLUMN_Installed_date, InstallDate);

        SQLiteDatabase SQLDB = db.getWritableDatabase();

        SQLDB.insert(TABLE_LOGIN, null, values);

        SQLDB.close();

        Log.d("DB", "Row Inserted");


    }
    //Remember me on updtate

    public void onupdate(dbhelper db, String userName, String password, String AppVersion, String DoctorId, String DoctorName, String RememberMe, String InstallDate) {
// TODO Auto-generated method stub

        ContentValues values = new ContentValues();

        values.put(COLUMN_UserID, userName);
        values.put(COLUMN_Password, password);
        values.put(COLUMN_AppVersion_no, AppVersion);
        values.put(COLUMN_DOCTOR_ID, DoctorId);
        values.put(COLUMN_DOCTOR_NAME, DoctorName);
        values.put(COLUMN_REMEMBER_ME, RememberMe);
        values.put(COLUMN_Installed_date, InstallDate);

        SQLiteDatabase SQLDB = db.getWritableDatabase();

        SQLDB.insert(TABLE_LOGIN, null, values);

        SQLDB.close();

        Log.d("DB", "Row Inserted");


    }


    public boolean FindRows(dbhelper db, String userName, String password) {

        SQLiteDatabase SQLDB = db.getWritableDatabase();

        Cursor cursor = SQLDB.query(true, TABLE_LOGIN, new String[]{
                        COLUMN_ID,
                        COLUMN_UserID,
                        COLUMN_Password,
                },
                COLUMN_UserID + "=?" + " and " +
                        COLUMN_Password + "=?",
                new String[]{userName, password},
                null, null, null, null);

        if (cursor.getCount() > 0) {
            return true;
        } else
            return false;
    }


    public String[] FindRememberRows(dbhelper db, String RememberMe) {

        SQLiteDatabase SQLDB = db.getWritableDatabase();

        Cursor cursor = SQLDB.rawQuery("SELECT * FROM " + TABLE_LOGIN + " where " + COLUMN_REMEMBER_ME + " = '" + RememberMe + "';", null);

        String[] StrArr = new String[2];


        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            StrArr[0] = cursor.getString(1);
            StrArr[1] = cursor.getString(2);
            Log.d("Cr", cursor.getString(1) + "," + cursor.getString(2));
            cursor.close();

        }
        return StrArr;
    }


    public String[] FindRememberRowsUName(dbhelper db) {

        SQLiteDatabase SQLDB = db.getWritableDatabase();

        Cursor cursor = SQLDB.rawQuery("SELECT * FROM " + TABLE_LOGIN + ";", null);

        String[] StrArr = new String[2];
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            StrArr[0] = cursor.getString(1);
            StrArr[1] = cursor.getString(2);

            cursor.close();

        }
        return StrArr;
    }
}

