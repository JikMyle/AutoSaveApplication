package services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackupService {
    private final static Logger LOGGER = LogManager.getLogger();
    private final int INSTANCE_LIMIT;

    public BackupService() {
        INSTANCE_LIMIT = 2; // Replace value with AppConfig variable
    }

    /**
     * @param source the directory to back up
     * @param target the directory where backups are stored
     * @throws IllegalArgumentException if source or target are not valid directories
     * @throws IOException if backup operations fails due to file system errors
     */
    public void backup(Path source, Path target) throws IOException {
        if (!Files.isDirectory(source) || !Files.isDirectory(target)) {
            throw new IllegalArgumentException("Both source and target paths must be a valid directory. " +
                    "Source: " + source + ", Target: " + target);
        }

        checkForInstances(target);
    }

    private void checkForInstances(Path target) throws IOException {
        try (Stream<Path> stream = Files.list(target)) {

            // SUGGESTION: Add filtering to only count valid instances
            // REASON: Directory is accessible by user; user may attempt to create files/folder inside
            // METHOD: Define a naming format, filter based on defined format
            List<Path> list = stream
                    .sorted()
                    .collect(Collectors.toCollection(ArrayList::new));

            if (list.size() > INSTANCE_LIMIT) {
                LOGGER.info("Backup instances exceeds limit: {}, " +
                        "attempting to delete oldest instance", INSTANCE_LIMIT);

                deleteOldBackups(list);
            }
        }
    }

    private void deleteOldBackups(List<Path> paths) throws IOException {
        while (paths.size() > INSTANCE_LIMIT) {
            recursivelyDeleteFiles(paths.removeFirst());
        }
    }

    private void recursivelyDeleteFiles(Path target) throws IOException {
        try (Stream<Path> walk = Files.walk(target)) {
            AtomicBoolean allDeleted = new AtomicBoolean(true);
            walk.sorted(Comparator.reverseOrder())
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            LOGGER.warn("Failed to delete backup at: {}", p);
                            allDeleted.set(false);
                        }
                    });

            if (!allDeleted.get()) {
                LOGGER.warn(
                        "Partial deletion of {}." +
                                "Please manually delete remaining files or wait until next backup operation.",
                        target
                );
            }
        }
    }
}
