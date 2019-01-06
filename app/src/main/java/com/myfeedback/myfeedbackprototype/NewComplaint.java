package com.myfeedback.myfeedbackprototype;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewComplaint extends AppCompatActivity implements LocationListener {
    Spinner spinner;
    public static String tvLongi;
    public static String tvLati;
    TextView tvLatitude;
    TextView tvLongitude;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcomplaint);

        CheckPermission();

        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //convert to actionbar
        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("New Complaint");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        spinner = findViewById(R.id.spinnerCategory);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public void onSubmit(View view) {
        //NOTE: 8 things need to submit to database
        //PHP variable => id(auto-increment), title, description, status, category, imageURL, gps, useremail
        String title, description, imageURL, gps, useremail, category, status;

        category = ((Spinner) findViewById(R.id.spinnerCategory)).getSelectedItem().toString();

        EditText et_title = findViewById(R.id.editTextTitle);
        title = String.valueOf(et_title.getText()).trim();

        EditText et_desc = findViewById(R.id.editTextDescription);
        description = String.valueOf(et_desc.getText()).trim();

        useremail = SharedPrefManager.getInstance(NewComplaint.this).getKeyUserEmail();

        ComplaintBackground complaintBackground = new ComplaintBackground(NewComplaint.this);
        complaintBackground.execute("complaint", title, description, "", category, "", "", useremail); //TO-DO
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Getting reference to TextView tv_longitude
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        // Getting reference to TextView tv_latitude
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);

        tvLongi = String.valueOf(location.getLongitude());
        tvLati = String.valueOf(location.getLatitude());

        // Setting Current Longitude
        tvLongitude.setText("Longitude:" + tvLongi);
        // Setting Current Latitude
        tvLatitude.setText("Latitude:" + tvLati);
    }


    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

}
