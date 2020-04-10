package com.example.hieuphong_vu_comp304_003_test02.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hieuphong_vu_comp304_003_test02.Entity.Booking;
import com.example.hieuphong_vu_comp304_003_test02.R;
import com.example.hieuphong_vu_comp304_003_test02.ViewModel.BookingViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ViewBookingDetailsActivity extends AppCompatActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,AdapterView.OnItemSelectedListener{
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    BookingViewModel bookingViewModel;
    ArrayList<String> spinnerItems;
    ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_booking_details);

        Spinner spinnerBookings=(Spinner)findViewById(R.id.spinnerBookings);
        spinnerItems=new ArrayList<>();
        spinnerItems.add("Find Org Location");

        bookingViewModel= ViewModelProviders.of(this).get(BookingViewModel.class);
        bookingViewModel.getAllBookings().observe(this, new Observer<List<Booking>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable List<Booking> bookings) {

                SharedPreferences prf=getSharedPreferences("user_details",MODE_PRIVATE);
                int currentUser=prf.getInt("userId",0);
                List<Booking> currentUserBookings=bookings.stream().filter(x->x.getUserId()==currentUser).collect(Collectors.toList());
                for(Booking curUserBooking:currentUserBookings){
                    spinnerItems.add("Booking"+curUserBooking.getBookingId()+"-"+bookingViewModel.getOrgLocationByBookingId(curUserBooking.getBookingId()));
                }
                adapter= new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerBookings.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

        spinnerBookings.setOnItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        final Button btnMyLocation=(Button)findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                //Place current location marker
                LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Current Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                //move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                Toast.makeText(getApplicationContext(),"Current Location:\n"
                        +"Longtitude: "+latLng.longitude+"\nLatitude: "+latLng.latitude,Toast.LENGTH_SHORT).show();

                //stop location updates
                //if (mGoogleApiClient != null) {
                //    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                //}
            }
        });

        final Button btnReturn=(Button)findViewById(R.id.buttonReturn);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent=new Intent(ViewBookingDetailsActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MapStyleOptions mapStyleOptions = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mMap.setMapStyle(mapStyleOptions);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
            else{
                //request for permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1
                );
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Suspended!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Failed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please turn on Location permission!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        if(position==0){
            return;
        }
        String chosenSpinnerItem=spinnerItems.get(position);
        String location=chosenSpinnerItem.substring(chosenSpinnerItem.indexOf("-")+1);
        new SearchLocationAsyncTask(location).execute();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

    public class SearchLocationAsyncTask extends AsyncTask<Void,Void, Address> {
        String errorMessage = "";
        String name;

        SearchLocationAsyncTask(String name){
            this.name=name;
        }

        @Override
        protected Address doInBackground(Void... voids) {
            Geocoder geocoder = new Geocoder(ViewBookingDetailsActivity.this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                errorMessage="Can't find this location due to: \n"+e.getMessage();
            }
            if(addresses != null && addresses.size() > 0)
            {
                return addresses.get(0);
            }

            return null;
        }

        protected void onPostExecute(Address address) {
            if(address == null) {
                Toast.makeText(ViewBookingDetailsActivity.this,errorMessage,Toast.LENGTH_SHORT).show();
            }
            else {
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                mCurrLocationMarker = mMap.addMarker(markerOptions);

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                Toast.makeText(getApplicationContext(),name+"\nLatitude: "+address.getLatitude()+"\nLongtitude: "+address.getLongitude(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
