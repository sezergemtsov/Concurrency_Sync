public class Dish {
    String name;
    String[] names = {"meat", "desert", "fish", "vegetables"};

    public Dish() {
        name = names[(int) (Math.random() * 4)];
    }

    @Override
    public String toString() {
        return name;
    }
}
