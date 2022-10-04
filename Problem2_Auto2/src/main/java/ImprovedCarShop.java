import java.util.concurrent.locks.ReentrantLock;

public class ImprovedCarShop extends CarShop {

    ReentrantLock lock;

    public ImprovedCarShop(CarFactory carFactory) {
        super(carFactory);
        lock = new ReentrantLock(true);
    }

    @Override
    public Car addCar() {
        synchronized (cars) {
            Car car = carFactory.call();
            try {
                lock.lockInterruptibly();
                cars.add(car);
                cars.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            return car;
        }
    }

    @Override
    public Car popCar() {
        synchronized (cars) {
            if (cars.isEmpty()) {
                try {
                    cars.wait();
                    lock.lock();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            return cars.pop();
        }
    }

}
