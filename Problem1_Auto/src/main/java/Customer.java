public class Customer implements Runnable {

    String name;
    CarShop carShop;
    long waitingTime = 1500;
    Thread t;

    public Customer(String name, CarShop carShop) {
        this.name = name;
        this.carShop = carShop;
        t = new Thread(this,name);
        t.start();
    }

    @Override
    public void run() {

        while (carShop.isMore==true) {
            System.out.println(name + " come to car shop");
            System.out.println(name + " went out on new " + carShop.popCar());
            try {
                Thread.sleep(waitingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public void setWaitingTime(long waitingTime) {
        this.waitingTime = waitingTime;
    }
}
