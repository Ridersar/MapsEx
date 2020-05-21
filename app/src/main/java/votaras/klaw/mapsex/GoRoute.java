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

import android.content.Intent; //подключаем класс Intent
import android.os.Bundle;
import android.view.View; // подключаем класс View для обработки нажатия кнопки
import android.widget.Button;
import android.widget.TextView;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
                new CameraPosition(TARGET_LOCATION, 16.0f, 0.0f, 0.0f),
                new Animation(Animation.Type.LINEAR, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects().addCollection();
        //animationHandler = new Handler();

        //получение информации из предыдущего activity
        Intent intent = getIntent();
        String A = intent.getStringExtra(ActivityDisplayMessage.A_str);
        String B = intent.getStringExtra(ActivityDisplayMessage.B_str);

        //конвертирование в int
        int a = Integer.parseInt(A); //начало
        int b = Integer.parseInt(B); //конец

        ArrayList<OpPoint> mas = new ArrayList<OpPoint>(); //список опорных точек
        Route.createPoints(mas); //создание объектов
        Route.drawRoute(mas); //отрисовка маршрутов

        ArrayList<Point> route = Route.searchRoute(mas, a, b); //маршрут

        PolylineMapObject polylineRoute = mapObjects.addPolyline(new Polyline(route)); //отрисовка маршрута по точкам списка
        polylineRoute.setStrokeColor(Color.RED);
         //createMapObjects();
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
        //я оставила на всякий, но можете удалить.
        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
    }
}