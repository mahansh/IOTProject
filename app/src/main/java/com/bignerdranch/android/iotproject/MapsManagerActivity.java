package com.bignerdranch.android.iotproject;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsManagerActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private List<Marker> mMarkers;
    private List<LatLng> mLngLine1;
    private List<LatLng> mLngLine2;

    private EditText mEditText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mMarkers = new ArrayList<>();
        mLngLine1 = new ArrayList<>();
        mLngLine2 = new ArrayList<>();

        mButton = (Button) findViewById(R.id.button_map);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    goLocation(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        if (mGoogleMap != null) {
            addMarkers();
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;//اطلاعات رو با حالت شیشه ای میندازه روی مپ
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_manager, null);

                    TextView tv_locality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tv_ratio = (TextView) v.findViewById(R.id.tv_ratio);
                    TextView tv_ratioAll = (TextView) v.findViewById(R.id.tv_ratio_all);
                    TextView tv_nameLine = (TextView) v.findViewById(R.id.tv_name_line);
                    TextView tv_numLine = (TextView) v.findViewById(R.id.tv_num_line);

                    List<String> infor = getInfoManag(marker.getTitle());

                    tv_locality.setText(infor.get(0));
                    tv_nameLine.setText("name line: " + infor.get(1));
                    tv_ratio.setText("ratio: " + infor.get(4));
                    tv_ratioAll.setText("ratio all: " + infor.get(5));
                    tv_numLine.setText("num subway in line: " + infor.get(6));

                    return v;
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    addMarker("locality", latLng.latitude, latLng.longitude);
//                    mEditText.setText("");
                }
            });

        }

        goToLocationZoom(35.6873796, 51.3933181, 15);

    }

    private void goToLocation(double lat, double len) {
        LatLng latLng = new LatLng(lat, len);
        CameraUpdate update = CameraUpdateFactory.newLatLng(latLng);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double len, float zoom) {
        LatLng latLng = new LatLng(lat, len);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mGoogleMap.moveCamera(update);
    }

    public void goLocation(View view) throws IOException {
        String location = mEditText.getText().toString();

        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(location, 1);
        if (list.size() > 0) {
            Address address = list.get(0);
            String locality = address.getLocality();

            Toast.makeText(this, locality, Toast.LENGTH_SHORT).show();

            goToLocationZoom(address.getLatitude(), address.getLongitude(), 15);

            addMarker(locality, address.getLatitude(), address.getLongitude());
        } else {
            Toast.makeText(this, "This text don't exist: " + location, Toast.LENGTH_SHORT).show();
        }

    }

    public void addMarker(String title, double lat, double len) {
        MarkerOptions mark = new MarkerOptions()
                .title(title)
//                .draggable(true)
                .position(new LatLng(lat, len))
                .snippet("Subway");
        Marker marker = mGoogleMap.addMarker(mark);
        mMarkers.add(marker);
    }

    public void addMarkers() {
        String[] title = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        double[] lats = {35.6873796880, 35.6974896880, 35.7075996880, 35.7176096880, 35.6937806880, 35.7038906880, 35.713900880, 35.6873796880, 35.6974896880, 35.7075996880, 35.7176096880};
        double[] lngs = {51.393318101763, 51.393318101763, 51.393318101763, 51.393318101763, 51.384319101763, 51.384319101763, 51.384319101763, 51.374319101763, 51.374319101763, 51.374319101763, 51.374319101763};

//        for (int i = 0; i < title.length; i++) {
//            if (i < 4) {
//                mLngLine1.add(new LatLng(lats[i], lngs[i]));
//            }else if (i == 5) {
//                mLngLine2.add(2, new LatLng(lats[i], lngs[i]));
//            }else if (i > 5) {
//                mLngLine2.add(new LatLng(lats[i], lngs[i]));
//            }else if (i == 4) {
////                mLngLine1.add(new LatLng(lats[i], lngs[i]));
//                mLngLine1.add(0, new LatLng(lats[i], lngs[i]));
//            }
//        }

        mLngLine1.add(new LatLng(lats[0], lngs[0]));
        mLngLine1.add(new LatLng(lats[4], lngs[4]));
        mLngLine1.add(new LatLng(lats[1], lngs[1]));
        mLngLine1.add(new LatLng(lats[2], lngs[2]));
        mLngLine1.add(new LatLng(lats[6], lngs[6]));
        mLngLine1.add(new LatLng(lats[3], lngs[3]));


        mLngLine2.add(new LatLng(lats[7], lngs[7]));
        mLngLine2.add(new LatLng(lats[4], lngs[4]));
        mLngLine2.add(new LatLng(lats[8], lngs[8]));
        mLngLine2.add(new LatLng(lats[5], lngs[5]));
        mLngLine2.add(new LatLng(lats[6], lngs[6]));
        mLngLine2.add(new LatLng(lats[9], lngs[9]));
        mLngLine2.add(new LatLng(lats[10], lngs[10]));

        String[] vaz = {"Basy", "Easy", "Basy", "Easy", "Basy", "Easy", "Basy", "Easy", "Easy", "Basy", "Basy"};


        for (int i = 0; i < title.length; i++) {
//            addMarker(title[i], lats[i], lngs[i]);
            MarkerOptions mark = new MarkerOptions()
                    .title(title[i])
                    .position(new LatLng(lats[i], lngs[i]))
                    .snippet(vaz[i]);
            mGoogleMap.addMarker(mark);
        }

        addLine(mLngLine1, 1);
        addLine(mLngLine2, 2);
    }

    public void addLine(List<LatLng> ml, int numLine) {
        PolylineOptions plo = new PolylineOptions()
                .width(15);

        if (numLine == 1) {
            plo.color(Color.GREEN);
        } else if (numLine == 2) {
            plo.color(Color.BLUE);
        }

        for (int i = 0; i < ml.size(); i++) {
            plo.add(ml.get(i));
        }

        mGoogleMap.addPolyline(plo);
    }

    public List<String> getInfoManag(String title){
        List<LocationPlace> places = new ArrayList<>();
        List<String> infor;

        for (int i = 0; i< places.size(); i++){
            if (title.equalsIgnoreCase(places.get(i).getName())){
                infor = places.get(i).getInfor();
                i = places.size();
                return infor;
            }
        }
        return null;
    }

    public void gaudeLine() {
        PolylineOptions plo = new PolylineOptions()
                .width(5);

        plo.color(Color.RED);

        plo.add(mLngLine1.get(0));
        plo.add(mLngLine1.get(1));
        plo.add(mLngLine2.get(2));
        plo.add(mLngLine2.get(3));
        plo.add(mLngLine2.get(4));

        mGoogleMap.addPolyline(plo);
    }
}
