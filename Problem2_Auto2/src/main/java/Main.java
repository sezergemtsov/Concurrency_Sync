public class Main {
    public static void main(String[] args) {

        CarShop carShop = new ImprovedCarShop(new Toyota());

        new Customer("Igor",carShop);
        new Customer("Oleg", carShop);
    }
}
