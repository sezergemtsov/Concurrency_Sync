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

        try {
            restaurant.lock.lock();
            synchronized (restaurant.orders) {
                synchronized (restaurant.visitorsMakeOrder) {
                    if (restaurant.orders.isEmpty()) {
                        try {
                            restaurant.orders.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    visitor = restaurant.visitorsMakeOrder.pop();
                    dish = restaurant.orders.remove(visitor);
                    restaurant.visitorsMakeOrder.notify();
                    restaurant.orders.notify();
                }
            }
        } finally {
            restaurant.lock.unlock();
        }

    }

    void finishCooking() {

        try {
            restaurant.lock.lock();
            synchronized (restaurant.readyDishes) {
                restaurant.readyDishes.put(visitor, dish);
                System.out.println(name + " complete " + dish);
                restaurant.readyDishes.notify();
            }
        } finally {
            restaurant.lock.unlock();
        }

    }

}
