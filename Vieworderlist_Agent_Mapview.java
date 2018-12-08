package my.project.agrim;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Vieworderlist_Agent_Mapview extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Details_Agent_Pending_Delivery_Mapview> coords = null;
    String Name = null;
    Double Latittude, Longitude = 0.0;
    Marker mk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworderlist_agent_mapview);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        coords = new ArrayList<Details_Agent_Pending_Delivery_Mapview>();

        Intent ii = getIntent();
        coords = ii.getParcelableArrayListExtra("Map");
        Toast.makeText(this,"Num of Deliveries: "+coords.size(),Toast.LENGTH_LONG).show();
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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for (Integer i = 0;i<coords.size();i++)
        {
            Latittude = coords.get(i).getShop_Latittude();
            Longitude = coords.get(i).getShop_Longitude();
            Name = coords.get(i).getBuyer_Name();

            LatLng shop = new LatLng(Latittude, Longitude);
//            mMap.addMarker(new MarkerOptions().position(shop).title(Name));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(shop));
            MarkerOptions marker = new MarkerOptions().title(Name).position(shop);
            mMap.addMarker(marker);
        }
    }
}
