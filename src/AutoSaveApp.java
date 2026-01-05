import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AutoSaveApp {
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
                System.out.println(e.getMessage());
            }
        }

        storageDir = path;
    }
}
