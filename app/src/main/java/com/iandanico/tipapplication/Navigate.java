package com.iandanico.tipapplication;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by nekomarino on 9/3/17.
 */

public class Navigate extends Fragment implements GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        FragmentCompat.OnRequestPermissionsResultCallback, View.OnClickListener{

    View myView;
    GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Geocoder geocoder;
    Button navToTip,navToBldg5,navToBldg6,navToPeCenter,navToAnnivHall,navToBldg9,navToStudyArea;
    Button navToBldg1,navToBldg3,navToBldg4,navToBldg8;
    TextToSpeech tts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.navigate, container, false);

        navToTip = (Button) myView.findViewById(R.id.nav_to_tip);
        navToBldg6 = (Button) myView.findViewById(R.id.nav_to_bldg6);
        navToPeCenter = (Button) myView.findViewById(R.id.nav_to_peCenter);
        navToAnnivHall = (Button) myView.findViewById(R.id.nav_to_annivHall);
        navToBldg5 = (Button) myView.findViewById(R.id.nav_to_bldg5);
        navToBldg9 = (Button) myView.findViewById(R.id.nav_to_bldg9);
        navToStudyArea  = (Button) myView.findViewById(R.id.nav_to_studyArea);
        navToBldg1 = (Button) myView.findViewById(R.id.nav_to_bldg1);
        navToBldg3 = (Button) myView.findViewById(R.id.nav_to_bldg3);
        navToBldg4 = (Button) myView.findViewById(R.id.nav_to_bldg4);
        navToBldg8 = (Button) myView.findViewById(R.id.nav_to_bldg8);

        navToTip.setOnClickListener(this);
        navToBldg6.setOnClickListener(this);
        navToPeCenter.setOnClickListener(this);
        navToAnnivHall.setOnClickListener(this);
        navToBldg5.setOnClickListener(this);
        navToBldg9.setOnClickListener(this);
        navToStudyArea.setOnClickListener(this);
        navToBldg1.setOnClickListener(this);
        navToBldg3.setOnClickListener(this);
        navToBldg4.setOnClickListener(this);
        navToBldg8.setOnClickListener(this);

        tts=new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.UK);
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());
        return myView;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);


        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onClick(View view) {
        Log.d("meh", "onClick: "+view.toString());
        mMap.clear();
        LatLng destination = new LatLng(0,0);
        int id = view.getId();
        if(id == R.id.nav_to_tip){
            destination = new LatLng(14.626850,121.061416);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to TIP-QC",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to T.I.P. - Quezon City", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg6){
            destination = new LatLng(14.626369, 121.061649);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Bldg 6",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building Six", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_peCenter){
            destination = new LatLng(14.625738, 121.061766);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to PE Center",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to P.E. Center ", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_annivHall){
            destination = new LatLng(14.626067, 121.062158);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Anniversary Hall",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Anniversary Hall", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg5){
            destination = new LatLng(14.626229, 121.062523);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to BLDG 5",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building Five", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg9){
            destination = new LatLng(14.626230, 121.062144);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to BLDG 9",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building Nine", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_studyArea){
            destination = new LatLng(14.625420, 121.062144);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(getDirectionsUrl(destination));
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Study Area",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Study Area", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg1){
            destination = new LatLng(14.624964, 121.061552);
            drawOnMap(destination);
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Bldg 1",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building 1", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg3){
            destination = new LatLng(14.624892, 121.062771);
            drawOnMap(destination);
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Bldg 3",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building 3", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg4){
            destination = new LatLng(14.625134, 121.062832);
            drawOnMap(destination);
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Bldg 4",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building 4", TextToSpeech.QUEUE_FLUSH, null);
        }
        if(id == R.id.nav_to_bldg8){
            destination = new LatLng(14.625146, 121.062390);
            drawOnMap(destination);
            Toast.makeText(getActivity().getApplicationContext(),"Directions to Bldg 8",Toast.LENGTH_SHORT).show();
            tts.speak("Directions to Building 8", TextToSpeech.QUEUE_FLUSH, null);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude()),13     ));

    }

    void drawOnMap(LatLng dest){
        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude()), dest)
                .width(5)
                .color(Color.GREEN));
    }



    private String downloadUrl(String strUrl) throws IOException {
        Log.d("meh", "downloadUrl: "+strUrl);
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("not","Exception while downloading url"+ e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.GREEN);
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
        }
    }


    private String getDirectionsUrl(LatLng dest){
        String str_origin = "origin="+mMap.getMyLocation().getLatitude()+","+ mMap.getMyLocation().getLongitude();
        String str_dest = "destination="+Double.toString(dest.latitude)+","+Double.toString(dest.longitude);
        String sensor = "sensor=false";
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }

//    @Override
//    public void onClick(View view) {
//        Log.d("wat", "onClick: "+view.toString());
//    }
}
