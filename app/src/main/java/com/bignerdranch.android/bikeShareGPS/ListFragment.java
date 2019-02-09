package com.bignerdranch.android.bikeShareGPS;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ListFragment extends Fragment implements Observer {
  private static RidesDB sharedRides;

  //View to list all rides
  private RecyclerView mRidesList;
  private RidesAdapter mAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sharedRides = RidesDB.get(getActivity());
    sharedRides.addObserver(this);
  }

  @Override
  public void update(Observable observable, Object data) {
    //updateListOfThings();
    mAdapter.notifyDataSetChanged();
  }

  public class RideHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private TextView mWhatTextView, mStartTextView, mEndTextView;
    public Ride mRide;

    public RideHolder(LayoutInflater inflater, ViewGroup parent){
      super(inflater.inflate(R.layout.list_item, parent, false));
      mWhatTextView= itemView.findViewById(R.id.what_bike_ride);
      mStartTextView= itemView.findViewById(R.id.start_ride);
      mEndTextView= itemView.findViewById(R.id.end_ride);
      itemView.setOnClickListener(this);
    }

    public void bind(Ride ride){
      mRide= ride;
      mWhatTextView.setText(mRide.getMbikeName());
      mStartTextView.setText(mRide.getMstartRide());
      mEndTextView.setText(mRide.getMendRide());
    }

    @Override
    public void onClick(View v) {
        //sharedRides.delete(getActivity(), getAdapterPosition());
    }
  }


  private class RidesAdapter extends RecyclerView.Adapter<RideHolder> {
    private ArrayList<Ride> mRides;

    public RidesAdapter(ArrayList <Ride> rides){ mRides= rides;}

    @Override
    public RideHolder onCreateViewHolder(ViewGroup parent, int viewType){
      LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
      return new RideHolder(layoutInflater, parent);
     }

    @Override
    public void onBindViewHolder(RideHolder holder, int position) {
      Ride ride= mRides.get(position);
      holder.bind(ride);
    }

    @Override
    public int getItemCount() {
      return mRides.size();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v= inflater. inflate(R.layout.fragment_list, container, false);
    mRidesList= v.findViewById(R.id.list_recycler_view);
    mRidesList.setLayoutManager(new LinearLayoutManager(getActivity()));
    mAdapter= new RidesAdapter(sharedRides.getRidesDB());
    mRidesList.setAdapter(mAdapter);

    return v;
  }
}
