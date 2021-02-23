package dev.sim0n.anticheat.database;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.sim0n.anticheat.AntiCheat;
import dev.sim0n.anticheat.config.Configuration;
import lombok.Getter;
import org.bson.Document;

@Getter
public class DataBaseManager {
    private final AntiCheat plugin = AntiCheat.getInstance();

    private final Configuration config = plugin.getConfiguration();

    private MongoClient client;
    private MongoDatabase database;
    private MongoCollection<Document> logsCollection;

    public DataBaseManager() {
        if (!plugin.getConfiguration().isDatabaseEnabled())
            return;

        client = new MongoClient(new ServerAddress(config.getDatabaseHost()));

        database = client.getDatabase(config.getDatabaseName());
        logsCollection = database.getCollection(config.getDatabaseLogsName());
    }
}
