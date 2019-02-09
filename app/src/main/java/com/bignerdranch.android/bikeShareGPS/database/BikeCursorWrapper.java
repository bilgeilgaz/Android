package com.bignerdranch.android.bikeShareGPS.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.bignerdranch.android.bikeShareGPS.Bike;
import java.util.UUID;

public class BikeCursorWrapper extends CursorWrapper {

    public BikeCursorWrapper(Cursor cursor) {super(cursor);}

    public Bike getBike() {
        String uuidString=getString(getColumnIndex(RidesDbSchema.BikeTable.Cols.UUID));
        String bikeID = getString(getColumnIndex(RidesDbSchema.BikeTable.Cols.BIKEID));
        String bikeName = getString(getColumnIndex(RidesDbSchema.BikeTable.Cols.BIKENAME));
        String bikeType = getString(getColumnIndex(RidesDbSchema.BikeTable.Cols.BIKETYPE));
        String bikePrice = getString(getColumnIndex(RidesDbSchema.BikeTable.Cols.BIKEPRICE));

        Bike bike = new Bike(UUID.fromString(uuidString));
        bike.setMID(bikeID);
        bike.setMname(bikeName);
        bike.setMType(bikeType);
        bike.setMPrice(bikePrice);
        return bike;
    }
}
