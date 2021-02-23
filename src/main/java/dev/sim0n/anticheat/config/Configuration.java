package dev.sim0n.anticheat.config;

import lombok.Getter;
@Getter
public class Configuration {
    private boolean databaseEnabled = true;

    private String databaseHost = "127.0.0.1";
    private String databaseName = "nemesis_db";
    private String databaseLogsName = "nemesis_logs";
}
