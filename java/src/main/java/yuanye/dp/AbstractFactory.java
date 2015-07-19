package yuanye.dp;

/**
 * Design pattern 1: Abstract Factory.
 */
public class AbstractFactory {
    /**
     * Abstract factory for creating products
     */
    static interface CarFactory {
        Car produceCar();
    }

    /**
     * Abstract product to produce
     */
    static interface Car {
        void run();
    }

    /**
     * Concrete product:Tesla Model X
     */
    static class ModelX implements Car {
        @Override
        public void run() {
            System.out.println("A beautiful Tesla Model X is running");
        }
    }

    /**
     * Concrete product:BYD E6
     */
    static class E6 implements Car {
        @Override
        public void run() {
            System.out.println("A E6 liking a box is rolling");
        }
    }

    /**
     * Concrete car factory for producing Tesla
     */
    static class TeslaFactory implements CarFactory {

        @Override
        public Car produceCar() {
            return new ModelX();
        }
    }

    /**
     * Concrete car factory for producing BYD
     */
    static class BydFactory implements CarFactory {
        @Override
        public Car produceCar() {
            return new E6();
        }
    }

    /**
     * Client to produce a car
     */
    static class Client {

        public void produce() {
            CarFactory factory1 = new TeslaFactory();
            CarFactory factory2 = new BydFactory();

            Car tesla = factory1.produceCar();
            Car e6 = factory2.produceCar();

            tesla.run();
            e6.run();
        }
    }


    public static void main(String[] args) {
        Client client = new Client();
        client.produce();
    }
}
