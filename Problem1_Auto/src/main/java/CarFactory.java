import java.util.concurrent.Callable;

public interface CarFactory extends Callable<Car> {
    long getProducingTime();
    Car call();
}
