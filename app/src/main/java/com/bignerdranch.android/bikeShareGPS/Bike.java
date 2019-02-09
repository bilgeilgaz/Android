package com.bignerdranch.android.bikeShareGPS;

import java.util.UUID;

public class Bike {

    private String mName;
    private String mID;
    private String mType;
    private String mPrice;
    private final UUID mUUID;
    


    public Bike(){
        mUUID= UUID.randomUUID();
        mID="";
        mName="";
        mType="";
        mPrice ="";
    }

    public Bike(String name, String ID, String type, String bikePrice) {
        mUUID= UUID.randomUUID();
        mID= ID;
        mName=name;
        mType=type;
        mPrice =bikePrice;
    }
    public UUID getId() {return mUUID;}
    public Bike(UUID id) {
        mUUID = id;
    }

    public String getMID() {return mID;}
    public void setMID( String mID){ this.mID= mID;}

    public String getMname() {return mName;}
    public void setMname( String mName){ this.mName=mName;}

    public String getMType() {return mType;}
    public void setMType( String mType){ this.mType= mType;}

    public String getMPrice() {return mPrice;}
    public void setMPrice( String mPrice){ this.mPrice= mPrice;}

    public String getPhotoFilename() {
        return "IMG_" + getId().toString() + ".jpg";
    }

}
