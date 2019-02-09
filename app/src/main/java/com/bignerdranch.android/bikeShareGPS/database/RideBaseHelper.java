package com.bignerdranch.android.bikeShareGPS.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class RideBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "ridesBase.db";

    public RideBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + RidesDbSchema.RideTable.NAME + "(" +
                RidesDbSchema.RideTable.Cols.UUID+ ", " +
                RidesDbSchema.RideTable.Cols.WHATBIKE + ", " +
                RidesDbSchema.RideTable.Cols.STARTRIDE + ", " +
                RidesDbSchema.RideTable.Cols.ENDRIDE + ")"
        );
        db.execSQL("create table " + RidesDbSchema.BikeTable.NAME + "(" +
                RidesDbSchema.RideTable.Cols.UUID+ ", " +
                RidesDbSchema.BikeTable.Cols.BIKEID+ ","+
                RidesDbSchema.BikeTable.Cols.BIKENAME+ ","+
                RidesDbSchema.BikeTable.Cols.BIKETYPE+ ","+
                RidesDbSchema.BikeTable.Cols.BIKEPRICE+ ")"
        );
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}
