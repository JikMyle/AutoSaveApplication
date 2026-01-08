package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppConfig {
    private static final String APP_NAME = "AutoSaB";
    private final Logger LOGGER = LogManager.getLogger();

    private String appDataDir;
    private String configFile;

    public AppConfig() {

    }
}
