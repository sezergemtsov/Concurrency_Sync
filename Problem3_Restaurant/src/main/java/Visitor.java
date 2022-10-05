import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Visitor implements Runnable {

    static int qnt = 1;
    protected List<Dish> table = new ArrayList<>();

    ReentrantLock vLock = new ReentrantLock();
    Condition isOnTable = vLock.newCondition();
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 5000;

    public Visitor(Restaurant restaurant) {
        name = "Visitor " + qnt;
        t = new Thread(this, name);
        qnt++;
        this.restaurant = restaurant;
        t.start();
    }

    @Override
    public void run() {
        System.out.println(name + " in the restaurant!");
        enter();
        try {
            Thread.sleep(operationTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " ordered " + toOrder());
        toEat();
        try {
            Thread.sleep(operationTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toLeave();
    }

    void enter() {
        restaurant.lock.lock();
        try {
            restaurant.visitors.add(this);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

    Dish toOrder() {
        restaurant.lock.lock();
        try {
            Dish dish = new Dish();
            restaurant.orders.put(this, dish);
            restaurant.visitorsMakeOrder.add(this);
            restaurant.isOrder.signalAll();
            return new Dish();
        }finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

    void toEat() {
        vLock.lock();
        try {
            while (table.isEmpty()) {
                isOnTable.await();
                Dish dish = table.get(0);
                System.out.println(name + " is eating " + dish);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (vLock.isHeldByCurrentThread()) {
                vLock.unlock();
            }
        }
    }

    void toLeave() {

        restaurant.lock.lock();
        try {
            restaurant.visitors.remove(this);
            System.out.println(name + " leave the restaurant");
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

}
