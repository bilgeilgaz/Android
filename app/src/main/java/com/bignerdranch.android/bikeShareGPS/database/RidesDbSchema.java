package com.bignerdranch.android.bikeShareGPS.database;


public class RidesDbSchema {
    public static final class RideTable {
        public static final String NAME = "rides";

        public static final class Cols {

            public static final String UUID= "uuid";
            public static final String WHATBIKE= "whatbike";
            public static final String STARTRIDE= "startride";
            public static final String ENDRIDE= "endride";
        }
    }
    public static final class BikeTable{
        public static final String NAME = "bikes";

        public static final class Cols {
            public static final String UUID= "uuid";
            public static final String BIKEID= "bikeid";
            public static final String BIKENAME= "bikename";
            public static final String BIKETYPE= "biketype";
            public static final String BIKEPRICE= "bikeprice";
        }
    }
}
