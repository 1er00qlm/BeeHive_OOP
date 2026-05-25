public abstract class Bee {
    private static int nextId = 1;
    private int id;
    private double weight;
    private int age;
    private int maxAge;
    private boolean alive;
    private double totalEaten;

    public Bee(double weight, int maxAge) {
        this.id = nextId++;
        this.weight = weight;
        this.age = 0;
        this.maxAge = maxAge;
        this.alive = true;
        this.totalEaten = 0;
    }

    public int getId() { return id; }
    public double getWeight() { return weight; }
    public int getAge() { return age; }
    public boolean isAlive() { return alive; }
    protected void setAlive(boolean a) { alive = a; }
    protected void increaseAge() { age++; }
    protected void addWeight(double w) { weight += w; }

    protected boolean eatDaily(Simulation sim) {
        double need = weight * Simulation.getFoodNeed();
        if (sim.consumeHoney(need)) {
            totalEaten += need;
            return true;
        } else {
            die(sim, "hungry");
            return false;
        }
    }

    protected void die(Simulation sim, String reason) {
        if (!alive) return;
        alive = false;
        sim.addCorpse(this);
        sim.removeBee(this);
    }

    public void update(Simulation sim) {
        if (!alive) return;
        if (!eatDaily(sim)) return;
        increaseAge();
        if (age >= maxAge) {
            die(sim, "old");
            return;
        }
        doWork(sim);
    }

    public abstract void doWork(Simulation sim);
}