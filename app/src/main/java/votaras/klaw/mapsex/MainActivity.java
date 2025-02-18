package votaras.klaw.mapsex;

import android.Manifest;
import android.app.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;


import android.content.Intent; //подключаем класс Intent
import android.os.Bundle;
import android.view.View; // подключаем класс View для обработки нажатия кнопки
import android.widget.Button;
import android.widget.EditText; // подключаем класс EditText

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;





public class MainActivity extends Activity {
    /**
     * Замените "your_api_key" валидным API-ключом.
     * Ключ можно получить на сайте https://developer.tech.yandex.ru/
     */
    private final String MAPKIT_API_KEY = "dee9673a-5e58-4c07-b5ee-efa512c14a95";
    private final Point TARGET_LOCATION = new Point(51.531644, 46.006030);

    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;
    private Button btn_create, btn_statistics, btn_location;
    private int kol = 0;
    SQLiteDatabase myDB;
    GPS m_gps;
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
        addListenerOnButton();
        super.onCreate(savedInstanceState);
        mapView = (MapView) findViewById(R.id.mapview);
        m_gps=new GPS(this); //местополежние
        //создание / открытие базы данных
        myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);

        //создание базы данных
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (numberOfRoutes INT, flag INT)"
        );

        /*
        //заполнение БД
        ContentValues row1 = new ContentValues();
        row1.put("cur", 1);

        myDB.insert("user", null, row1);
        */

        // Перемещение камеры в центр Санкт-Петербурга.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 14.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.LINEAR, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        //animationHandler = new Handler();
        // createMapObjects();
        //createMapObjects(); //отрисовка объектов


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


    // Метод обработки нажатия на кнопку
    public void addListenerOnButton () {
        btn_create = (Button)findViewById(R.id.btnCreate);
        btn_create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       Intent intent = new Intent(".ActivityDisplayMessage");
                       startActivity(intent);
                    }
                }
        );


        btn_location = (Button)findViewById(R.id.btnLocation);
        btn_location.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Test", "Ok");


                        //МЕСТОПОЛОЖЕНИЕ
                        Location location=m_gps.getLocation();
                        Point my_gps=new Point(location.getLatitude(),location.getLongitude());
                        //Point my_gps= new Point(51.520771, 46.002646);
                        //mapView.getMap().getMapObjects().addPlacemark(my_gps);


                        // Перемещение камеры в текущее местоположение
                        mapView.getMap().move(
                                new CameraPosition(my_gps, 15.5f, 0.0f, 0.0f),
                                new Animation(Animation.Type.LINEAR, 0),
                                null);



                    }
                }
        );


        btn_statistics = (Button)findViewById(R.id.btnStatistics);
        btn_statistics.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        kol++;
                        String Skol = Integer.toString(kol);
                        Log.i("Zaxodi v statistiky", Skol);
                        Intent intent = new Intent(".Statistics");
                        startActivity(intent);


                        //тестирование БД!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                        /*
                        //заполнение БД


                        //Есть ли в базе данных что-то?
                        boolean fl = false;

                        Cursor cursor = myDB.query("user", new String[] { "numberOfRoutes",
                                        "flag"}, "flag" + " = "+1,
                                null, null, null, null, null);
                        if (cursor.getCount() > 0)
                            fl = true;

                        //Если база данных пуста - заполнение начальными значениями
                        if (fl == false)
                        {
                            ContentValues inf1 = new ContentValues();
                            inf1.put("numberOfRoutes", 1); //заполнение начальной информацией
                            inf1.put("flag", 1); //сигнал о том, что ячейка создана
                            myDB.insert("user", null, inf1);
                        }


                        //запрос
                        Cursor myCursor =
                                myDB.rawQuery("select numberOfRoutes, flag from user", null);

                        //проход по БД
                        int num = -111; //начальное значение, символизирует ошибку
                        while(myCursor.moveToNext())
                        {
                            num = myCursor.getInt(0);
                        }

                        //вывод
                        String Scur = Integer.toString(num);
                        Log.i("Baza dannix", Scur);

                        num++; //увеличение посещений ????????
                        ContentValues rowUp = new ContentValues();
                        rowUp.put("numberOfRoutes", num);
                        String where = "flag" + "=" + 1;
                        myDB.update("user", rowUp, where, null);
                        //myDB.update("user", rowUp, null, null);

                        myCursor =
                                myDB.rawQuery("select numberOfRoutes from user", null);

                        while(myCursor.moveToNext())
                        {
                            num = myCursor.getInt(0);
                        }
                        Scur = Integer.toString(num);
                        Log.i("Baza dannix2", Scur);


                        //myDB.isOpen()
                        //закрытие
                        myCursor.close();
                        cursor.close();
                        */
                    }
                }
        );
    }





    //создание объектов на карте
    private void createMapObjects() {
        /*
        ArrayList<Point> polylinePoints = new ArrayList<>(); //создание списка точек
        polylinePoints.add(new Point(point1.getLatitude(), point1.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point2.getLatitude(), point2.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point3.getLatitude(), point3.getLongitude())); //добавление точки в список
        polylinePoints.add(new Point(point4.getLatitude(), point4.getLongitude())); //добавление точки в список
        ArrayList<Point> polylinePointsAlena = new ArrayList<>(); //создание списка точек
        polylinePointsAlena.add(new Point(pointBlue1.getLatitude(), pointBlue1.getLongitude())); //добавление точки в список
        polylinePointsAlena.add(new Point(pointBlue2.getLatitude(), pointBlue2.getLongitude())); //добавление точки в список

        PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints)); //отрисовка маршрута по точкам списка
        polyline.setStrokeColor(Color.RED);
        PolylineMapObject polylineAlena = mapObjects.addPolyline(new Polyline(polylinePointsAlena)); //отрисовка маршрута по точкам списка
        polylineAlena.setStrokeColor(Color.BLUE);
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
        */
    }


};






//public class


