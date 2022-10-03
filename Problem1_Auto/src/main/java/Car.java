public class Car {
    CarFactory vendor;
    String name;
    public Car(CarFactory vendor, String name) {
        this.vendor = vendor;
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
    }
}
