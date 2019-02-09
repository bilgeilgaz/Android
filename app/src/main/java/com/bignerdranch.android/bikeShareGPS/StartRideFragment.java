package com.bignerdranch.android.bikeShareGPS;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StartRideFragment extends Fragment {
  // fragment only instantiated when there is no current ride

  private static RidesDB sharedRides;

  // GUI variables
  private Button addRide;
  private TextView lastAdded;
  private TextView newWhat, newWhere;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedRides = RidesDB.get(getActivity());
  }

  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v= inflater. inflate(R.layout.start_ride, container, false);
    lastAdded= v.findViewById(R.id.last_ride);

    // Button
    addRide= v.findViewById(R.id.add_button);

    // Texts
    newWhat= v.findViewById(R.id.what_text);
    newWhere= v.findViewById(R.id.where_text);

    addRide.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if ((newWhat.getText().length() > 0) && (newWhere.getText().length() > 0)) {
          sharedRides.startRide(newWhat.getText().toString().trim(), newWhere.getText().toString().trim());
          lastAdded.setText(newWhat.getText().toString().trim()+
                  " started here:" + newWhere.getText().toString().trim());
          getActivity().recreate();  //BikeShareActivity restarted
        }
      }
    });
    return v;
  }
}
