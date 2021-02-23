package dev.sim0n.anticheat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.sim0n.anticheat.check.CheckManager;
import dev.sim0n.anticheat.command.CommandManager;
import dev.sim0n.anticheat.config.Configuration;
import dev.sim0n.anticheat.database.DataBaseManager;
import dev.sim0n.anticheat.file.FileManager;
import dev.sim0n.anticheat.listener.PlayerListener;
import dev.sim0n.anticheat.log.LogManager;
import dev.sim0n.anticheat.net.NetworkManager;
import dev.sim0n.anticheat.net.packet.PacketManager;
import dev.sim0n.anticheat.player.PlayerDataManager;
import dev.sim0n.anticheat.task.ExportLogsTask;
import dev.sim0n.anticheat.task.ServerTickTask;
import dev.sim0n.anticheat.util.CC;
import dev.sim0n.anticheat.util.CommandUtil;
import dev.sim0n.anticheat.violation.ViolationManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

@Getter
public class AntiCheat extends JavaPlugin {
    public static final String PREFIX = CC.BLUE + "[NEM] " + CC.R;

    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static Optional<AntiCheat> instance;

    private Configuration configuration;

    private PlayerDataManager playerDataManager;
    private ViolationManager violationManager;
    private DataBaseManager dataBaseManager;
    private NetworkManager networkManager;
    private CommandManager commandManager;
    private PacketManager packetManager;
    private CheckManager checkManager;
    private FileManager fileManager;
    private LogManager logManager;

    private ServerTickTask serverTickTask;
    private ExportLogsTask exportLogsTask;

    public AntiCheat() {
        instance = Optional.of(this);
    }

    @Override
    public void onEnable() {
        registerConfiguration();
        registerManagers();
        registerListeners();
        registerCommands();
        registerTasks();
    }

    private void registerConfiguration() {
        File configFile = new File(getDataFolder(), "config.json");

        try {
            if (!configFile.exists()) {
                try (FileWriter writer = new FileWriter(configFile)) {
                    GSON.toJson(configuration = new Configuration(), writer);
                }
            }

            try (FileReader reader = new FileReader(configFile)) {
                configuration = GSON.fromJson(reader, Configuration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerManagers() {
        // We need to initialise the file manager first to download all our libraries
        fileManager = new FileManager();

        playerDataManager = new PlayerDataManager();
        violationManager = new ViolationManager();
        dataBaseManager = new DataBaseManager();
        networkManager = new NetworkManager();
        commandManager = new CommandManager();
        packetManager = new PacketManager();
        checkManager = new CheckManager();
        logManager = new LogManager();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    private void registerCommands() {
        commandManager.getCommands().forEach(CommandUtil::registerCommand);
    }

    private void registerTasks() {
        (serverTickTask = new ServerTickTask()).runTaskTimer(this, 0L, 1L);

        if (configuration.isDatabaseEnabled())
            // Export logs every 15 seconds
            (exportLogsTask = new ExportLogsTask()).runTaskTimerAsynchronously(this, 300L, 300L);
    }

    public boolean isLagging() {
        return serverTickTask.isLagging();
    }

    @Override
    public void onDisable() {
        serverTickTask.cancel();
    }

    public static AntiCheat getInstance() {
        return instance.orElseThrow(() -> new IllegalStateException("AntiCheat instance is null."));
    }

}
