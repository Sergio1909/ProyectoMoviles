package com.example.proyectomoviles;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapitaFragment extends Fragment implements OnMapReadyCallback {

    MapView mapView;
    GoogleMap map;
    //final double latitudMapita;
    // final double longitudMapita;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private double latitudMapita;
    private double longitudMapita;

    public MapitaFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MapitaFragment newInstance(double latitudMapita, double longitudMapita) {
        MapitaFragment fragment = new MapitaFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, latitudMapita);
        args.putDouble(ARG_PARAM2, longitudMapita);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            latitudMapita = getArguments().getDouble(ARG_PARAM1);
            longitudMapita = getArguments().getDouble(ARG_PARAM2);
        }
    }

    @Override // HECHO
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapita, container, false);
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return v ; }

    @Override //HECHO
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.addMarker(new MarkerOptions().position(new LatLng(latitudMapita, longitudMapita)));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitudMapita, longitudMapita), 10));

    }
}