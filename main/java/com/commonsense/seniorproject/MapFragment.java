package com.commonsense.seniorproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    Location loc;

    MapView mapView;

    ArrayList<Place> places = new ArrayList<>();
    double latitude;
    double longitude;
    // private FragmentActivity fragContext;
    Location lastLocation;

    boolean markers = false;


    boolean checkGPS = false;
    boolean checkNetwork = false;
    boolean canGetLocation = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    private static GoogleMap mMap;

    private EditText mSearchText;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        // get the reference of Button\
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
            getPlaces(loc.getLatitude(), loc.getLongitude());
        }
        //get reference to text editor
        mSearchText = (EditText) view.findViewById(R.id.input_search);
        //get reference to search button
        Button btn = view.findViewById(R.id.search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on search button press, get locations at input and set them as markers
                geoLocate();
                setMarkers();
            }
        });
        //get reference to target button
        Button btn2 = view.findViewById(R.id.target);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on target button press, get locations at user location and set them as markers
                onMapReady(mMap);
            }
        });
        //get reference to help button
        Button btn3 = view.findViewById(R.id.help);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on help button press, get MapPopUpHelp class as a pop up activity
                Intent intent = new Intent(getActivity(), MapPopUpHelp.class);
                startActivity(intent);
            }
        });
        //initialize keycode for enter
        init();
        // check or ask for user location permissions
        getPermissions();
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); //this is important
        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //saveMap(googleMap);
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(getActivity(), permissions, 99);
        getPermissions();
        // if permission is enabled set view and map markers at users current location else set view at default location and getPlaces for the default location
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.getUiSettings().setZoomControlsEnabled(true);
                loc = getLocation();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 12));
                getCompleteAddressString(loc.getLatitude(), loc.getLongitude());
                setMarkers();
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            return;
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.8, -75), 13));
            getPlaces(39.8, -75);
            setMarkers();
        }
        init();
        //mMap.setMyLocationEnabled(true);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //Enter something which is currently postal code and return list of places from database
    //maybe more efficient if every address in db has longitude and latitude and returns a list of all address in a range of
    //that input longitude and latitude.
    public void getPlaces(double lat, double lng) {
        String tag_string_req = "req_news";
        Log.e("BEFORE REQUEST", "CHECK");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest strReq = new JsonArrayRequest(Request.Method.POST, CommonSenseConfig.URL_LOC, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("AFTER REQUEST", "CHECK");
                Log.e("onResponse", "news response: " + response);

                try {
                    //Response Array from Database
                    JSONArray locations = response;
                    JSONObject jobj;
                    ArrayList<Place> places = new ArrayList<>();
                    //Iterates through each JSON object in the array
                    //turns each JSON into a place in the ArrayList
                    for (int i = 0; i < locations.length(); i++) {
                        jobj = locations.getJSONObject(i);
                        Log.d("DatabaseLoc", jobj.getString("Name"));
                        //TODO: Get the places actually in the ArrayList


                        places.add(new Place(jobj.getString("Name"), jobj.getString("Address"), jobj.getDouble("Latitude"), jobj.getDouble("Longitude"), jobj.getString("Type"), jobj.getString("Treated Date"), jobj.getString("Renewal Date")));

                    }
                    storePlaces(places);


                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(view.getContext(), "JSON error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "Login error: " + error.getMessage());
                //Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to news url
                Map<String, String> params = new HashMap<String, String>();

                //params.put("userID",session.getUserID());

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    //get address from longitude and latitude to get list of places
    @SuppressLint("LongLogTag")
    private void getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                getPlaces(returnedAddress.getLatitude(), returnedAddress.getLongitude());
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current loction address", strReturnedAddress.toString());
            } else {
                Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current loction address", "Cannot get Address!");
        }
    }

    private void storePlaces(ArrayList<Place> places) {
        this.places = places;
    }

    //place markers on the map
    private void setMarkers() {

        mMap.clear();//clear old markers

        // loop through each place retrieve from the Database
        for (Place place: places
        ) {
            if((!(place.getName().equals(null))) && (!(place.getAddress().equals(null)))) {
                // if place is treated set marker using custom bitmap at that location
                if(place.getType().equals("Treated")) {
                    Bitmap bitmapOut = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.desinfectant), 60, 60, false);
                    mMap.addMarker(new MarkerOptions().position(geoLocate(place.getAddress())).title(place.getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmapOut)).snippet("Expiration: " + place.getRenewalDate()));
                }
                // if place is sales set marker using custom bitmap at that location
                else if (place.getType().equals("Sales")) {
                    Bitmap bitmapOut = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.shoppingcart), 60, 60, false);
                    mMap.addMarker(new MarkerOptions().position(geoLocate(place.getAddress())).title(place.getName()).icon(BitmapDescriptorFactory.fromBitmap(bitmapOut)));
                }
            }
        }
        markers = true;
    }



    //takes input from user and geolocate than grab long/lat from output
    private void geoLocate(){
        Log.d("MapFragment", "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("MapFragment", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d("MapFragment", "geoLocate: found a location: " + address.toString());
            Log.d("MapFragment", "postal: " + address.getPostalCode());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            hideSoftKeyboard();// hide keyboard after geolocation on the input
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(address.getLatitude(), address.getLongitude()), 12));
            getPlaces(address.getLatitude(), address.getLongitude());

        }
    }

    //takes input from user and geolocate than grab long/lat from output
    private LatLng geoLocate(String place){
        Log.d("MapFragment", "geoLocate: geolocating");

        String searchString = place;

        LatLng placeLatLng = new LatLng(80, 80);

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e("MapFragment", "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d("MapFragment", "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();
            hideSoftKeyboard();// hide keyboard after geolocation on the input
            placeLatLng = new LatLng(address.getLatitude(), address.getLongitude());
            return placeLatLng;
        }
        return placeLatLng;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    
    //Ask for location permissions
    private void getPermissions(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                //  try again to request the permission.
            } else {
                // request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        } else {
            // Permission has already been granted
        }
    }

    //Run geolocation if enter is pressed
    private void init(){
        Log.d("MapFragment", "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    //execute our method for geolocation
                    geoLocate();
                }

                return false;
            }
        });
    }

    //hide the soft keyboard
    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mapView.getWindowToken(),0);
    }

    //get users location
    private Location getLocation() {
        try {
            locationManager = (LocationManager) getActivity()
                    .getSystemService(getActivity().LOCATION_SERVICE);

            // get GPS status
            checkGPS = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // get network provider status
            checkNetwork = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!checkGPS && !checkNetwork) {
                Toast.makeText(getActivity(), "No Service Provider is available", Toast.LENGTH_SHORT).show();
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // request permissions check
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    if (locationManager != null) {
                        loc = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null) {
                            latitude = loc.getLatitude();
                            longitude = loc.getLongitude();
                        }
                    }


                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return loc;
    }

    //get longitude
    public double getLongitude() {
        if (loc != null) {
            longitude = loc.getLongitude();
        }
        return longitude;
    }

    //get Latitude
    public double getLatitude() {
        if (loc != null) {
            latitude = loc.getLatitude();
        }
        return latitude;
    }

    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}