package pl.jolantawojcik.grafikfitnessteam;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Contact extends Activity {

    private MapFragment mapFragment;
    private GoogleMap map;
    private static final LatLng LOCATION = new LatLng(50.09120, 19.95951);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.addMarker(new MarkerOptions().position(LOCATION)
                .title("Fitness Team").snippet("Adres Fitness Team"));

    }
}
