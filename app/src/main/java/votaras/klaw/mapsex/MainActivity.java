package votaras.klaw.mapsex;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.PolylineMapObject;
import com.yandex.mapkit.mapview.MapView;

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

    class OpPoint
    {
        double x, y; //координаты точки
        int countVisit; //количество посещений точки
        int id; //идентификатор
        Map<Integer, Integer> transitions = new HashMap<Integer, Integer>(); //связи (с какой точкой, вес перехода)
        boolean visit = false; //посещена ли вершина (изначально нет) (для прохода по графу)
        //Pair<OpPoint, Integer> pr = new Pair<OpPoint, Integer> ();
        //Pair<OpPoint, Integer> pr2;//
        //ArrayList<Pair<Integer, Integer>> prMas;

        //конструкторы опорной точки
        //1 соседняя
        public OpPoint(double x, double y, int id1)
        {
            this.x = x;
            this.y = y;
            transitions.put(id1, 0);
        }

        //2 соседних
        public OpPoint(double x, double y, int id1, int id2)
        {
            this.x = x;
            this.y = y;
            transitions.put(id1, 0);
            transitions.put(id2, 0);
        }

        //3 соседних
        public OpPoint(double x, double y, int id1, int id2, int id3)
        {
            this.x = x;
            this.y = y;
            transitions.put(id1, 0);
            transitions.put(id2, 0);
            transitions.put(id3, 0);
        }

        //4 соседних
        public OpPoint(double x, double y, int id1, int id2, int id3, int id4)
        {
            this.x = x;
            this.y = y;
            transitions.put(id1, 0);
            transitions.put(id2, 0);
            transitions.put(id3, 0);
            transitions.put(id4, 0);
        }
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
        //createMapObjects(); //отрисовка объектов
        ArrayList<OpPoint> mas = new ArrayList<OpPoint>(); //список опорных точек
        createPoints(mas); //создание объектов
        drawRoute(mas); //отрисовка маршрутов
        searchRoute(mas);
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
    private void createMapObjects()
    {
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

    //создание точек
    private void createPoints(ArrayList<OpPoint> mas)
    {
        //добавление в список (тестовое)
        mas.add(new OpPoint(0,0,0)); //null
        mas.add(new OpPoint(51.528253, 46.000825, 2, 3, 5)); //1
        mas.add(new OpPoint(51.528284, 46.007407, 1, 4)); //2
        mas.add(new OpPoint(51.526316, 46.004129, 1, 4)); //3
        mas.add(new OpPoint(51.525953, 46.012302, 2, 3, 6)); //4
        mas.add(new OpPoint(51.522377, 46.000247, 1, 6)); //5
        mas.add(new OpPoint(51.522229, 46.008572, 4, 5)); //6

        //добавление весов (тестовое)
        mas.get(0).transitions.put(1, 1000);
        //1
        mas.get(1).transitions.put(2, 1);
        mas.get(1).transitions.put(3, 4);
        mas.get(1).transitions.put(5, 2);
        //2
        mas.get(2).transitions.put(1, 1);
        mas.get(2).transitions.put(4, 9);
        //3
        mas.get(3).transitions.put(1, 4);
        mas.get(3).transitions.put(4, 7);
        //4
        mas.get(4).transitions.put(2, 9);
        mas.get(4).transitions.put(3, 7);
        mas.get(4).transitions.put(6, 2);
        //5
        mas.get(5).transitions.put(1, 2);
        mas.get(5).transitions.put(6, 8);
        //6
        mas.get(6).transitions.put(4, 2);
        mas.get(6).transitions.put(5, 8);
    }

    //отрисовка частей маршрута
    private void drawRoute(ArrayList<OpPoint> mas)
    {
        //ArrayList<Point> Route = new ArrayList<>();
        //Route.add(new Point(mas.get(1).x, mas.get(1).y)); //добавление точки в список
        //Route.add(new Point(mas.get(2).x, mas.get(2).y)); //добавление точки в список

        /*

        //вывод всех соединений точки xx
        int xx = 1;
        for(Map.Entry<Integer, Integer> item : mas.get(xx).transitions.entrySet())
        {
            Route.add(new Point(mas.get(item.getKey()).x, mas.get(item.getKey()).y)); //добавление точки в список
            Route.add(new Point(mas.get(xx).x, mas.get(xx).y)); //добавление точки в список
        }

        */
        /*

        //вывод всех соединений (с лишними переходами)
        for(int i = 1; i < mas.size(); i++)
        {
            for (Map.Entry<Integer, Integer> item : mas.get(i).transitions.entrySet())
            {
                Route.add(new Point(mas.get(item.getKey()).x, mas.get(item.getKey()).y)); //добавление точки в список
                Route.add(new Point(mas.get(i).x, mas.get(i).y)); //добавление точки в список
            }
        }

        PolylineMapObject polylineRoute = mapObjects.addPolyline(new Polyline(Route)); //отрисовка маршрута по точкам списка
        polylineRoute.setStrokeColor(Color.RED);

         */
    }

    //алгоритм Дейкстры (поиск наименее посещаемого пути)
    private void searchRoute(ArrayList<OpPoint> mas) {
        int start = 1; // стартовая вершина
        int INT_MAX = 1000; //поменять!

        ArrayList<Integer> distance = new ArrayList<Integer>(); //массив расстояний, заполнение бесконечностями весов всех вершин
        for (int i = 0; i < mas.size(); i++)
            distance.add(INT_MAX);

        ArrayList<Integer> parents = new ArrayList<Integer>(); //массив предков
        for (int i = 0; i < mas.size(); i++)
            parents.add(0);

        distance.set(start, 0); //стартовая вершина
        ArrayList<Boolean> visit = new ArrayList<Boolean>(); //массив меток //char
        for (int i = 0; i < mas.size(); i++)
            visit.add(false);

        //На каждой итерации сначала находится вершина vertex, имеющая наименьшее расстояние среди непомеченных вершин
        for (int i = 0; i < mas.size(); i++)
        {
            int vertex = -1; //вершина
            for (int j = 0; j < mas.size(); j++) //проход по всем вершинам
                if (visit.get(j) == false && (vertex == -1 || distance.get(j) < distance.get(vertex))) //если вершина не посещена и значение веса в вершине меньше
                    vertex = j; //обновить значение
            if (distance.get(vertex) == INT_MAX) //если минимальное расстояние равно бесконечности
                break; //остановка алгоритма
            visit.set(vertex, true); //вершина просмотрена

            //просматриваются все ребра из данной вершины
            for (Map.Entry<Integer, Integer> j : mas.get(vertex).transitions.entrySet()) //проход по переходам точки
            {
                int to = j.getKey(); //вершина в которую перешли
                int len = j.getValue(); //ее вес
                if (distance.get(vertex) + len < distance.get(to)) //если расстояние из этой вершины до точки короче (чем предыдущие если они есть)
                {
                    distance.set(to, distance.get(vertex) + len); //обновление веса
                    parents.set(to, vertex); //сохранение вершины в массиве предков
                }
            }
        }

        //В массиве distance оказываются длины кратчайших путей до всех вершин, а в массиве parents - предки всех вершин (кроме стартовой start)

        //восстановление пути до вершины finish
        ArrayList<Integer> path = new ArrayList<Integer>(); //путь

        int finish = 6; //конечная вершина пути
        for (int vertex = finish; vertex != start; vertex = parents.get(vertex)) //проход по вершинам пути
            path.add(vertex); //сохранение вершины пути
        path.add(start); //последний пункт (стартовый)
        Collections.reverse(path); //переворот пути, т.к. шли с конца


        //отрисовка
        ArrayList<Point> Route = new ArrayList<>();
        for(int i = 0; i < path.size(); i++)
        {
                Route.add(new Point(mas.get(path.get(i)).x, mas.get(path.get(i)).y)); //добавление точки в список
        }

        PolylineMapObject polylineRoute = mapObjects.addPolyline(new Polyline(Route)); //отрисовка маршрута по точкам списка
        polylineRoute.setStrokeColor(Color.RED);

    }
}