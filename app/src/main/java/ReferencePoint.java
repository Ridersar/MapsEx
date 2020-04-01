import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

    class RefPoint{
        public
        String id;//перекресток улиц
        double x,y;//координаты
        //string tag;
        int attendance;//посещаемость точки
        RefPoint(String ID,double x,double y,int at){
            this.id=ID;
            this.x=x;
            this.y=y;
            this.attendance=at;
        }
    }

class AllPoints {
public RefPoint binSearch(List<RefPoint> rp, RefPoint name){
    int low=rp.get(0).attendance;
    int high=rp.get(rp.size()-1).attendance;
    int index=-1;
while ( low<= high) {
        int mid = (low + high) / 2;
        if (rp.get(mid).attendance < name.attendance) {
            low = mid + 1;
        } else if (rp.get(mid).attendance > name.attendance) {
            high = mid - 1;
        } else if (rp.get(mid).attendance == name.attendance) {
            index=mid;
            break;
        }
    }
    return rp.get(index);
}


public void sort(List<RefPoint> rp,int low, int high){
            if (rp.size() == 0)
                return;

            if (low >= high)
                return;

            int middle = low + (high - low) / 2;
            int opora = rp.get(middle).attendance;


            int i = low, j = high;
            while (i <= j) {
                while (rp.get(i).attendance < opora) {
                    i++;
                }

                while (rp.get(i).attendance > opora) {
                    j--;
                }

                if (i <= j) {
                    Collections.swap(rp,i,j);
                    i++;
                    j--;
                }
            }

            // вызов рекурсии для сортировки левой и правой части
            if (low < j)
                sort(rp, low, j);

            if (high > i)
                sort(rp, i, high);
        }

    private HashMap<RefPoint, List<RefPoint>> PointMap = new HashMap<RefPoint, List<RefPoint>>();

    public void addPoint(RefPoint PointName) {
        if (!hasPoint(PointName)) {
            PointMap.put(PointName, new ArrayList<RefPoint>());
        }
    }

    public boolean hasPoint(RefPoint PointName) {
        return PointMap.containsKey(PointName);
    }

    public boolean hasEdge(RefPoint PointName1, RefPoint PointName2) {
        if (!hasPoint(PointName1)) return false;
        List<RefPoint> edges = PointMap.get(PointName1);
        return binSearch(edges, PointName2) != NULL;
    }

    public void addEdge(RefPoint PointName1, RefPoint PointName2) {
        if (!hasPoint(PointName1)) addPoint(PointName1);
        if (!hasPoint(PointName2)) addPoint(PointName2);
        List<RefPoint> edges1 = PointMap.get(PointName1);
        List<RefPoint> edges2 = PointMap.get(PointName2);
        edges1.add(PointName2);
        edges2.add(PointName1);
        sort(edges1,0,edges1.size()-1);
        sort(edges2,0,edges2.size()-1);
    }

    public Map<RefPoint, List<RefPoint>> getPointMap() {
        return PointMap;
    }
}

