package votaras.klaw.mapsex;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.List;

public class GPS {
    Context m_context;
    Location m_location;
    LocationManager m_locationManager;
    String m_provider = LocationManager.GPS_PROVIDER;

    GPS(Context context) {
        m_context = context;
        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        m_locationManager.requestLocationUpdates(m_provider, 0, 10, new LocationListener() {
            public void onLocationChanged(Location location) {
                m_location = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            public void onProviderEnabled(String provider) {

            }

            public void onProviderDisabled(String provider) {

            }
        });
    }

    Location getLocation() {
        if (m_location == null)
            if (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
                m_location = m_locationManager.getLastKnownLocation(m_provider);


        if (m_location == null) {
            m_location = new Location(m_provider);
            m_location.setLatitude(0);
            m_location.setLongitude(0);
            //m_location.setLatitude(51.517547);
           // m_location.setLongitude(46.010487);
            m_location.setAltitude(0);
        }
        return m_location;
    }

   /* public Location getLastKnownLocation(Context context) {
        m_locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = m_locationManager.getProviders(true);
        Location bestLocation = null;
        if (m_location == null)
            if (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
        for (String provider : providers) {
            Location l = m_locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }*/
}