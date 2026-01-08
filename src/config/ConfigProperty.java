package config;

public enum ConfigProperty {
    STORAGE_DIR("storageDir", "");

    private final String key;
    private final Object defaultValue;

    <T> ConfigProperty(String key, T defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String getKey() { return key; }
    public Object getDefaultValue() { return defaultValue; }
}
