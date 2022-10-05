import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ImprovedCarShop extends CarShop {

    ReentrantLock lock;
    Condition isEmpty;

    public ImprovedCarShop(CarFactory carFactory) {
        super(carFactory);
        lock = new ReentrantLock(true);
        isEmpty = lock.newCondition();
    }

    @Override
    public Car addCar() {
        lock.lock();
        try {
            Car car = carFactory.call();
            cars.add(car);
            isEmpty.signalAll();
            return car;
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public Car popCar() {
        int pr = lock.getHoldCount();
        if (pr >= 10) {
            pr = 10;
        } else {
            pr = 10 - pr;
        }
        Thread.currentThread().setPriority(pr);
        lock.lock();
        try {
            while (cars.isEmpty()) {
                isEmpty.await();
            }
            return cars.pop();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

}
