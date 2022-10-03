public class Main {
    public static void main(String[] args) {

        CarShop carShop = new CarShop(new Toyota());

        Customer customer1 = new Customer("Igor",carShop);
        Customer customer2 = new Customer("Oleg", carShop);

    }
}
