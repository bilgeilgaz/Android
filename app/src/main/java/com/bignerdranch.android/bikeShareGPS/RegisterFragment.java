package com.bignerdranch.android.bikeShareGPS;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

public class RegisterFragment extends Fragment implements Observer {

    private static RidesDB sharedRides;
    private Bike mBike;
    private static final String ARG_RIDE_ID = "BikeId" ;
    private BikesAdapter mBikesAdapter;
    private RecyclerView mBikesList;

    private File mPhotoFile;

    // GUI variables
    private Button register_button2;
    private TextView bikeName, bikeID, bikeType, bikePrice;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    private static final int REQUEST_PHOTO= 2;

    public static  RegisterFragment newInstance(UUID bikeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_RIDE_ID, bikeId);
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID id= (UUID) getArguments().getSerializable(ARG_RIDE_ID);
        sharedRides = RidesDB.get(getActivity());
        sharedRides.addObserver(this);
        mBike= RidesDB.get(getActivity()).getBike(id);
        mBike= new Bike();
        mPhotoFile = RidesDB.get(getActivity()).getPhotoFile(mBike);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater. inflate(R.layout.register, container, false);

        // Button
        register_button2= v.findViewById(R.id.register_button2);

        // Texts
        bikeName= v.findViewById(R.id.bike_name);
        bikeID= v.findViewById(R.id.bike_id);
        bikeType=v.findViewById(R.id.bike_type);
        bikePrice=v.findViewById(R.id.bike_price);

        mBikesList= v.findViewById(R.id.fragment_list_container);
        mBikesList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBikesAdapter= new BikesAdapter(sharedRides.getBikesDB());
        mBikesList.setAdapter(mBikesAdapter);

        register_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // bike is registered
                if ((bikeName.getText().length() > 0)&& (bikeID.getText().length() > 0)) {
                    sharedRides.registerBike(bikeName.getText().toString(),bikeID.getText().toString(),bikePrice.getText().toString(),
                            bikeType.getText().toString());
                    getActivity().recreate();
                }
            }
        });
        PackageManager packageManager = getActivity().getPackageManager();
        mPhotoButton = (ImageButton) v.findViewById(R.id.bike_camera);

        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.bignerdranch.android.bikeShareScreen.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.bike_photo);
        updatePhotoView();

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.bignerdranch.android.bikeShareScreen.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updatePhotoView();
        }
    }


    public class BikeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mBikeID, mBikeName, mBikeType, mBikePrice;
        public Bike mBike;

        public BikeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_register, parent, false));
            mBikeID= itemView.findViewById(R.id.bike_id);
            mBikeName= itemView.findViewById(R.id.bike_name);
            mBikeType= itemView.findViewById(R.id.bike_type);
            mBikePrice= itemView.findViewById(R.id.bike_price);
            itemView.setOnClickListener(this);
        }

        public void bind(Bike bike){
            mBike= bike;
            mBikeID.setText(mBike.getMID());
            mBikeName.setText(mBike.getMname());
            mBikeType.setText(mBike.getMType());
            mBikePrice.setText(mBike.getMPrice());
        }

        @Override
        public void onClick(View v) {
            //sharedRides.delete(getActivity(), getAdapterPosition());
        }
    }

    private class BikesAdapter extends RecyclerView.Adapter<BikeHolder> {
        private ArrayList<Bike> mBikes;

        public BikesAdapter(ArrayList <Bike> bikes){ mBikes= bikes;}

        @Override
        public BikeHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            return new BikeHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(BikeHolder holder, int position) {
            Bike bike= mBikes.get(position);
            holder.bind(bike);
        }

        @Override
        public int getItemCount() {
            return mBikes.size();
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        //updateListOfThings();
        mBikesAdapter.notifyDataSetChanged();
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

}
