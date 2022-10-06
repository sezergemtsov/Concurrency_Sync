import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws InterruptedException {

        Restaurant restaurant = new Restaurant();

        Cook cook = new Cook(restaurant);

        List<Waiter> waiters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            waiters.add(new Waiter(restaurant));
        }

        List<Visitor> visitors = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            if (i == 10) {
                restaurant.lock.lock();
                try {
                    while (!restaurant.close()) {
                        restaurant.isVisitors.await();
                    }
                } finally {
                    restaurant.lock.unlock();
                }
            } else {
                visitors.add(new Visitor(restaurant));
                Thread.sleep(5000);
            }
        }

    }
}
