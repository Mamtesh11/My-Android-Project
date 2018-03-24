package com.example.coed.attendancebolo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GetAndSendLocation extends AppCompatActivity implements LocationListener{

    String ServerURL = "https://mamteshkumar111.000webhostapp.com/get_data.php" ;
    Button LocButton, sendButton;
    String username;
    String password;
    Double longitude = 0.0;
    Double latitude = 0.0;
    public static final String EXTRA_USERNAME = "userNew";
    public static final String EXTRA_PASS = "passNew";
    public static final String EXTRA_LONGI = "longitudrwe";
    public static final String EXTRA_LATI = "latirdfs";
    TextView locationText;
    TextView waitForResponse;
    TextView vallidInvalid;


    static String  phpData;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_get_and_send_location);

        LocButton = (Button)findViewById(R.id.getLocationBtn);
        sendButton = (Button)findViewById(R.id.sendLocationBtn);
        Intent intent = getIntent();
        username = intent.getStringExtra(MainActivity.EXTRA_USERNAME);
        password = intent.getStringExtra(MainActivity.EXTRA_PASSWORD);

        locationText = (TextView)findViewById(R.id.locationText);
        waitForResponse = (TextView)findViewById(R.id.waitForResponse);
        vallidInvalid  = (TextView)findViewById(R.id.validNinvalid);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        LocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(latitude == 0.0 && longitude == 0.0){
                    Toast.makeText(GetAndSendLocation.this,"First get the Location", Toast.LENGTH_LONG).show();
                }
                else{

                    InsertData(username, password, longitude, latitude);
                    waitForResponse.setText("Wait For Response.....");

                }
            }
        });

    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        /*try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }*/

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(GetAndSendLocation.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void InsertData(final String username, final String password, final Double longitude, final Double latitude){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = username ;
                String EmailHolder = password ;
                String longitudeHolder = longitude.toString();
                String latituteHolder = latitude.toString();


                List<NameValuePair> nameValuePairs;
                nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("pass", EmailHolder));
                nameValuePairs.add(new BasicNameValuePair("lati", longitudeHolder));
                nameValuePairs.add(new BasicNameValuePair("longi", latituteHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ServerURL);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();

                    InputStream is = httpEntity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8); // From here you can extract the data that you get from your php file..

                    StringBuilder sb = new StringBuilder();

                    String line = null;

                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }

                    is.close();
                    phpData="";
                    phpData = sb.toString(); // Here you are converting again the string into json object. So that you can get values with the help of keys that you send


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                vallidInvalid.setText(phpData);



            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(username, password, longitude.toString(), latitude.toString());
    }

}
