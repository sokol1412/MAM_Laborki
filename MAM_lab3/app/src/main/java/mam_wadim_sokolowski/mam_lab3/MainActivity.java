package mam_wadim_sokolowski.mam_lab3;

import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class MainActivity extends AppCompatActivity implements SensorsChangedListener {

    private List<POI> POIList;
    private CurrentLocation currentLocation;
    private double mMyLatitude = 0;
    private double mMyLongitude = 0;


    private EditText POIName;
    private EditText POIDescription;
    private EditText POILatitude;
    private EditText POILongitude;
    private TextView closestPOIInfo;

    private Button addPOIButton;
    private Button findClosestPOIButton;

    private ImageView arrowImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        POIList = new ArrayList<POI>();
        addDefaultPOIs();
        currentLocation = new CurrentLocation(this);
        currentLocation.buildGoogleApiClient(this);
        currentLocation.start();

        POIName = (EditText) findViewById(R.id.POIName);
        POIDescription = (EditText) findViewById(R.id.POIDescription);
        POILatitude = (EditText) findViewById(R.id.POILatitude);
        POILongitude = (EditText) findViewById(R.id.POILongitude);
        closestPOIInfo = (TextView) findViewById(R.id.closestPOIInfo);
        arrowImageView = (ImageView) findViewById(R.id.arrowImageView) ;

        addPOIButton = (Button) findViewById(R.id.addPOI);
        findClosestPOIButton = (Button) findViewById(R.id.findClosestPOI);


        addPOIButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addPOI();
            }
        });
        findClosestPOIButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findClosestsPOI();
            }
        });
    }

    private void addDefaultPOIs() {
        POIList.add(new POI(
                "London",
                "London center",
                51.509865,
                -0.118092));
        POIList.add(new POI(
                "Warsaw",
                "Warsaw center",
                52.237049,
                21.017532));
        POIList.add(new POI(
                "Helsinki",
                "Helsinki center",
                60.192059,
                24.945831));
        POIList.add(new POI(
                "Madrid",
                "Madrid center",
                40.416775,
                -3.703790));
        POIList.add(new POI(
                "Ottawa",
                "Ottawa center",
                45.425533,
                -75.692482));
    }

    private boolean POIAlreadyDefined(String POI) {
        for(POI p : POIList) {
            if(p != null && p.getPointName().equals(POI)) {
                return true;
            }
        }
        return false;
    }

    private void addPOI()
    {
        if (!POIAlreadyDefined(POIName.getText().toString()))
        {
            POIList.add(new POI(POIName.getText().toString(), POIDescription.getText().toString(), Double.parseDouble(POILatitude.getText().toString()), Double.parseDouble(POILongitude.getText().toString())));
            Toast.makeText(this, "New POI \"" + POIName.getText().toString() + "\" added!", Toast.LENGTH_LONG);
        }

    }


    private void rotateLocationArrow(float degree) {
        final RotateAnimation rotateAnim = new RotateAnimation(0.0f, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        arrowImageView.startAnimation(rotateAnim);
    }

    private void findClosestsPOI() {
        for (POI p : POIList) {
            float[] calculatedDistance = new float[3];
            Location.distanceBetween(mMyLatitude, mMyLongitude, p.getPointLat(), p.getPointLon(), calculatedDistance);
            double distanceInKMs = calculatedDistance[0] * 0.001;
            p.setDistanceTo(distanceInKMs);
        }
        POI closestPOI = Collections.min(POIList, POI.POI_COMPARATOR);
        Location myLocation = new Location(LocationManager.GPS_PROVIDER);
        Location closestPOILocation = new Location(LocationManager.GPS_PROVIDER);
        myLocation.setLatitude(mMyLatitude);
        myLocation.setLongitude(mMyLongitude);
        closestPOILocation.setLatitude(closestPOI.getPointLat());
        closestPOILocation.setLongitude(closestPOI.getPointLon());
        float bearingTo = myLocation.bearingTo(closestPOILocation);
        closestPOIInfo.setText("\t Closest POI found!"
                + "\n Name : " + closestPOI.getPointName()
                + "\n Description: " + closestPOI.getDescription()
                + "\n Distance to (in kilometers):  " + closestPOI.getDistanceTo()
                + "\n Bearing to (degrees): " + bearingTo);

        arrowImageView.setVisibility(View.VISIBLE);
        rotateLocationArrow(bearingTo);
    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        mMyLatitude = currentLocation.getLatitude();
        mMyLongitude = currentLocation.getLongitude();
    }
}
