public class Truten extends Bee {
    private int fertilizedCount;

    public Truten(double weight, int maxAge) {
        super(weight, maxAge);
        fertilizedCount = 0;
    }

    public int fertilize(Simulation sim, int available) {
        double foodFactor = Math.min(1.0, sim.getHive().getFood() / Simulation.getBestFood());
        double randF = 0.7 + 0.6 * sim.getRandom().nextDouble();
        double power = Simulation.getBaseFert() * foodFactor * randF;
        int can = Math.min(available, (int) power);
        if (can > 0) {
            sim.getHive().removeUnfert(can);
            sim.getHive().addFertEggs(can, Simulation.getEggTime());
            fertilizedCount += can;
        }
        return can;
    }

    public void doWork(Simulation sim) { }
}