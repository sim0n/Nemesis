package dev.sim0n.anticheat.file;

import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.util.FileUtil;

import java.io.File;

public class FileManager {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private final File dataFolder = plugin.getDataFolder();

    public FileManager() {
        if (!dataFolder.exists())
            dataFolder.mkdir();

        loadFile("mongo", "https://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/3.12.6/mongo-java-driver-3.12.6.jar");
        loadFile("classindex", "https://repo1.maven.org/maven2/org/atteo/classindex/classindex/3.9/classindex-3.9.jar");
        loadFile("fastutil", "https://repo1.maven.org/maven2/it/unimi/dsi/fastutil/8.1.0/fastutil-8.1.0.jar");
    }

    /**
     * Loads a file into memory
     * @param fileName The name of the file to load
     * @param fileUrl The url of the file to load
     */
    private void loadFile(String fileName, String fileUrl) {
        try {
            File lib = new File(dataFolder, String.format("lib/%s.jar", fileName));

            if (!lib.exists()) {
                if (!lib.getParentFile().exists())
                    lib.getParentFile().mkdir();

                plugin.getLogger().info(String.format("Downloading %s...", fileName));
                FileUtil.download(lib, fileUrl);
                plugin.getLogger().info(String.format("Finished downloading %s", fileName));
            }

            FileUtil.injectUrl(lib.toURI().toURL());
        } catch (Exception e) {
            plugin.getLogger().warning(String.format("Failed to download or load %s: %s", fileName, e.getMessage()));
        }
    }
}
