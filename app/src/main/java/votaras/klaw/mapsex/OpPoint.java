package votaras.klaw.mapsex;

import android.graphics.Color;

import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.geometry.Polyline;
import com.yandex.mapkit.map.PolylineMapObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        //добавить везде id
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
