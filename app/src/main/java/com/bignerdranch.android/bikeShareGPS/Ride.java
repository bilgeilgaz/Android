package com.bignerdranch.android.bikeShareGPS;

import java.util.UUID;

public class Ride {
    private String mbikeName;
    private String mstartRide;
    private String mendRide;
    private final UUID mUUID;

    public Ride(){
        mUUID= UUID.randomUUID();
        //this(UUID.randomUUID());
    mbikeName= "";  mstartRide= "";   mendRide= ""; }
    public Ride(String name, String start) { mbikeName= name; mstartRide= start;
        mUUID= UUID.randomUUID();
    }
    public Ride(String name, String startRide, String endRide) {
        mUUID= UUID.randomUUID();
        //this(UUID.randomUUID());
        mbikeName= name;
        mstartRide= startRide;
        mendRide= endRide;
    }

    public Ride(UUID id) {
        mUUID = id;
    }

    public String getMbikeName() {return mbikeName;}
    public void setMbikeName(String mbikeName) {
        this.mbikeName = mbikeName;
    }
    public String getMstartRide() {
        return mstartRide;
    }
    public void setMstartRide(String mstartRide) {
        this.mstartRide = mstartRide;
    }
    public String getMendRide() { return mendRide; }
    public void setMendRide(String mendRide) { this.mendRide = mendRide; }
    public String toString() { return mbikeName + " started: " + mstartRide + " ended: " + mendRide; }
    public UUID getId() {return mUUID;}

}
