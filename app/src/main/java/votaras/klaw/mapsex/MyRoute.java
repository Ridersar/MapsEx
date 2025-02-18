package votaras.klaw.mapsex;

import android.graphics.Color;
import android.util.Log;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.PolylineMapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MyRoute
{
    //создание точек
    static void createPoints(ArrayList<OpPoint> mas)
    {
        //добавление в список (тестовое)
        mas.add(new OpPoint(0,0,0)); //null
        mas.add(new OpPoint(51.533694, 46.014967, 2, 4)); //1
        mas.add(new OpPoint(51.534387, 46.012199, 1, 3, 5)); //2
        mas.add(new OpPoint(51.535070, 46.009447, 2, 6)); //3
        mas.add(new OpPoint(51.535983, 46.016313, 1, 5, 7)); //4
        mas.add(new OpPoint(51.536620, 46.013590, 2, 4, 6, 8)); //5
        mas.add(new OpPoint(51.537257, 46.010808, 3, 5, 9)); //6
        mas.add(new OpPoint(51.537746, 46.017283, 4, 8)); //7
        mas.add(new OpPoint(51.538355, 46.014530, 5, 7, 9)); //8
        mas.add(new OpPoint(51.538936, 46.011852, 6, 8)); //9

        //добавление весов (тестовое)
        mas.get(0).transitions.put(1, 1000);
        //1
        mas.get(1).transitions.put(2, 6);
        mas.get(1).transitions.put(4, 8);
        //2
        mas.get(2).transitions.put(1, 6);
        mas.get(2).transitions.put(3, 5);
        mas.get(2).transitions.put(5, 2);
        //3
        mas.get(3).transitions.put(2, 5);
        mas.get(3).transitions.put(6, 7);
        //4
        mas.get(4).transitions.put(1, 8);
        mas.get(4).transitions.put(5, 4);
        mas.get(4).transitions.put(7, 9);
        //5
        mas.get(5).transitions.put(2, 2);
        mas.get(5).transitions.put(4, 4);
        mas.get(5).transitions.put(6, 3);
        mas.get(5).transitions.put(8, 2);
        //6
        mas.get(6).transitions.put(3, 7);
        mas.get(6).transitions.put(5, 3);
        mas.get(6).transitions.put(9, 8);
        //7
        mas.get(7).transitions.put(4, 9);
        mas.get(7).transitions.put(8, 11);
        //8
        mas.get(8).transitions.put(5, 2);
        mas.get(8).transitions.put(7, 11);
        mas.get(8).transitions.put(9, 10);
        //9
        mas.get(9).transitions.put(6, 8);
        mas.get(9).transitions.put(8, 10);
    }

    //отрисовка частей маршрута
    static void drawRoute(ArrayList<OpPoint> mas)
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
    static ArrayList<Point> searchRoute(ArrayList<OpPoint> mas, int start, int finish) {
        //int start = 1; // стартовая вершина
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
        String p_ln = Integer.toString(parents.size()); //вывод start
        Log.i("Dlina mas parent", p_ln);
        //В массиве distance оказываются длины кратчайших путей до всех вершин, а в массиве parents - предки всех вершин (кроме стартовой start)

        //восстановление пути до вершины finish
        ArrayList<Integer> path = new ArrayList<Integer>(); //путь

        //int finish = 6; //конечная вершина пути
        //start = 3;
        //finish = 4;
        for (int vertex = finish; vertex != start; vertex = parents.get(vertex)) //проход по вершинам пути
        {
            path.add(vertex); //сохранение вершины пути
            String start_str = Integer.toString(vertex); //вывод start
            Log.i("Path", start_str);
        }
        path.add(start); //последний пункт (стартовый)
        Collections.reverse(path); //переворот пути, т.к. шли с конца


        //список точек маршрута
        //ArrayList<Point> Route = new ArrayList<>();
        ArrayList<Point> route = new ArrayList<>();
        for(int i = 0; i < path.size(); i++)
        {
            int p = mas.get(path.get(i)).id; //
            String p_str = Integer.toString(p); //вывод вершины
            Log.i("Point", p_str); //
            route.add(new Point(mas.get(path.get(i)).x, mas.get(path.get(i)).y)); //добавление точки в список
        }
        return route;
        //PolylineMapObject polylineRoute = mapObjects.addPolyline(new Polyline(Route)); //отрисовка маршрута по точкам списка
        //polylineRoute.setStrokeColor(Color.RED);

    }
}
