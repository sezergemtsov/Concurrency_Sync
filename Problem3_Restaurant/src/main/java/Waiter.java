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
            serveReadyDish();
            try {
                Thread.sleep(operationTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void takeReadyDish() {

        restaurant.lock.lock();
        try {
            while (restaurant.readyDishes.isEmpty()) {
                restaurant.isReady.await();
            }
            restaurant.visitors.forEach(x -> {
                if (restaurant.readyDishes.containsKey(x)) {
                    visitor = x;
                }
            });
            dish = restaurant.readyDishes.remove(visitor);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (restaurant.lock.isHeldByCurrentThread()) {
                restaurant.lock.unlock();
            }
        }
    }

    void serveReadyDish() {
        visitor.vLock.lock();
        try {
            visitor.table.add(dish);
            visitor.isOnTable.signalAll();
        } finally {
            if (visitor.vLock.isHeldByCurrentThread()) {
                visitor.vLock.unlock();
            }
        }
    }

}
