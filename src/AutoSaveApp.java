import config.AppConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import services.PollingService;

public class AutoSaveApp {
    private final Logger LOGGER = LogManager.getLogger();
    private final AppConfig config;

    PollingService pollingService;

    public AutoSaveApp() {
        // Handles app configurations and properties
        config = new AppConfig();

        // Service for detecting running applications
        pollingService = new PollingService(new String[] {"/usr/bin/gnome-calculator"});
        pollingService.poll();

        // TODO: Create save backup service, to create a backup of files
    }

    public static void main() {
        // NOTE TO SELF: Prioritize backup generation process before anything else
        // NOTE TO SELF: DO NOT think about refining, make it functional first
        // NOTE TO SELF: DO NOT stress about logistics, changes can be made in the future
        new AutoSaveApp();
    }
}
