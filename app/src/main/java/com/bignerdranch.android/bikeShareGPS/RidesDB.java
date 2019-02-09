package com.bignerdranch.android.bikeShareGPS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.bikeShareGPS.database.BikeCursorWrapper;
import com.bignerdranch.android.bikeShareGPS.database.RideBaseHelper;
import com.bignerdranch.android.bikeShareGPS.database.RideCursorWrapper;
import com.bignerdranch.android.bikeShareGPS.database.RidesDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.UUID;


public class RidesDB extends Observable {   // Singleton
  private static RidesDB sRidesDB;
  private static SQLiteDatabase mDatabase;
  private Context mContext;
  private Ride mlastRide= new Ride("", "");
  private Bike mBike= new Bike("","","","");

  public synchronized static RidesDB get(Context context) {
     if (sRidesDB == null) { sRidesDB= new RidesDB(context);}
    return sRidesDB;
  }

  private static ContentValues getContentValues(Ride ride){
        ContentValues values = new ContentValues();
        values.put(RidesDbSchema.RideTable.Cols.UUID, ride.getId().toString());
        values.put(RidesDbSchema.RideTable.Cols.WHATBIKE, ride.getMbikeName());
        values.put(RidesDbSchema.RideTable.Cols.STARTRIDE, ride.getMstartRide());
        values.put(RidesDbSchema.RideTable.Cols.ENDRIDE, ride.getMendRide());
        return values;
  }

  private static ContentValues getContentValuesB(Bike bike){
        ContentValues values = new ContentValues();
        values.put(RidesDbSchema.BikeTable.Cols.UUID, bike.getId().toString());
        values.put(RidesDbSchema.BikeTable.Cols.BIKENAME,bike.getMname());
        values.put(RidesDbSchema.BikeTable.Cols.BIKEID,bike.getMID().toString());
        values.put(RidesDbSchema.BikeTable.Cols.BIKETYPE,bike.getMType());
        values.put(RidesDbSchema.BikeTable.Cols.BIKEPRICE,bike.getMPrice().toString());
        return values;
    }

  public synchronized ArrayList<Ride> getRidesDB() {
      ArrayList<Ride> rides= new ArrayList<Ride>();
      RideCursorWrapper cursor=queryRides(null, null);
      cursor.moveToFirst();
      while (!cursor.isAfterLast()) {
          rides.add(cursor.getRide());
          cursor.moveToNext();
      }
      cursor.close();
      return rides;
  }

  public synchronized ArrayList<Bike> getBikesDB() {
        ArrayList<Bike> bikes= new ArrayList<Bike>();
        BikeCursorWrapper cursor=queryBikes(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            bikes.add(cursor.getBike());
            cursor.moveToNext();
        }
        cursor.close();
        return bikes;
    }

  public synchronized void startRide(String what, String where) {
    mlastRide.setMbikeName(what);
    mlastRide.setMstartRide(where);
  }

  public synchronized void endRide(String where) {
    mlastRide.setMendRide(where);
    this.addFullRide(mlastRide);
    mlastRide= new Ride("", "");
    this.setChanged();
    notifyObservers();
  }

  public synchronized void registerBike(String bikeName, String bikeID, String bikePrice, String bikeType) {

      mBike.setMname(bikeName);
      mBike.setMID(bikeID);
      mBike.setMPrice(bikePrice);
      mBike.setMType(bikeType);
      this.addBikes(mBike);
      mBike=new Bike();
      this.setChanged();
      notifyObservers();
  }

  public synchronized void addFullRide(Ride r) {
      if(r==null) return;
      this.setChanged();
      notifyObservers();
      ContentValues values= getContentValues(r);
      mDatabase.insert(RidesDbSchema.RideTable.NAME, null, values);
  }

  public synchronized void addBikes(Bike b){
      if(b==null) return;
      this.setChanged();
      notifyObservers();
      ContentValues values= getContentValuesB(b);
      mDatabase.insert(RidesDbSchema.BikeTable.NAME, null, values);
  }

  Ride getRide(UUID id){
      RideCursorWrapper cursor= queryRides(RidesDbSchema.RideTable.Cols.UUID+" =?", new String[]{ id.toString()
      });
      try {
          if (cursor.getCount() == 0) {
              return null;
          }
          cursor.moveToFirst();
          Ride ride = cursor.getRide();
          if( ride.getId()== null){
              return new Ride();
          }else{
              return ride;
          }
      } finally {
          cursor.close();
      }
  }

  Bike getBike(UUID id){
        BikeCursorWrapper cursor= queryBikes(RidesDbSchema.BikeTable.Cols.UUID+" =?", new String[]{ id.toString()
        });
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            Bike bike = cursor.getBike();
            if( bike.getId()== null){
                return new Bike();
            }else{
                return bike;
            }
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Bike bike) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, bike.getPhotoFilename());
    }

  public synchronized boolean activeRide(){return (  (!mlastRide.getMbikeName().equals("")) && (!mlastRide.getMstartRide().equals("")) );}

  public synchronized Ride currentRide(){return activeRide() ? mlastRide : new Ride();}

  public synchronized Bike currentBike(){return new Bike();}

  private RidesDB(Context context) {
      mContext= context.getApplicationContext();
      mDatabase = new RideBaseHelper(mContext).getWritableDatabase();
      this.setChanged();
      notifyObservers();
  }


  public static RideCursorWrapper queryRides(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RidesDbSchema.RideTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new RideCursorWrapper(cursor);
    }

    public static BikeCursorWrapper queryBikes (String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                RidesDbSchema.BikeTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new BikeCursorWrapper(cursor);
    }

    public static void updateRide(Ride ride) {
        String uuidString = ride.getId().toString();
        ContentValues values = getContentValues(ride);
        mDatabase.update(RidesDbSchema.RideTable.NAME, values, RidesDbSchema.RideTable.Cols.UUID + " = ?",
        new String[] { uuidString });
  }

    public static void updateBike(Bike bike) {
        String uuidString = bike.getId().toString();
        ContentValues values = getContentValuesB(bike);
        mDatabase.update(RidesDbSchema.BikeTable.NAME, values, RidesDbSchema.BikeTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }
}