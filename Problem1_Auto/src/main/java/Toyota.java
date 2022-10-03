public class Toyota implements CarFactory{

    public long producingTime = 2000;

    @Override
    public Car call() { return new Car(this,"Camry"); }

    @Override
    public String toString() {
        return "Toyota";
    }

    @Override
    public long getProducingTime() {
        return producingTime;
    }

    public void setPT(long time) {
        producingTime = time;
    }

}
