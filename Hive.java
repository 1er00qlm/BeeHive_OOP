import java.util.ArrayList;

public class Hive {
    private double food;
    private ArrayList<Object> deads;
    private int unfert;
    private ArrayList<Integer> eggCounts;
    private ArrayList<Integer> eggTimers;

    public Hive(double startFood) {
        food = startFood;
        deads = new ArrayList<Object>();
        unfert = 0;
        eggCounts = new ArrayList<Integer>();
        eggTimers = new ArrayList<Integer>();
    }

    public double getFood() { return food; }
    public void addFood(double a) { food += a; }
    public void useFood(double a) { food -= a; }

    public ArrayList<Object> getDeads() { return deads; }
    public int getDeadCount() { return deads.size(); }
    public void addDead(Object o) { deads.add(o); }
    public void removeDead(Object o) { deads.remove(o); }

    public int getUnfert() { return unfert; }
    public void addUnfert(int c) { unfert += c; }
    public void removeUnfert(int c) { unfert -= c; }

    public void addFertEggs(int count, int time) {
        eggCounts.add(new Integer(count));
        eggTimers.add(new Integer(time));
    }

    public void processEggs(Simulation sim) {
        if (eggTimers.isEmpty()) return;
        int firstTimer = eggTimers.get(0).intValue();
        firstTimer--;
        eggTimers.set(0, new Integer(firstTimer));
        if (firstTimer <= 0) {
            int count = eggCounts.get(0).intValue();
            eggCounts.remove(0);
            eggTimers.remove(0);
            for (int i = 0; i < count; i++) {
                sim.addBee(new Lichinka());
            }
        }
    }
}