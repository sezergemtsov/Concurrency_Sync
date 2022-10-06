import java.util.concurrent.TimeUnit;

public class Waiter implements Runnable {

    static int qnt = 1;
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 2000;

    Visitor visitor;
    Dish dish;


    public Waiter(Restaurant restaurant) {
        name = "Waiter " + qnt;
        t = new Thread(this, name);
        t.setPriority(5);
        qnt++;
        this.restaurant = restaurant;
        System.out.println(name + " got ready for work!");
        t.start();
    }

    @Override
    public void run() {
        while (restaurant.isMoreVisitors) {
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!takeReadyDish()) {
                continue;
            }
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serveReadyDish();
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(name + " went home");
    }

    boolean takeReadyDish() {

        restaurant.lock.lock();
        try {
            while (restaurant.readyDishes.isEmpty()) {
                restaurant.isReady.await(10, TimeUnit.SECONDS);
                if (restaurant.readyDishes.isEmpty()) {
                    return false;
                }
            }
            visitor = restaurant.visitorsReadyToServe.pop();
            dish = restaurant.readyDishes.remove(visitor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
        return true;
    }

    void serveReadyDish() {
        visitor.vLock.lock();
        try {
            visitor.table.add(dish);
            visitor.isOnTable.signalAll();
            System.out.println(this.name + " served " + dish + " to " + visitor.name);
        } finally {
            if (visitor.vLock.isHeldByCurrentThread()) {
                visitor.vLock.unlock();
            }
        }
    }

}
