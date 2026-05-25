import java.util.ArrayList;
import java.util.Random;

public class Simulation {
    public static final int MAX_TIME = 10000;
    private static final double FOOD_NEED = 0.02;
    private static final double BEST_FOOD = 1000.0;
    private static final int DEAD_PENALTY = 50;
    private static final int BASE_EGGS = 20;
    private static final double BASE_FERT = 5.0;
    private static final int EGG_TIME = 120;
    private static final int GROW_TIME = 240;
    private static final double BABY_WEIGHT = 0.5;
    private static final double GROW_RATE = 0.02;
    private static final double WORKER_CHANCE = 0.8;
    private static final double CLEANER_CHANCE = 0.4;
    private static final double WORKER_W = 10.0;
    private static final int WORKER_LIFE = 2400;
    private static final double DRONE_W = 12.0;
    private static final int DRONE_LIFE = 1800;
    private static final double QUEEN_W = 15.0;
    private static final int QUEEN_LIFE = 10000;
    private static final double GET_HONEY = 2.0;
    private static final int EGG_INTERVAL = 60;

    private ArrayList<Object> all;
    private Hive hive;
    private Queen queen;
    private int time;
    private int eggTimer;
    private Random random;

    public Simulation() {
        all = new ArrayList<Object>();
        random = new Random();
    }

    public void start() {
        all.clear();
        queen = new Queen(QUEEN_W, QUEEN_LIFE);
        all.add(queen);
        for (int i = 0; i < 5; i++) {
            all.add(new Workbee(WORKER_W, WORKER_LIFE, "HONEY_GATHERER"));
        }
        for (int i = 0; i < 2; i++) {
            all.add(new Workbee(WORKER_W, WORKER_LIFE, "CLEANER"));
        }
        for (int i = 0; i < 3; i++) {
            all.add(new Truten(DRONE_W, DRONE_LIFE));
        }
        time = 0;
        eggTimer = 0;
        hive = new Hive(500.0);
    }

    public void step() {
        ArrayList<Object> copy = new ArrayList<Object>();
        for (int i = 0; i < all.size(); i++) {
            copy.add(all.get(i));
        }
        for (int i = 0; i < copy.size(); i++) {
            Bee bee = (Bee) copy.get(i);
            bee.update(this);
        }
        eggTimer++;
        if (eggTimer >= EGG_INTERVAL && queen.isAlive()) {
            queen.layEggs(this);
            eggTimer = 0;
        }
        int unfert = hive.getUnfert();
        if (unfert > 0) {
            for (int i = 0; i < all.size(); i++) {
                Object obj = all.get(i);
                if (obj instanceof Truten) {
                    Truten d = (Truten) obj;
                    if (d.isAlive()) {
                        int fert = d.fertilize(this, unfert);
                        unfert -= fert;
                        if (unfert <= 0) break;
                    }
                }
            }
        }
        hive.processEggs(this);
        time++;
    }

    public int getTime() { return time; }
    public Hive getHive() { return hive; }
    public Queen getQueen() { return queen; }
    public ArrayList<Object> getAll() { return all; }
    public void addBee(Object bee) { all.add(bee); }
    public void removeBee(Object bee) { all.remove(bee); }
    public Random getRandom() { return random; }

    public synchronized boolean consumeHoney(double amount) {
        if (hive.getFood() >= amount) {
            hive.useFood(amount);
            return true;
        }
        return false;
    }
    public synchronized void produceHoney(double amount) { hive.addFood(amount); }
    public synchronized void addCorpse(Object bee) { hive.addDead(bee); }
    public synchronized void removeCorpse(Object bee) { hive.removeDead(bee); }

    public static double getFoodNeed() { return FOOD_NEED; }
    public static double getBestFood() { return BEST_FOOD; }
    public static int getDeadPenalty() { return DEAD_PENALTY; }
    public static int getBaseEggs() { return BASE_EGGS; }
    public static double getBaseFert() { return BASE_FERT; }
    public static int getEggTime() { return EGG_TIME; }
    public static int getGrowTime() { return GROW_TIME; }
    public static double


    BabyWeight() { return BABY_WEIGHT; }
    public static double getGrowRate() { return GROW_RATE; }
    public static double getWorkerChance() { return WORKER_CHANCE; }
    public static double getCleanerChance() { return CLEANER_CHANCE; }
    public static double getWorkerW() { return WORKER_W; }
    public static int getWorkerLife() { return WORKER_LIFE; }
    public static double getDroneW() { return DRONE_W; }
    public static int getDroneLife() { return DRONE_LIFE; }
    public static double getQueenW() { return QUEEN_W; }
    public static int getQueenLife() { return QUEEN_LIFE; }
    public static double getGetHoney() { return GET_HONEY; }
    public static double getBabyWeight() { return BABY_WEIGHT; }
}
