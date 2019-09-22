package com.mortengredal.logging.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ConfigurationProperties(prefix = "sample.app") //Could probably use a better name than.. sample.app
public class LoggngConfigurationProperties {
    private Set<String> bodyFilters = new HashSet<>(Arrays.asList("password", "key", "salt", "pin", "pincode", "access_token", "refresh_token", "client_secret"));
    private boolean logAsJson = false;

    public Set<String> getBodyFilters() {
        return bodyFilters;
    }

    public void setBodyFilters(Set<String> bodyFilters) {
        this.bodyFilters = bodyFilters;
    }

    public boolean isLogAsJson() {
        return logAsJson;
    }

    public void setLogAsJson(boolean logAsJson) {
        this.logAsJson = logAsJson;
    }
}
