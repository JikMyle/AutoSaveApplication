package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class PollingService {
    private final Logger LOGGER = LogManager.getLogger();
    private final Long POLL_INTERVAL_MILLIS = 2000L;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private String[] watchlist;
    private ProcessHandle activeProcess;

    public PollingService(String[] processes) {
        setWatchlist(processes);
    }

    public void stop() {
        executorService.shutdownNow();
        LOGGER.warn("Polling has been stopped");
    }

    public void setWatchlist(String[] processes) {
        watchlist = processes != null ? processes.clone() : new String[0];
    }

    public String[] getWatchlist() {
        return watchlist != null ? watchlist.clone() : new String[0];
    }

    public ProcessHandle getActiveProcess() {
        return activeProcess;
    }

    public CompletableFuture<ProcessHandle> poll() {
        if(watchlist == null || watchlist.length == 0) {
            LOGGER.warn("Could not begin polling, app watchlist is empty");
            return CompletableFuture.completedFuture(null);
        }

        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Searching for running process");

            while (!Thread.currentThread().isInterrupted() && activeProcess == null) {
                try (Stream<ProcessHandle> allProcesses = ProcessHandle.allProcesses()) {
                    Optional<ProcessHandle> found = allProcesses
                            .filter(ph -> {
                                String cmd = ph.info().command().orElse("");
                                return !cmd.isBlank() &&
                                        Arrays.stream(watchlist)
                                                .anyMatch(cmd::contains);
                            })
                            .findFirst();

                    if (found.isPresent()) {
                        ProcessHandle ph = found.get();
                        LOGGER.info("Running process detected: {}", ph);
                        activeProcess = ph;

                        ph.onExit().thenRunAsync(() -> {
                            activeProcess = null;
                            LOGGER.info("Process {} has been terminated", ph);
                        }, executorService);

                        return ph;
                    }
                }

                if (activeProcess == null) {
                    try {
                        Thread.sleep(POLL_INTERVAL_MILLIS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        LOGGER.warn("Polling interrupted");
                        return null;
                    }
                }
            }

            return null;
        }, executorService);
    }
}
