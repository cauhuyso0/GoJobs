package vn.com.gojobs.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import vn.com.gojobs.interfake.ITurnOnGPSCallback;
import vn.com.gojobs.R;
import vn.com.gojobs.dialog.DialogTurnOnGPS;

public class MapFragment extends Fragment implements OnMapReadyCallback, ITurnOnGPSCallback, View.OnClickListener {

    private ImageView imgDirection;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location currentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        imgDirection = view.findViewById(R.id.img_diretion);
        imgDirection.setBackground(getContext().getResources().getDrawable(R.drawable.avatar_logo));
        imgDirection.setOnClickListener(this);
        createMap();
        return view;
    }

    private void createMap() {
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            focusGPS();
        }
    }

    @Override
    public void turnOnGPS() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.img_diretion) {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showDialogGPS();
            } else {
                focusGPS();
            }
        }
    }

    private void showDialogGPS() {
        fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        DialogTurnOnGPS dialogTurnOnGPS = new DialogTurnOnGPS(this);
        dialogTurnOnGPS.show(fragmentTransaction, DialogTurnOnGPS.TAG);
    }

    private void focusGPS() {
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentLocation = task.getResult();
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(),
                                    currentLocation.getLongitude()), 17));
                        }
                    }
                });
        mMap.setMyLocationEnabled(true);
    }
}
