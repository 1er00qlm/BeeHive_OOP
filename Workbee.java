import java.util.ArrayList;

public class Workbee extends Bee {
    private String job;
    private double gathered;
    private int cleaned;
    private int idle;

    public Workbee(double weight, int maxAge, String job) {
        super(weight, maxAge);
        this.job = job;
        this.gathered = 0;
        this.cleaned = 0;
        this.idle = 0;
    }

    public String getJob() { return job; }
    public double getGathered() { return gathered; }
    public int getCleaned() { return cleaned; }
    public int getIdle() { return idle; }

    public void doWork(Simulation sim) {
        if (job.equals("HONEY_GATHERER")) {
            double add = Simulation.getGetHoney() * (0.8 + 0.4 * sim.getRandom().nextDouble());
            sim.produceHoney(add);
            gathered += add;
        } else if (job.equals("CLEANER")) {
            ArrayList deads = sim.getHive().getDeads();
            if (deads.isEmpty()) {
                idle++;
                return;
            }
            Object target = null;
            for (int i = 0; i < deads.size(); i++) {
                Object d = deads.get(i);
                if (d instanceof Bee && ((Bee) d).getWeight() < this.getWeight()) {
                    target = d;
                    break;
                }
            }
            if (target != null) {
                sim.removeCorpse(target);
                cleaned++;
            } else {
                idle++;
            }
        }
    }
}