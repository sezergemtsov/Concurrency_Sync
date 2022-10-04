import java.util.ArrayList;
import java.util.List;

public class Visitor implements Runnable {

    static int qnt = 1;
    protected List<Dish> table = new ArrayList<>();
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
        try {
            restaurant.lock.lock();
            synchronized (restaurant.visitors) {
                restaurant.visitors.add(this);
                restaurant.visitors.notify();
            }
        } finally {
            restaurant.lock.unlock();
        }

    }

    Dish toOrder() {

        try {
            restaurant.lock.lock();
            synchronized (restaurant.orders) {
                synchronized (restaurant.visitorsMakeOrder) {
                    Dish dish = new Dish();
                    restaurant.orders.put(this, dish);
                    restaurant.visitorsMakeOrder.add(this);
                    restaurant.orders.notify();
                    return new Dish();
                }
            }
        } finally {
            restaurant.lock.unlock();
        }

    }

    void toEat() {
        try{
            restaurant.lock.lock();
            synchronized (this.table) {
                if (table.isEmpty()) {
                    try {
                        table.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Dish dish = table.get(0);
                System.out.println(name + " is eating " + dish);
            }
        } finally {
            restaurant.lock.unlock();
        }
    }

    void toLeave() {
        try {
            restaurant.lock.lock();
            synchronized (restaurant.visitors) {
                restaurant.visitors.remove(this);
                System.out.println(name + " leave the restaurant");
                restaurant.visitors.notify();
            }
        } finally {
            restaurant.lock.unlock();
        }
    }

}
