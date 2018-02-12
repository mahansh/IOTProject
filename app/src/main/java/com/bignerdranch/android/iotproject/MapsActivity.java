package com.bignerdranch.android.iotproject;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private View mView;
    private List<Marker> mMarkers;
    private List<MarkerOptions> mMrkOpn;
    private List<LatLng> mLngLine1;
    private List<LatLng> mLngLine2;

    private EditText mEditText;
    private TextView mTextView;
    private Button mButton;

    private Marker mMarker1, mMarker2;
    private Polyline  mPolylineGaude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mView = (View)findViewById(R.id.map);

        mEditText = (EditText) findViewById(R.id.edit_text);
        mTextView = (TextView) findViewById(R.id.tv_markers);
        mMarkers = new ArrayList<>();
        mMrkOpn = new ArrayList<>();
        mLngLine1 = new ArrayList<>();
        mLngLine2 = new ArrayList<>();

        mButton = (Button) findViewById(R.id.button_map);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMarker1 != null && mMarker2 != null){
                    gaudeLine();
                }

//                try {
//                    goLocation(view);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });

        mButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                addMarkers();
                Toast.makeText(MapsActivity.this, "nLongClick", Toast.LENGTH_SHORT).show();
                return false;
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

            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;//اطلاعات رو با حالت شیشه ای میندازه روی مپ
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info, null);

                    TextView tv_locality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tv_lat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tv_lng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView tv_snippet = (TextView) v.findViewById(R.id.tv_snippet);

                    LatLng ltn = marker.getPosition();

                    tv_locality.setText(marker.getTitle());
                    tv_lat.setText("Latitude: " + ltn.latitude);
                    tv_lng.setText("Longitude: " + ltn.longitude);
                    tv_snippet.setText(marker.getSnippet());

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

            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Toast.makeText(MapsActivity.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                    if(mMarker1 == null){
                        mMarker1 = marker;
                        mTextView.setText("Station first: " + marker.getTitle());
                    }else if(mMarker2 == null){
                        mMarker2 = marker;
                        mTextView.setText(mTextView.getText() + " Station sec: " + marker.getTitle());
                        gaudeLine();
//                        drawLine();
                    } else {
                        mMarker2 = null;
                        mMarker1 = marker;
                        mTextView.setText("Station first: " + marker.getTitle());
                        mPolylineGaude.remove();
                    }
                    return false;
                }
            });
        }

        goToLocationZoom(35.6873796, 51.3933181, 15);

    }

    private void drawLine() {
        PolylineOptions plo = new PolylineOptions()
                .add(mMarker1.getPosition())
                .add(mMarker2.getPosition())
                .color(Color.DKGRAY)
                .width(3);

        mGoogleMap.addPolyline(plo);
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
                .snippet("I am here");
        Marker marker = mGoogleMap.addMarker(mark);
        mMarkers.add(marker);
    }

    public void dataFace() {
        double zd = 0, za = 0;
        for (int i = 0; i < 11; i++) {
            if (i < 4) {
                zd = 0.001 * i;
            } else if (i < 7) {
                zd = 0.05 + 0.001 * (i - 4);
                za = 0.01;
            } else {
                zd = 0.001 * (i - 4);
                za = 0.02;
            }

            MarkerOptions mark = new MarkerOptions()
                    .title("location " + (i + 1))
                    .visible(false)
                    .position(new LatLng(35.6873796880 + zd, 51.393318101763 + za))
                    .snippet("Busy");
            mMrkOpn.add(mark);
        }
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
                    .title(title[i])//.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.fromResource(R.string.app_name)));
                    .position(new LatLng(lats[i], lngs[i]))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.train))
                    .snippet(vaz[i]);
            mMarkers.add(mGoogleMap.addMarker(mark));
        }

        addLine(mLngLine1, 1);
        addLine(mLngLine2, 2);
    }

    public void addLine(List<LatLng> ml, int numLine) {
        PolylineOptions plo = new PolylineOptions()
                .width(15);

        if (numLine == 1) {
            plo.color(Color.RED);
        } else if (numLine == 2) {
            plo.color(Color.BLUE);
        }

        for (int i = 0; i < ml.size(); i++) {
            plo.add(ml.get(i));
        }

        mGoogleMap.addPolyline(plo);
//        gaudeLine();
    }

    public static final int PATTERN_DASH_LENGTH_PX = 20;
    public static final int PATTERN_GAP_LENGTH_PX = 20;
    public static final PatternItem DOT = new Dot();
    public static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    public static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
    public static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

    private void drawDashedLeg(GoogleMap googleMap, List<LatLng> m) {
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(ContextCompat.getColor(this, R.color.colorAccent));
        polyOptions.addAll(m);
        polyOptions.pattern(PATTERN_POLYGON_ALPHA);
        mPolylineGaude = mGoogleMap.addPolyline(polyOptions);
    }

    public void gaudeLine() {
        PolylineOptions plo = new PolylineOptions()
                .width(15);

        plo.color(Color.GREEN);

        plo.add(mLngLine1.get(0));
        plo.add(mLngLine1.get(1));
        plo.add(mLngLine2.get(2));
        plo.add(mLngLine2.get(3));
        plo.add(mLngLine2.get(4));

        plo.pattern(PATTERN_POLYGON_ALPHA);

        for (int i = 0; i < mMarkers.size(); i++){
            if (mMarkers.get(i).getPosition().latitude == mLngLine1.get(0).latitude &&
                    mMarkers.get(i).getPosition().longitude == mLngLine1.get(0).longitude){
                Toast.makeText(this, "begin: " + mMarkers.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                mMarkers.get(i).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_green));
            }else if (mMarkers.get(i).getPosition().latitude == mLngLine1.get(4).latitude &&
                    mMarkers.get(i).getPosition().longitude == mLngLine1.get(4).longitude){
                Toast.makeText(this, "end: " + mMarkers.get(i).getTitle(), Toast.LENGTH_SHORT).show();
                mMarkers.get(i).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_red));
            }
        }

//        mMarker1 = mGoogleMap.addMarker(new MarkerOptions().position(mLngLine1.get(0))
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_green)));
//
//        mMarker2 = mGoogleMap.addMarker(new MarkerOptions().position(mLngLine1.get(4))
//                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_red)));


        mPolylineGaude = mGoogleMap.addPolyline(plo);
    }

    public void gaudeLine(List<LatLng> gaude) {
        PolylineOptions plo = new PolylineOptions()
                .width(15);

        plo.color(Color.RED);

        for (int i = 0; i < gaude.size(); i++){
            plo.add(mLngLine1.get(i));
        }

        mMarker1 = mGoogleMap.addMarker(new MarkerOptions().position(mLngLine1.get(0))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_green)));

        mMarker2 = mGoogleMap.addMarker(new MarkerOptions().position(mLngLine1.get(gaude.size()-1))
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.mark_red)));

        mPolylineGaude = mGoogleMap.addPolyline(plo);
    }
}
