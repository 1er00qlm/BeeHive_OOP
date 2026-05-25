public class Lichinka extends Bee {
    private int grown;

    public Lichinka() {
        super(Simulation.getBabyWeight(), 9999);
        grown = 0;
    }

    public void doWork(Simulation sim) {
        grown++;
        addWeight(Simulation.getGrowRate());
        if (grown >= Simulation.getGrowTime()) {
            double p = sim.getRandom().nextDouble();
            Bee newBee;
            if (p < Simulation.getWorkerChance()) {
                String job = sim.getRandom().nextDouble() < Simulation.getCleanerChance() ? "CLEANER" : "HONEY_GATHERER";
                newBee = new Workbee(Simulation.getWorkerW(), Simulation.getWorkerLife(), job);
            } else {
                newBee = new Truten(Simulation.getDroneW(), Simulation.getDroneLife());
            }
            sim.addBee(newBee);
            sim.removeBee(this);
            setAlive(false);
        }
    }
}
