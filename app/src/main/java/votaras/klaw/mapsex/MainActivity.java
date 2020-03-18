package votaras.klaw.mapsex;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;

import java.util.ArrayList;

public class MainActivity extends Activity {
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
<<<<<<< HEAD
    private final Point point5 = new Point(51.541771, 46.032646);

=======
    
>>>>>>> 5dcbceb8279d62ac0b25d952617f4f55735673e5
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;

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
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);

        // Перемещение камеры в центр Санкт-Петербурга.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 5),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        //animationHandler = new Handler();
        createMapObjects();
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
        polylinePoints.add(new Point(point1.getLatitude(), point1.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point2.getLatitude(), point2.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point3.getLatitude(), point3.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point4.getLatitude(), point4.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point5.getLatitude(), point5.getLongitude())); //добавление точки в список

        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints)); //отрисовка маршрута по точкам списка
        polyline.setStrokeColor(Color.RED);
        //polyline.setZIndex(100.0f);
    }
}