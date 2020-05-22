package votaras.klaw.mapsex;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
    private Button btn_create, btn_statistics;
    private int kol = 0;
    SQLiteDatabase myDB;
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
        MapKitFactory.initialize(this);
        // Создание MapView.

        setContentView(R.layout.activity_main);
        addListenerOnButton ();
        super.onCreate(savedInstanceState);
        mapView = (MapView)findViewById(R.id.mapview);

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
                    }
                }
        );
    };



    };






//public class


