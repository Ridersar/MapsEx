/*package votaras.klaw.mapsex;
//этот код - не мой, предлагается с помощью одной переменной всегда определять местоположение.
//для кнопки - хорошее решение, но вести по маршруту с этим не реально
//попробуй на реальном устройстве

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

class MyLocationListener implements LocationListener {

    static Location imHere; // здесь будет всегда доступна самая последняя информация о местоположении пользователя.

    public static void SetUpLocationListener(Context context) // это нужно запустить в самом начале работы программы
    {
        LocationManager locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000,
                10,
                locationListener); // здесь можно указать другие более подходящие вам параметры

        imHere = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onLocationChanged(Location loc) {
        imHere = loc;
    }
    @Override
    public void onProviderDisabled(String provider) {}
    @Override
    public void onProviderEnabled(String provider) {}
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}
*/
//добавить в GoRoute
//MyLocationListener.SetUpLocationListener(this);
//тогда у нас всегда и в любой части нашего проекта есть переменная MyLocationListener.imHere типа Location,
// в которой хранится самое последнее местоположение пользователя и множество
// дополнительной информации, как например скорость или точность определения местоположения.