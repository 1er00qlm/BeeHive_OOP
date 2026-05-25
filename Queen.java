public class Queen extends Bee {
    private int eggsLaid;

    public Queen(double weight, int maxAge) {
        super(weight, maxAge);
        eggsLaid = 0;
    }

    public void layEggs(Simulation sim) {
        double foodFactor = Math.min(1.0, sim.getHive().getFood() / Simulation.getBestFood());
        double deadFactor = Math.max(0.0, 1.0 - (sim.getHive().getDeadCount() / (double) Simulation.getDeadPenalty()));
        double prod = foodFactor * deadFactor;
        int eggs = (int) (Simulation.getBaseEggs() * prod);
        if (eggs > 0) {
            sim.getHive().addUnfert(eggs);
            eggsLaid += eggs;
        }
    }

    public void doWork(Simulation sim) { }
}