import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {

    public Map<Visitor,Dish> orders;
    public Map<Visitor,Dish> readyDishes;
    public Deque<Visitor> visitors;
    public Deque<Visitor> visitorsMakeOrder;
    public Deque<Visitor> visitorsReadyToServe;
    public boolean isMoreVisitors;

    public ReentrantLock lock;
    public Condition isOrder;
    public Condition isReady;
    public Condition isVisitors;

    public Restaurant() {
        orders = new HashMap<>();
        readyDishes = new HashMap<>();
        visitors = new ArrayDeque<>();
        visitorsMakeOrder = new ArrayDeque<>();
        lock = new ReentrantLock(true);
        isOrder = lock.newCondition();
        isReady = lock.newCondition();
        isVisitors = lock.newCondition();
        visitorsReadyToServe = new ArrayDeque<>();
        isMoreVisitors = true;
    }
    public boolean close() {
        if (!visitors.isEmpty()) {
            return false;
        } else {
            isMoreVisitors = false;
            return true;
        }
    }
}
