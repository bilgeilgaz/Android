package com.bignerdranch.android.bikeShareGPS;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.UUID;

public class EndRideFragment extends Fragment {
  private static final String ARG_RIDE_ID = "RideId" ;
  private static RidesDB sharedRides;
  private Ride mRide;

  // GUI variables
  private Button stopRide;
  private TextView WhatField, newWhere;

  public static  EndRideFragment newInstance(UUID rideId){
    Bundle args = new Bundle();
    args.putSerializable(ARG_RIDE_ID, rideId);
    EndRideFragment fragment = new EndRideFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UUID id= (UUID) getArguments().getSerializable(ARG_RIDE_ID);
    sharedRides = RidesDB.get(getActivity());
    mRide= RidesDB.get(getActivity()).getRide(id);
  }


  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v= inflater. inflate(R.layout.end_ride, container, false);
    // Button
    stopRide= v.findViewById(R.id.add_button);

    // Textfields
    WhatField= v.findViewById(R.id.what_text);
    newWhere= v. findViewById(R.id.where_text);
    WhatField.setText(sharedRides.currentRide().getMbikeName());

    // ending ride
    stopRide.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
      if ( (newWhere.getText().length() > 0)) {
        sharedRides.endRide(newWhere.getText().toString().trim());
        getActivity().recreate();  //restarts BikeShareActivity
      }
      }
    });
    return v;
  }
}
