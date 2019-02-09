package com.bignerdranch.android.bikeShareGPS;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class FetchRides extends AsyncTask<String, Void, ArrayList<Ride>> {
  private static final String TAG = "FetchRides";
  private RidesDB sharedRides;

  public FetchRides(RidesDB sharedRides) {
    this.sharedRides = sharedRides;
  }

  private ArrayList<Ride> parse(String s) {
    //String s has the format  Rides:{rN,rS,rE:}*
    //where rN, rS,rE are strings

    Ride r;
    ArrayList<Ride> response = new ArrayList<>();

    s = RideParse.stringSkip(s, "Rides:");  //Skips prefix
    while (s.length() > 1) {
      r = new Ride();
      r.setMbikeName(RideParse.stringToken(s, ","));  //get rN
      s = RideParse.stringSkip(s, ",");

      r.setMstartRide(RideParse.stringToken(s, ","));  // get rS
      s = RideParse.stringSkip(s, ",");

      r.setMendRide(RideParse.stringToken(s, ":"));  //get rE
      s = RideParse.stringSkip(s, ":");

      response.add(r);
    }
    return response;
  }

  @Override
  protected ArrayList<Ride> doInBackground(String... params) {
    String s = "null";
    try {
      s = new NetworkFetcher().getUrlBytes(params[0]);
    } catch (IOException ioe) {
      Log.i("NetworkFetcher", ioe.toString());
    }
    return parse(s);
  }


  @Override
  protected void onPostExecute(ArrayList<Ride> response) {

    for (Ride i: response)  sharedRides.addFullRide(i);

    /*for (Ride i : response) {
      Log.d(TAG, "It is working");
      boolean hasId = false;
      sharedRides.addFullRide(i);

      for (Ride ride : sharedRides.getRidesDB()) {
        Log.d(TAG, "It is working");
        if (i.getId().toString().equals(ride.getId().toString())) {
          hasId = true;
          break;
        }
      }

      if (!hasId) {
        sharedRides.addFullRide(i);
      }

    }*/
  }
}
