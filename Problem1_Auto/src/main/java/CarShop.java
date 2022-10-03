import java.util.ArrayDeque;
import java.util.Deque;

public class CarShop implements Runnable {

    public Deque<Car> cars;
    CarFactory carFactory;
    Thread t;
    public boolean isMore = true;
    final int expectedCarsQty = 10;

    public CarShop(CarFactory carFactory) {
        cars = new ArrayDeque<>();
        this.carFactory = carFactory;
        t= new Thread(this,"Car Shop");
        t.start();
    }

    @Override
    public void run(){
        for (int i = 0; i < expectedCarsQty; i++) {
            if (i == expectedCarsQty-2) {
                isMore = false;
            }
            System.out.println(carFactory.toString()+" produced new car " + addCar());
            try {
                Thread.sleep(carFactory.getProducingTime());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Car addCar() {

        synchronized (cars) {
            Car car = carFactory.call();
            cars.add(car);
            cars.notify();
            return car;
        }

    }
    public Car popCar() {
        synchronized (cars) {
            if (cars.isEmpty()) {
                try {
                    cars.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return cars.pop();
        }
    }

    public Car peekCar() {
        synchronized (cars) {
            if (cars.isEmpty()) {
                try {
                    cars.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return cars.peek();
        }
    }

}
