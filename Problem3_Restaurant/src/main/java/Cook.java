public class Cook implements Runnable {

    static int qnt = 1;
    Thread t;
    String name;
    Restaurant restaurant;
    long operationTime = 5000;

    Visitor visitor;
    Dish dish;


    public Cook(Restaurant restaurant) {
        name = "Cook " + qnt;
        t = new Thread(this, name);
        t.setPriority(5);
        qnt++;
        this.restaurant = restaurant;
        System.out.println(name + " got ready for work!");
        t.start();
    }


    @Override
    public void run() {
        while (true) {
            startCooking();
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishCooking();
        }
    }

    void startCooking() {
        restaurant.lock.lock();
        try {
            while (restaurant.orders.isEmpty()) {
                restaurant.isOrder.await();
            }
            visitor = restaurant.visitorsMakeOrder.pop();
            dish = restaurant.orders.remove(visitor);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }

    }

    void finishCooking() {
        restaurant.lock.lock();
        try {
            restaurant.readyDishes.put(visitor, dish);
            System.out.println(name + " complete " + dish);
            restaurant.isReady.signalAll();
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

}
