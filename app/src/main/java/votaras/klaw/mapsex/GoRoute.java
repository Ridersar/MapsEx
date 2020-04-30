package votaras.klaw.mapsex;

import android.app.Activity;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.transport.Transport;
import com.yandex.mapkit.transport.TransportFactory;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent; //подключаем класс Intent
import android.os.Bundle;
import android.view.View; // подключаем класс View для обработки нажатия кнопки
import android.widget.Button;
import android.widget.EditText; // подключаем класс EditText
import android.location.Geocoder;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class GoRoute extends Activity {
    /**
     * Замените "your_api_key" валидным API-ключом.
     * Ключ можно получить на сайте https://developer.tech.yandex.ru/
     */
    private final String MAPKIT_API_KEY = "dee9673a-5e58-4c07-b5ee-efa512c14a95";
    private final Point TARGET_LOCATION = new Point(51.531644, 46.006030);
    private final Point point1 = new Point(51.531503, 46.004495);
    private final Point point2 = new Point(51.527569, 46.011533);
    private final Point point3 = new Point(51.525753, 46.010434);
    private final Point point4 = new Point(51.520771, 46.002646);
   private Point p1,p2;


    private final Point MagentaRoute1 = new Point(51.528021,46.021923);
    private final Point MagentaRoute2 = new Point(51.522136,46.018522);
    private final Point GreenRoute1 = new Point(51.525276, 45.994920);
    private final Point GreenRoute2 = new Point(51.528515, 46.000392);
    private final Point pointBlue1 = new Point(51.541023, 46.012058);
    private final Point pointBlue2 = new Point(51.544455, 46.014134);


    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;
    private Button btn_create;
    //попытка геокода
  /*  public Point getLocationFromAddress(String strAddress) throws IOException {

      Geocoder coder = new Geocoder(this);
        List<Address> address;
        Point p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new Point((double) (location.getLatitude() * 1E6),
                    (double) (location.getLongitude() * 1E6));


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p1;
    }*/
    public void routeBetween(double longitude, double latitude, double lat, double lon){
        Transport transport = TransportFactory.getInstance();
        PedestrianRouter router = transport.createPedestrianRouter();
        ArrayList<RequestPoint> requests = new ArrayList<>();
        requests.add(new RequestPoint(new Point(latitude, longitude), RequestPointType.WAYPOINT, ""));
        requests.add(new RequestPoint(new Point(lat, lon), RequestPointType.WAYPOINT, ""));
        router.requestRoutes(requests, new TimeOptions(), new Session.RouteListener() {
            @Override
            public void onMasstransitRoutes(@NonNull List<com.yandex.mapkit.transport.masstransit.Route> list) {
                System.out.println(list.toString());
                if(!list.isEmpty()){
                    mapView.getMap().getMapObjects().clear();
                }
                for(Route route: list){
                    mapView.getMap().getMapObjects().addPolyline(list.get(0).getGeometry());
                }
            }

            @Override
            public void onMasstransitRoutesError(@NonNull Error error) {
                System.out.println(error);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Задайте API-ключ перед инициализацией MapKitFactory.
         * Рекомендуется устанавливать ключ в методе Application.onCreate,
         * но в данном примере он устанавливается в activity.
         */
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
        MapKitFactory.setLocale("ru_RU");
        /**
         * Инициализация библиотеки для загрузки необходимых нативных библиотек.
         * Рекомендуется инициализировать библиотеку MapKit в методе Activity.onCreate
         * Инициализация в методе Application.onCreate может привести к лишним вызовам и увеличенному использованию батареи.
         */
        MapKitFactory.initialize(this);
        // Создание MapView.
        setContentView(R.layout.activity_go_route);
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);

        // Перемещение камеры в центр Санкт-Петербурга.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        //animationHandler = new Handler();

        //получение информации из предыдущего activity
        Intent intent = getIntent();
        String A = intent.getStringExtra(ActivityDisplayMessage.A_str);
        String B = intent.getStringExtra(ActivityDisplayMessage.B_str);


        ArrayList<RequestPoint> requests = new ArrayList<>();
        //Geocoder geocoder = new Geocoder(getApplicationContext());
        //List<Address> addresses;
        //ArrayList<Point> polylinePoints = new ArrayList<>(); //создание списка точек

//попытка геокодирования
     /*   try {
            addresses = geocoder.getFromLocationName(A, 1);
            addresses=geocoder.getFromLocationName(B,1);
            if(addresses.size() > 0) {
                double latitude= addresses.get(0).getLatitude();
                double longitude= addresses.get(0).getLongitude();
                double latitude1= addresses.get(1).getLatitude();
                double longitude1= addresses.get(1).getLongitude();
               // polylinePoints.add(new Point(latitude, longitude)); //добавление точки в список
                //polylinePoints.add(new Point(latitude1,longitude1)); //добавление точки в список
                p1=new Point(latitude, longitude);
                p2=new Point(latitude1, longitude1);
            }

        } catch (IOException e) {
           // Toast.makeText(GoRoute.this,"Sorry, can you select again?", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }*/
        //PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints)); //отрисовка маршрута по точкам списка
        //polyline.setStrokeColor(Color.RED);

        //конвертирование в int
        double aa=Double.parseDouble(A);
        double bb=Double.parseDouble(B);
        //int a = Integer.parseInt(A); //начало
        //int b = Integer.parseInt(B); //конец
        mapView.getMap().getMapObjects().addPlacemark(new Point(aa, bb));
        try{
            routeBetween(aa,bb,51.517547, 46.010487);
        }catch (NullPointerException e){
            System.out.println("GPS is off");
        }
        //ArrayList<OpPoint> mas = new ArrayList<OpPoint>(); //список опорных точек
        //Route.createPoints(mas); //создание объектов
        //Route.drawRoute(mas); //отрисовка маршрутов

       // ArrayList<Point> route = Route.searchRoute(mas, a, b); //маршрут

        //PolylineMapObject polylineRoute = mapObjects.addPolyline(new Polyline(route)); //отрисовка маршрута по точкам списка
        //polylineRoute.setStrokeColor(Color.RED);
         //createMapObjects();
    }

    @Override
    protected void onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    //создание объектов на карте
    private void createMapObjects() {
        ArrayList<Point> polylinePoints = new ArrayList<>(); //создание списка точек
        polylinePoints.add(p1);
        polylinePoints.add(p2);
      /*  polylinePoints.add(new Point(point1.getLatitude(), point1.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point2.getLatitude(), point2.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point3.getLatitude(), point3.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point4.getLatitude(), point4.getLongitude())); //добавление точки в список
        ArrayList<Point> polylinePointsAlena = new ArrayList<>(); //создание списка точек
        polylinePointsAlena.add(new Point(pointBlue1.getLatitude(), pointBlue1.getLongitude())); //добавление точки в список
        polylinePointsAlena.add(new Point(pointBlue2.getLatitude(), pointBlue2.getLongitude())); //добавление точки в список
*/

        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints)); //отрисовка маршрута по точкам списка
        polyline.setStrokeColor(Color.RED);
  //      PolylineMapObject polylineAlena = mapObjects.addPolyline(new Polyline(polylinePointsAlena)); //отрисовка маршрута по точкам списка
    //    polylineAlena.setStrokeColor(Color.BLUE);
        //polyline.setZIndex(100.0f);

        ArrayList<Point> polylinePointsMagenta = new ArrayList<>();
        polylinePointsMagenta.add(new Point(MagentaRoute1.getLatitude(), MagentaRoute1.getLongitude())); //добавление точки в список
        polylinePointsMagenta.add(new Point(MagentaRoute2.getLatitude(), MagentaRoute2.getLongitude())); //добавление точки в список
        PolylineMapObject polylineMagenta = mapObjects.addPolyline(new Polyline(polylinePointsMagenta)); //отрисовка маршрута по точкам списка
        polylineMagenta.setStrokeColor(Color.MAGENTA);

        ArrayList<Point> polylinePointsGreen = new ArrayList<>();
        polylinePointsGreen.add(new Point(GreenRoute1.getLatitude(), GreenRoute1.getLongitude())); //добавление точки в список
        polylinePointsGreen.add(new Point(GreenRoute2.getLatitude(), GreenRoute2.getLongitude())); //добавление точки в список
        PolylineMapObject polylineGreen = mapObjects.addPolyline(new Polyline(polylinePointsGreen)); //отрисовка маршрута по точкам списка
        polylineGreen.setStrokeColor(Color.GREEN);

    }




}