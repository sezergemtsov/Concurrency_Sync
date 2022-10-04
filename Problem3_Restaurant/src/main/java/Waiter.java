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
        while (true) {
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            takeReadyDish();
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            serveReadyDish(visitor);
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void takeReadyDish() {

        try {
            restaurant.lock.lock();
            synchronized (restaurant.visitors) {
                synchronized (restaurant.readyDishes) {
                    if (restaurant.readyDishes.isEmpty()) {
                        try {
                            restaurant.readyDishes.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    restaurant.visitors.forEach(x -> {
                        if (restaurant.readyDishes.containsKey(x)) {
                            visitor = x;
                        }
                    });
                    dish = restaurant.readyDishes.remove(visitor);
                }
            }
        } finally {
            restaurant.lock.unlock();
        }
    }

    void serveReadyDish(Visitor visitor) {
        try {
            restaurant.lock.lock();
            synchronized (visitor.table) {
                visitor.table.add(dish);
                visitor.table.notify();
            }
        } finally {
            restaurant.lock.unlock();
        }
    }

}
