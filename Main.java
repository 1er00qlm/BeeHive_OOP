import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;

public class Main extends Application {
    private Simulation simulation;
    private MyTimer timer;
    private volatile boolean running = false;
    private volatile boolean paused = false;

    private Label timeLabel;
    private Label honeyLabel;
    private Label beesLabel;
    private Label deadLabel;
    private TextArea logArea;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Симуляция улья");
        Button startBtn = new Button("Старт");
        Button pauseBtn = new Button("Пауза");
        Button resumeBtn = new Button("Возобновить");
        Button stopBtn = new Button("Стоп");
        Button statBtn = new Button("Статистика");

        timeLabel = new Label("Time: 0");
        honeyLabel = new Label("Honey: 0.0");
        beesLabel = new Label("Bees: 0");
        deadLabel = new Label("Dead: 0");
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefRowCount(15);

        HBox buttons = new HBox(10);
        buttons.getChildren().add(startBtn);
        buttons.getChildren().add(pauseBtn);
        buttons.getChildren().add(resumeBtn);
        buttons.getChildren().add(stopBtn);
        buttons.getChildren().add(statBtn);

        HBox info = new HBox(20);
        info.getChildren().add(timeLabel);
        info.getChildren().add(honeyLabel);
        info.getChildren().add(beesLabel);
        info.getChildren().add(deadLabel);

        VBox root = new VBox(10);
        root.getChildren().add(buttons);
        root.getChildren().add(info);
        root.getChildren().add(logArea);

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        startBtn.setOnAction(new StartHandler());
        pauseBtn.setOnAction(new PauseHandler());
        resumeBtn.setOnAction(new ResumeHandler());
        stopBtn.setOnAction(new StopHandler());
        statBtn.setOnAction(new StatsHandler());
        primaryStage.setOnCloseRequest(new CloseHandler());
    }

    private class MyTimer extends AnimationTimer {
        private long lastUpdate = 0;
        public void handle(long now) {
            if (lastUpdate == 0) {
                lastUpdate = now;
                return;
            }
            if (now - lastUpdate >= 20_000_000) {
                if (running && !paused && simulation.getTime() < Simulation.MAX_TIME) {
                    simulation.step();
                    Platform.runLater(new UIRefresher());
                    if (simulation.getTime() % 100 == 0) {
                        Platform.runLater(new LogAppender());
                    }
                }
                lastUpdate = now;
            }
        }
    }

    private class StartHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent e) {
            startSimulation();
        }
    }

    private class PauseHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent e) {
            pauseSimulation();
        }
    }

    private class ResumeHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent e) {
            resumeSimulation();
        }
    }

    private class StopHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent e) {
            stopSimulation();
        }
    }

    private class StatsHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent e) {
            showStatistics();
        }
    }

    private class CloseHandler implements javafx.event.EventHandler<javafx.stage.WindowEvent> {
        public void handle(javafx.stage.WindowEvent e) {
            stopSimulation();
            Platform.exit();
        }
    }

    private class UIRefresher implements Runnable {
        public void run() {
            timeLabel.setText("Время: " + simulation.getTime());
            honeyLabel.setText(String.format("Меда: %.1f", simulation.getHive().getFood()));
            beesLabel.setText("Пчёл: " + simulation.getAll().size());
            deadLabel.setText("Трупов: " + simulation.getHive().getDeadCount());
        }
    }

    private class LogAppender implements Runnable {
        public void run() {
            logArea.appendText(String.format("[%d мин] Мёда: %.1f | Пчел: %d | Трупы: %d%n",
                    simulation.getTime(), simulation.getHive().getFood(),
                    simulation.getAll().size(), simulation.getHive().getDeadCount()));
        }
    }

    private void startSimulation() {
        if (running) return;
        simulation = new Simulation();
        simulation.start();
        running = true;
        paused = false;
        timer = new MyTimer();
        timer.start();
        logArea.appendText("начало симуляции.\n");
    }

    private void pauseSimulation() {
        if (running && !paused) {
            paused = true;
            logArea.appendText("пауза.\n");
        }
    }

    private void resumeSimulation() {
        if (running && paused) {
            paused = false;
            logArea.appendText("возобновление.\n");
        }
    }

    private void stopSimulation() {
        if (running) {
            running = false;
            if (timer != null) timer.stop();
            logArea.appendText("симуляция остановлена.\n");
            showStatistics();
        }
    }

    private void showStatistics() {
        if (simulation == null) return;
        int larvae = 0, drones = 0, gatherers = 0, cleaners = 0;
        ArrayList all = simulation.getAll();
        for (int i = 0; i < all.size(); i++) {
            Object b = all.get(i);
            if (b instanceof Lichinka) {
                larvae++;
            } else if (b instanceof Truten) {
                drones++;
            } else if (b instanceof Workbee) {
                Workbee w = (Workbee) b;
                if (w.getJob().equals("HONEY_GATHERER")) {
                    gatherers++;
                } else {
                    cleaners++;
                }
            }
        }
        logArea.appendText("\nСТАТИСТИКА\n");
        logArea.appendText("Личинки: " + larvae + ", Трутни: " + drones +
                ", Сборщики: " + gatherers + ", Уборщики: " + cleaners + "\n");
        logArea.appendText("Мёда в улье: " + String.format("%.1f", simulation.getHive().getFood()) + "\n");
    }
}