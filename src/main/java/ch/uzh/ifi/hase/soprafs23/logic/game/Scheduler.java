package ch.uzh.ifi.hase.soprafs23.logic.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static Scheduler instance;
    private ScheduledExecutorService executorService;

    private Scheduler() {
        executorService = Executors.newSingleThreadScheduledExecutor();
    }

    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }
    
    public void schedule(Runnable command, int delaySeconds) {
        executorService.schedule(command, delaySeconds, TimeUnit.SECONDS);
    }
}
