package pl.jolantawojcik.grafikfitnessteam;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Contact extends Activity {

    private MapFragment mapFragment;
    private GoogleMap map;
    private static final LatLng LOCATION = new LatLng(50.0891729, 19.957589);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.addMarker(new MarkerOptions().position(LOCATION)
                //  .icon(BitmapDescriptorFactory.fromResource(R.drawable.logomarker)));
                .title("Fitness Team").snippet("Adres Fitness Team"));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(LOCATION)
                .zoom(17).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

    }
}
