import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AutoSaveApp {
    private static final Logger LOGGER = LogManager.getLogger();

    String os;
    Path storageDir;

    public AutoSaveApp() {
        checkOs();
        initializePaths();

        // TODO: Create app polling service, to check if a game is running
        // TODO: Create save backup service, to create a backup of files
    }

    private void checkOs() {
        String osName = System.getProperty("os.name").toLowerCase();
        LOGGER.info("Detected operating system: {}", System.getProperty("os.name"));

        if(osName.contains("win")) {
            os = "win";
        } else if(osName.contains("mac")) {
            os = "mac";
        } else {
            os = "linux";
        }
    }

    private void initializePaths() {
        initializeStorageDir();
    }

    private void initializeStorageDir() {
        String homeDir = System.getProperty("user.home");
        String docsFolder = "Documents";
        String storageFolder = "SaveBackups";

        Path path = Paths.get(homeDir, docsFolder, storageFolder);

        if(Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (Exception e) {
                LOGGER.error("Error in initializing storage directory:", e);
            }
        }

        storageDir = path;
        LOGGER.info("Initialized storage directory at: {}", path);
    }
}
