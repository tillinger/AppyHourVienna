package is.ru.happyhour;

import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import is.ru.happyhour.model.HappyHour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Peter on 27.10.2014.
 */
public class MyMapFragment extends com.google.android.gms.maps.MapFragment {

    private HappyHour happyHour;

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();
        if (args != null) { //TODO change extra name
            this.happyHour = ((HappyHour) args.getSerializable(MainActivity.HAPPYHOUR_EXTRA));
        }

        //if latitude and longitude are not calculated at the moment!
        getAndSaveCoordinatesOfHappyHour();

        // create marker and add to the map
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(happyHour.getAddress().getLatitude(), happyHour.getAddress().getLongitude()))
                .title(happyHour.getName())
                .snippet("Next Happy Hour: TODAY!");
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        getMap().addMarker(marker).showInfoWindow();

        //first zoom to Vienna without any animation
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(48.208663, 16.372761), 10));

        //zoom to the happyhour after a little bit of time!
        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                //zoom to the location in the map
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(happyHour.getAddress().getLatitude(), happyHour.getAddress().getLongitude()))
                        .zoom(15).build();
                getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }, 2000);
    }

    //get lattitude and longitude if not specified yet
    //TODO update database
    //TODO is not working!
    private void getAndSaveCoordinatesOfHappyHour() {
        if(this.happyHour.getAddress().getLatitude() == 0) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.GERMAN);

            List<Address> addresses = new ArrayList<Address>();
            try {
                addresses = geocoder.getFromLocationName(
                        happyHour.getAddress().getPostcode() + " Vienna, " +
                        happyHour.getAddress().getAddress(), 1);
            } catch (IOException e) {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }

            if(addresses.size() > 0) {
                happyHour.getAddress().setLongitude(addresses.get(0).getLongitude());
                happyHour.getAddress().setLatitude(addresses.get(0).getLatitude());
                //TODO update to db
            }
        }
    }
}