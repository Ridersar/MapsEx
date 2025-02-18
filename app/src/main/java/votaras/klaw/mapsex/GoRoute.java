package votaras.klaw.mapsex;

import android.Manifest;
import android.app.Activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.content.pm.PackageManager;

import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

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
import com.yandex.mapkit.transport.masstransit.MasstransitOptions;
import com.yandex.mapkit.transport.masstransit.MasstransitRouter;
import com.yandex.mapkit.transport.masstransit.PedestrianRouter;
import com.yandex.mapkit.transport.masstransit.Route;
import com.yandex.mapkit.transport.masstransit.Session;
import com.yandex.mapkit.transport.masstransit.TimeOptions;
import com.yandex.runtime.Error;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent; //подключаем класс Intent
import android.os.Bundle;
import android.util.Log;
import android.view.View; // подключаем класс View для обработки нажатия кнопки
import android.widget.Button;

import android.widget.TextView;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Toast;

import android.widget.EditText; // подключаем класс EditText
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
    private Point p1, p2;


    private final Point MagentaRoute1 = new Point(51.528021, 46.021923);
    private final Point MagentaRoute2 = new Point(51.522136, 46.018522);
    private final Point GreenRoute1 = new Point(51.525276, 45.994920);
    private final Point GreenRoute2 = new Point(51.528515, 46.000392);
    private final Point pointBlue1 = new Point(51.541023, 46.012058);
    private final Point pointBlue2 = new Point(51.544455, 46.014134);


    private MapView mapView;
    private MapObjectCollection mapObjects;
    private Handler animationHandler;
    private Button btn_create;


    private final Point ROUTE_START_LOCATION = new Point(51.528021, 46.021923);
    private final Point ROUTE_END_LOCATION = new Point(51.522136, 46.018522);


    private PedestrianRouter router;
    private MasstransitRouter mtRouter;

    GPS m_gps;


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

    public void routeBetween(double lat1, double lon1, double lat2, double lon2)
    {
        Transport transport = TransportFactory.getInstance();
        router = transport.createPedestrianRouter();
        /*
        MasstransitOptions options = new MasstransitOptions(
                new ArrayList<String>(),
                new ArrayList<String>(),
                new TimeOptions());
        List<RequestPoint> points = new ArrayList<RequestPoint>();
        points.add(new RequestPoint(ROUTE_START_LOCATION, RequestPointType.WAYPOINT, null));
        points.add(new RequestPoint(ROUTE_END_LOCATION, RequestPointType.WAYPOINT, null));
        //mtRouter = TransportFactory.getInstance().createMasstransitRouter();
        //mtRouter.requestRoutes(points, options, this);
        */

        ArrayList<OpPoint> mas = new ArrayList<OpPoint>(); //список опорных точек
        MyRoute.createPoints(mas); //создание объектов



        //ближайшая точка (к A)
        int start = 1;
        double distA = 1000.0;
        for (int i = 1; i < mas.size() - 1; i++)
            if (Math.sqrt((mas.get(i + 1).x - lat1)*(mas.get(i + 1).x - lat1) + (mas.get(i + 1).y - lon1)*(mas.get(i + 1).y - lon1))  < distA) //точка, которая ближе Math.pow()
            {
                start = i + 1;
                distA = Math.sqrt((mas.get(i + 1).x - lat1)*(mas.get(i + 1).x - lat1) + (mas.get(i + 1).y - lon1)*(mas.get(i + 1).y - lon1));
            }


        int finish = 1;
        double distB = 1000.0;
        for (int i = 1; i < mas.size() - 1; i++)
            if (Math.sqrt((mas.get(i + 1).x - lat2)*(mas.get(i + 1).x - lat2) + (mas.get(i + 1).y - lon2)*(mas.get(i + 1).y - lon2))  < distB) //точка, которая ближе Math.pow()
            {
                finish = i + 1;
                distB = Math.sqrt((mas.get(i + 1).x - lat2)*(mas.get(i + 1).x - lat2) + (mas.get(i + 1).y - lon2)*(mas.get(i + 1).y - lon2));
            }

        String start_str = Integer.toString(start); //вывод start
        String finish_str = Integer.toString(finish); //вывод finish
        Log.i("Start", start_str);
        Log.i("Finish", finish_str);

        //Route.drawRoute(mas); //отрисовка маршрутов
        ArrayList<Point> route = MyRoute.searchRoute(mas, start, finish); //маршрут

        ArrayList<RequestPoint> requests = new ArrayList<>();

        requests.add(new RequestPoint(new Point(lat1, lon1), RequestPointType.WAYPOINT, "")); //A
        for (int i = 0; i < route.size(); i++)
        {
            requests.add(new RequestPoint(new Point(route.get(i).getLatitude(), route.get(i).getLongitude()), RequestPointType.WAYPOINT, ""));
        }
        requests.add(new RequestPoint(new Point(lat2, lon2), RequestPointType.WAYPOINT, "")); //B

        //requests.add(new RequestPoint(new Point(lat1, lon1), RequestPointType.WAYPOINT, ""));
        //requests.add(new RequestPoint(new Point(lat2, lon2), RequestPointType.WAYPOINT, ""));
        //requests.add(new RequestPoint(new Point(lat1, lon1), RequestPointType.WAYPOINT, ""));
        //requests.add(new RequestPoint(new Point(51.531503, 46.004495), RequestPointType.WAYPOINT, ""));
        //requests.add(new RequestPoint(new Point(51.531903, 46.004995), RequestPointType.WAYPOINT, ""));

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
                //System.out.println(error);
                System.out.println("Oshibka");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        m_gps=new GPS(this);
        MapKitFactory.initialize(this);
        // Создание MapView.
        setContentView(R.layout.activity_go_route);
        super.onCreate(savedInstanceState);

        mapView = (MapView)findViewById(R.id.mapview);
        TransportFactory.initialize(this);

        // Перемещение камеры в центр Санкт-Петербурга.
        mapView.getMap().move(
                new CameraPosition(TARGET_LOCATION, 15.5f, 0.0f, 0.0f),
                new Animation(Animation.Type.LINEAR, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        //animationHandler = new Handler();





        //получение информации из предыдущего activity
        Intent intent = getIntent();
        String A = intent.getStringExtra(ActivityDisplayMessage.A_str);
        String B = intent.getStringExtra(ActivityDisplayMessage.B_str);



        //ГЕОКОДИРОВАНИЕ
        ArrayList<RequestPoint> requests = new ArrayList<>();
        //Geocoder geocoder = new Geocoder(getApplicationContext());
        Geocoder geocoder = new Geocoder(this); //Locale.US
        List<Address> addressesA = new ArrayList<Address>(); //ArrayList A
        List<Address> addressesB = new ArrayList<Address>(); //ArrayList B
        ArrayList<Point> polylinePoints = new ArrayList<>(); //создание списка точек

        //A = "Саратов, Слонова, 10/16";
        double latA= 46.000000, latB= 46.000000, lonA= 46.000000, lonB = 46.000000; //изменить на ошибку! (если не корректное считывание)

        latA=51.529515;
        lonA=46.001392;
        latB=51.629515;
        lonB=46.101392;
        try {
            //List<Address> list = geocoder.getFromLocationName(A, 1);
            //Address add = list.get(0);
            addressesA = geocoder.getFromLocationName(A, 1); //получение координат по названию (A)
            addressesB = geocoder.getFromLocationName(B, 1); //получение координат по названию (B)
            //Address a = geocoder.getFromLocationName(A, 1).get(0);
            //addresses.add(a);
            int lengt = addressesA.size(); //длина массива
            String Slengt = Integer.toString(lengt); //длина массива (строка)
            Log.i("Length - ", Slengt); //вывод информации
            //addresses=geocoder.getFromLocationName(B,1);

            if (addressesA != null) //addresses.size() > 0
            {
                latA = 51.529515;
                lonA = 46.001392;
                latA = addressesA.get(0).getLatitude();
                lonA = addressesA.get(0).getLongitude();
                mapView.getMap().getMapObjects().addPlacemark(new Point(latA, lonA));

                //double latitude1= addresses.get(1).getLatitude();
                //double longitude1= addresses.get(1).getLongitude();
                //polylinePoints.add(new Point(latitude, longitude)); //добавление точки в список
                //polylinePoints.add(new Point(latitude1,longitude1)); //добавление точки в список
                //p1=new Point(latitude, longitude);
                //p2=new Point(latitude1, longitude1);
            }
            if (addressesB != null) //addresses.size() > 0
            {
                latB = 51.629515;
                lonB = 46.101392;
                latB = addressesB.get(0).getLatitude();
                lonB = addressesB.get(0).getLongitude();
                mapView.getMap().getMapObjects().addPlacemark(new Point(latB, lonB));

                //double latitude1= addresses.get(1).getLatitude();
                //double longitude1= addresses.get(1).getLongitude();
                //polylinePoints.add(new Point(latitude, longitude)); //добавление точки в список
            }
        }
        catch (IOException e) //IOException e
        {
            // Toast.makeText(GoRoute.this,"Sorry, can you select again?", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }







        //МЕСТОПОЛОЖЕНИЕ

        //Location location=m_gps.getLocation();
        //Point my_gps=new Point(location.getLatitude(),location.getLongitude());
        //mapView.getMap().getMapObjects().addPlacemark(my_gps);








//        MyLocationListener.SetUpLocationListener(this);
        //      mapView.getMap().getMapObjects().addPlacemark(new Point(MyLocationListener.imHere.getLatitude(),MyLocationListener.imHere.getLongitude()));

        // Geocoder geocoder = new Geocoder(getApplicationContext());
        //List<Address> addresses;
        //ArrayList<Point> polylinePoints = new ArrayList<>(); //создание списка точек




        //PolylineMapObject polyline = mapObjects.addPolyline(new Polyline(polylinePoints)); //отрисовка маршрута по точкам списка
        //polyline.setStrokeColor(Color.RED);

        //конвертирование в int
        //double aa=Double.parseDouble(A);
        //double bb=Double.parseDouble(B);


        //int a = Integer.parseInt(A); //начало
        //int b = Integer.parseInt(B); //конец
        //mapView.getMap().getMapObjects().addPlacemark(new Point(aa, bb));


        //ПОСТРОЕНИЕ МАРШРУТА
        try{
            //routeBetween(aa,bb,51.517547, 46.010487);
            //routeBetween(51.528515, 46.000392,51.517547, 46.010487);
            routeBetween(latA, lonA, latB, lonB);
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

    final int DIALOG_EXIT = 1;
    public void onclick(View v) {
        // вызываем диалог
        showDialog(DIALOG_EXIT);
    }
    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_EXIT) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle(R.string.exit);
            // сообщение
            adb.setMessage(R.string.save_data);
            // иконка
            adb.setIcon(android.R.drawable.ic_dialog_info);
            // кнопка положительного ответа
            adb.setPositiveButton(R.string.yes, myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton(R.string.no, myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    OnClickListener myClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    saveStatistics();
                    CompletionScreen();
                    //finish();
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;

            }
        }
    };

     void CompletionScreen() {
        // действия, совершаемые после нажатия на кнопку
        // Создаем объект Intent для вызова новой Activity
        Intent intent = new Intent(this, Completion.class);
        // запуск activity
        startActivity(intent);
    }

    void saveStatistics() {
        //Здесь должна быть функция сохранения статистики...То, что внизу написано, после нажатия на
        //кнопку "ДА" выводит такое окошечко маленькое с надписью "сохранено"

        //Сохранение в статистику

        //заполнение БД
        SQLiteDatabase myDB;
        //создание / открытие базы данных
        myDB =
                openOrCreateDatabase("my.db", MODE_PRIVATE, null);

        //создание базы данных
        myDB.execSQL(
                "CREATE TABLE IF NOT EXISTS user (numberOfRoutes INT, flag INT)"
        );

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

        num++; //увеличение посещений
        ContentValues rowUp = new ContentValues();
        rowUp.put("numberOfRoutes", num);
        String where = "flag" + "=" + 1;
        myDB.update("user", rowUp, where, null);

        //закрытие
        myCursor.close();
        cursor.close();




        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    }
}